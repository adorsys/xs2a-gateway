package de.adorsys.xs2a.adapter.ing;

import de.adorsys.xs2a.adapter.api.model.*;
import de.adorsys.xs2a.adapter.service.PaymentInitiationService;
import de.adorsys.xs2a.adapter.service.RequestParams;
import de.adorsys.xs2a.adapter.service.Response;
import de.adorsys.xs2a.adapter.test.ServiceWireMockTest;
import de.adorsys.xs2a.adapter.test.TestRequestResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceWireMockTest(IngServiceProvider.class)
class IngPaymentInitiationServiceWireMockTest {
    public static final String PAYMENT_ID = "111be84f-a62b-469b-b964-792da2840afb";
    public static final String PERIODIC_PAYMENT_ID = "111be84f-a62b-469b-b964-792ea2847abd";
    private final PaymentInitiationService service;

    IngPaymentInitiationServiceWireMockTest(PaymentInitiationService service) {
        this.service = service;
    }

    @Test
    void initiatePayment() throws Exception {
        TestRequestResponse requestResponse =
            new TestRequestResponse("pis/payments/sepa-credit-transfers/initiate-payment.json");

        Response<PaymentInitationRequestResponse201> response =
            service.initiatePayment(PaymentService.PAYMENTS.toString(),
                PaymentProduct.SEPA_CREDIT_TRANSFERS.toString(),
                requestResponse.requestHeaders(),
                RequestParams.empty(),
                requestResponse.requestBody(PaymentInitiationJson.class));

        PaymentInitationRequestResponse201 expectedBody =
            requestResponse.responseBody(PaymentInitationRequestResponse201.class);
        // sca redirect link is stubbed via wiremock (has dynamic port)
        expectedBody.getLinks().put("scaRedirect", response.getBody().getLinks().get("scaRedirect"));
        assertThat(response.getBody()).isEqualTo(expectedBody);
    }

    @Test
    void getPaymentStatus() throws Exception {
        TestRequestResponse requestResponse =
            new TestRequestResponse("pis/payments/sepa-credit-transfers/get-payment-status.json");

        Response<PaymentInitiationStatusResponse200Json> response =
            service.getPaymentInitiationStatus(PaymentService.PAYMENTS.toString(),
                PaymentProduct.SEPA_CREDIT_TRANSFERS.toString(),
                PAYMENT_ID,
                requestResponse.requestHeaders(),
                RequestParams.empty());

        assertThat(response.getBody())
            .isEqualTo(requestResponse.responseBody(PaymentInitiationStatusResponse200Json.class));
    }

    @Test
    void initiatePeriodicPayment() throws Exception {
        TestRequestResponse requestResponse =
            new TestRequestResponse("pis/periodic-payments/sepa-credit-transfers/initiate-payment.json");

        Response<PaymentInitationRequestResponse201> response =
            service.initiatePayment(PaymentService.PERIODIC_PAYMENTS.toString(),
                PaymentProduct.SEPA_CREDIT_TRANSFERS.toString(),
                requestResponse.requestHeaders(),
                RequestParams.empty(),
                requestResponse.requestBody(PeriodicPaymentInitiationJson.class));

        PaymentInitationRequestResponse201 expectedBody =
            requestResponse.responseBody(PaymentInitationRequestResponse201.class);
        // sca redirect link is stubbed via wiremock (has dynamic port)
        expectedBody.getLinks().put("scaRedirect", response.getBody().getLinks().get("scaRedirect"));
        assertThat(response.getBody()).isEqualTo(expectedBody);
    }


    @Test
    void getPeriodicPaymentStatus() throws Exception {
        TestRequestResponse requestResponse =
            new TestRequestResponse("pis/periodic-payments/sepa-credit-transfers/get-payment-status.json");

        Response<PaymentInitiationStatusResponse200Json> response =
            service.getPaymentInitiationStatus(PaymentService.PERIODIC_PAYMENTS.toString(),
                PaymentProduct.SEPA_CREDIT_TRANSFERS.toString(),
                PERIODIC_PAYMENT_ID,
                requestResponse.requestHeaders(),
                RequestParams.empty());

        assertThat(response.getBody())
            .isEqualTo(requestResponse.responseBody(PaymentInitiationStatusResponse200Json.class));
    }
}
