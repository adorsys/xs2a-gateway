package de.adorsys.xs2a.adapter.ing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IngPaymentInitiationResponse {
    private String transactionStatus;

    private String paymentId;

    @JsonProperty("_links")
    private IngLinks links;

    private List<IngTppMessage> tppMessages;

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public IngLinks getLinks() {
        return links;
    }

    public void setLinks(IngLinks links) {
        this.links = links;
    }

    public List<IngTppMessage> getTppMessages() {
        return tppMessages;
    }

    public void setTppMessages(List<IngTppMessage> tppMessages) {
        this.tppMessages = tppMessages;
    }
}
