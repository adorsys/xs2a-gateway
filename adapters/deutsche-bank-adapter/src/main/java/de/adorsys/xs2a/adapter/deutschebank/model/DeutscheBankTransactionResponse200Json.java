package de.adorsys.xs2a.adapter.deutschebank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.adorsys.xs2a.adapter.api.model.AccountReference;
import de.adorsys.xs2a.adapter.api.model.Balance;
import de.adorsys.xs2a.adapter.api.model.HrefType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeutscheBankTransactionResponse200Json {
    private AccountReference account;
    private DeutscheBankAccountReport transactions;
    private List<Balance> balances;

    @JsonProperty("_links")
    private Map<String, HrefType> links;

    public AccountReference getAccount() {
        return account;
    }

    public void setAccount(AccountReference account) {
        this.account = account;
    }

    public DeutscheBankAccountReport getTransactions() {
        return transactions;
    }

    public void setTransactions(DeutscheBankAccountReport transactions) {
        this.transactions = transactions;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public Map<String, HrefType> getLinks() {
        return links;
    }

    public void setLinks(Map<String, HrefType> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeutscheBankTransactionResponse200Json that = (DeutscheBankTransactionResponse200Json) o;
        return Objects.equals(account, that.account) &&
            Objects.equals(transactions, that.transactions) &&
            Objects.equals(balances, that.balances) &&
            Objects.equals(links, that.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account,
            transactions,
            balances,
            links);
    }
}
