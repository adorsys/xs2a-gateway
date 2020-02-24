package de.adorsys.xs2a.adapter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import java.util.List;
import java.util.Map;

@Generated("xs2a-adapter-codegen")
public class UpdatePsuIdenticationResponseTO {
    private List<AuthenticationObjectTO> scaMethods;

    @JsonProperty("_links")
    private Map<String, HrefTypeTO> links;

    private ScaStatusTO scaStatus;

    private String psuMessage;

    public List<AuthenticationObjectTO> getScaMethods() {
        return scaMethods;
    }

    public void setScaMethods(List<AuthenticationObjectTO> scaMethods) {
        this.scaMethods = scaMethods;
    }

    public Map<String, HrefTypeTO> getLinks() {
        return links;
    }

    public void setLinks(Map<String, HrefTypeTO> links) {
        this.links = links;
    }

    public ScaStatusTO getScaStatus() {
        return scaStatus;
    }

    public void setScaStatus(ScaStatusTO scaStatus) {
        this.scaStatus = scaStatus;
    }

    public String getPsuMessage() {
        return psuMessage;
    }

    public void setPsuMessage(String psuMessage) {
        this.psuMessage = psuMessage;
    }
}
