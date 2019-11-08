package de.adorsys.xs2a.adapter.service.loader;

import de.adorsys.xs2a.adapter.http.HttpClientFactory;
import de.adorsys.xs2a.adapter.service.*;
import de.adorsys.xs2a.adapter.service.exception.AdapterNotFoundException;
import de.adorsys.xs2a.adapter.service.exception.AspspRegistrationNotFoundException;
import de.adorsys.xs2a.adapter.service.model.Aspsp;
import de.adorsys.xs2a.adapter.service.provider.AccountInformationServiceProvider;
import de.adorsys.xs2a.adapter.service.provider.AdapterServiceProvider;
import de.adorsys.xs2a.adapter.service.provider.DownloadServiceProvider;
import de.adorsys.xs2a.adapter.service.provider.PaymentInitiationServiceProvider;
import org.slf4j.MDC;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.StreamSupport;

public class AdapterServiceLoader {
    private static final String EMPTY_STRING = "";

    private final AspspReadOnlyRepository aspspRepository;
    protected final Pkcs12KeyStore keyStore;
    protected final HttpClientFactory httpClientFactory;
    private final ConcurrentMap<Class<?>, ServiceLoader<? extends AdapterServiceProvider>> serviceLoaders = new ConcurrentHashMap<>();

    public AdapterServiceLoader(AspspReadOnlyRepository aspspRepository,
                                Pkcs12KeyStore keyStore,
                                HttpClientFactory httpClientFactory) {
        this.aspspRepository = aspspRepository;
        this.keyStore = keyStore;
        this.httpClientFactory = httpClientFactory;
    }

    public AccountInformationService getAccountInformationService(RequestHeaders requestHeaders) {
        Aspsp aspsp = getAspsp(requestHeaders);
        String adapterId = aspsp.getAdapterId();
        String baseUrl = aspsp.getUrl();
        return getServiceProvider(AccountInformationServiceProvider.class, adapterId)
            .orElseThrow(() -> new AdapterNotFoundException(adapterId))
            .getAccountInformationService(baseUrl, httpClientFactory, keyStore);
    }

    protected Aspsp getAspsp(RequestHeaders requestHeaders) {
        Optional<String> aspspId = requestHeaders.getAspspId();
        Optional<String> bankCode = requestHeaders.getBankCode();
        Optional<String> bic = requestHeaders.getBic();
        if (aspspId.isPresent()) {
            return aspspRepository.findById(aspspId.get())
                .orElseThrow(() -> new AspspRegistrationNotFoundException("No ASPSP was found with id: " + aspspId.get()));
        }
        if (!bankCode.isPresent() && !bic.isPresent()) {
            throw new AspspRegistrationNotFoundException("None of " + RequestHeaders.X_GTW_ASPSP_ID + ", "
                + RequestHeaders.X_GTW_BIC + ", " + RequestHeaders.X_GTW_BANK_CODE
                + " headers were provided to identify the ASPSP");
        }

        List<Aspsp> aspsps;

        if (bankCode.isPresent() && bic.isPresent()) {
            Aspsp aspsp = new Aspsp();
            aspsp.setBankCode(bankCode.get());
            aspsp.setBic(bic.get());

            aspsps = aspspRepository.findLike(aspsp);
        } else if (bankCode.isPresent()) {
            aspsps = aspspRepository.findByBankCode(bankCode.get());
        } else {
            aspsps = aspspRepository.findByBic(bic.get());
        }

        if (aspsps.isEmpty()) {
            throw new AspspRegistrationNotFoundException(
                buildAspspNotFoundErrorMessage(bankCode.orElse(EMPTY_STRING), bic.orElse(EMPTY_STRING))
            );
        } else if (aspsps.size() > 1) {
            throw new AspspRegistrationNotFoundException(
                buildAspspCouldNotBeIdentifiedErrorMessage(aspsps.size(), bankCode.orElse(EMPTY_STRING), bic.orElse(EMPTY_STRING))
            );
        }
        return aspsps.get(0);
    }

    public <T extends AdapterServiceProvider> Optional<T> getServiceProvider(Class<T> klass, String adapterId) {
        MDC.put("adapterId", adapterId);

        ServiceLoader<T> serviceLoader = getServiceLoader(klass);

        return StreamSupport.stream(serviceLoader.spliterator(), false)
            .filter(provider -> provider.getAdapterId().equalsIgnoreCase(adapterId))
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    private <T extends AdapterServiceProvider> ServiceLoader<T> getServiceLoader(Class<T> klass) {
        return (ServiceLoader<T>) serviceLoaders.computeIfAbsent(klass, k -> ServiceLoader.load(klass));
    }

    public PaymentInitiationService getPaymentInitiationService(RequestHeaders requestHeaders) {
        Aspsp aspsp = getAspsp(requestHeaders);
        String adapterId = aspsp.getAdapterId();
        String baseUrl = aspsp.getUrl();
        return getServiceProvider(PaymentInitiationServiceProvider.class, adapterId)
            .orElseThrow(() -> new AdapterNotFoundException(adapterId))
            .getPaymentInitiationService(baseUrl, httpClientFactory, keyStore);
    }

    public Oauth2Service getOauth2Service(RequestHeaders requestHeaders) {
        Aspsp aspsp = getAspsp(requestHeaders);
        String adapterId = aspsp.getAdapterId();
        String baseUrl = aspsp.getIdpUrl() != null ? aspsp.getIdpUrl() : aspsp.getUrl();
        return getServiceProvider(Oauth2ServiceFactory.class, adapterId)
            .orElseThrow(() -> new AdapterNotFoundException(adapterId))
            .getOauth2Service(baseUrl, httpClientFactory, keyStore);
    }

    public DownloadService getDownloadService(RequestHeaders requestHeaders) {
        Aspsp aspsp = getAspsp(requestHeaders);
        String adapterId = aspsp.getAdapterId();
        String baseUrl = Optional.ofNullable(aspsp.getIdpUrl()).orElseGet(aspsp::getUrl);
        return getServiceProvider(DownloadServiceProvider.class, adapterId)
                   .orElseThrow(() -> new AdapterNotFoundException(adapterId))
                   .getDownloadService(baseUrl, httpClientFactory, keyStore);
    }

    private String buildAspspNotFoundErrorMessage(String bankCode, String bic) {
        return appendNotEmptyBankCodeAndBic(new StringBuilder("No ASPSP was found with "), bankCode, bic);
    }

    private String buildAspspCouldNotBeIdentifiedErrorMessage(int numberOfEntries, String bankCode, String bic) {
        return appendNotEmptyBankCodeAndBic(
            new StringBuilder(String.format("The ASPSP could not be identified: %s registry entries found for ", numberOfEntries)),
            bankCode, bic
        );
    }

    private String appendNotEmptyBankCodeAndBic(StringBuilder errorMessageBuilder,
                                                String bankCode, String bic) {
        if (!bankCode.isEmpty() && !bic.isEmpty()) {
            return errorMessageBuilder.append("bank code ")
                       .append(bankCode)
                       .append(" and ")
                       .append("BIC ")
                       .append(bic)
                       .toString();
        }

        if (!bankCode.isEmpty()) {
            return errorMessageBuilder.append("bank code ")
                       .append(bankCode)
                       .toString();
        }

        if (!bic.isEmpty()) {
            return errorMessageBuilder.append("BIC ")
                       .append(bic)
                       .toString();
        }

        return EMPTY_STRING;
    }
}
