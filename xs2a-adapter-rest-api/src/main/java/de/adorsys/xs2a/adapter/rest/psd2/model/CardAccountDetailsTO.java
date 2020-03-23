package de.adorsys.xs2a.adapter.rest.psd2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CardAccountDetailsTO {
    private String resourceId;

    private String maskedPan;

    private String currency;

    private String ownerName;

    private String name;

    private String displayName;

    private String product;

    private String status;

    private String usage;

    private String details;

    private AmountTO creditLimit;

    private List<BalanceTO> balances;

    @JsonProperty("_links")
    private Map<String, HrefTypeTO> links;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public AmountTO getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(AmountTO creditLimit) {
        this.creditLimit = creditLimit;
    }

    public List<BalanceTO> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceTO> balances) {
        this.balances = balances;
    }

    public Map<String, HrefTypeTO> getLinks() {
        return links;
    }

    public void setLinks(Map<String, HrefTypeTO> links) {
        this.links = links;
    }
}
