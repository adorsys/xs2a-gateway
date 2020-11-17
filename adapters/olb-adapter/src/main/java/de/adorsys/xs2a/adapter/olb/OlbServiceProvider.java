package de.adorsys.xs2a.adapter.olb;

import de.adorsys.xs2a.adapter.api.*;
import de.adorsys.xs2a.adapter.api.http.HttpClientConfig;
import de.adorsys.xs2a.adapter.api.http.HttpClientFactory;
import de.adorsys.xs2a.adapter.api.link.LinksRewriter;
import de.adorsys.xs2a.adapter.api.model.Aspsp;
import de.adorsys.xs2a.adapter.impl.BasePaymentInitiationService;

public class OlbServiceProvider implements AccountInformationServiceProvider, PaymentInitiationServiceProvider {

    @Override
    public AccountInformationService getAccountInformationService(Aspsp aspsp,
                                                                  HttpClientFactory httpClientFactory,
                                                                  Pkcs12KeyStore keyStore,
                                                                  LinksRewriter linksRewriter) {
        return new OlbAccountInformationService(aspsp,
            httpClientFactory.getHttpClient(getAdapterId()),
            linksRewriter,
            null);
    }

    @Override
    public AccountInformationService getAccountInformationService(Aspsp aspsp, LinksRewriter linksRewriter, HttpClientConfig httpClientConfig) {
        return new OlbAccountInformationService(aspsp,
            httpClientConfig.getClientFactory().getHttpClient(getAdapterId()),
            linksRewriter,
            httpClientConfig.getLogSanitizer());
    }

    @Override
    public PaymentInitiationService getPaymentInitiationService(Aspsp aspsp,
                                                                HttpClientFactory httpClientFactory,
                                                                Pkcs12KeyStore keyStore,
                                                                LinksRewriter linksRewriter) {
        return new BasePaymentInitiationService(aspsp,
            httpClientFactory.getHttpClient(getAdapterId()),
            linksRewriter,
            null);
    }

    @Override
    public PaymentInitiationService getPaymentInitiationService(Aspsp aspsp, HttpClientConfig clientConfig, LinksRewriter linksRewriter) {
        return new BasePaymentInitiationService(aspsp,
            clientConfig.getClientFactory().getHttpClient(getAdapterId()),
            linksRewriter,
            clientConfig.getLogSanitizer());
    }

    @Override
    public String getAdapterId() {
        return "olb-adapter";
    }
}
