
package com.beans.mdxjson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "providerType",
    "providerActive",
    "nameQualifier",
    "taxIDs",
    "areasOFocus",
    "providerLanguages"
})
public class Profile {

    @JsonProperty("providerType")
    private String providerType;
    @JsonProperty("providerActive")
    private ProviderActive providerActive;
    @JsonProperty("nameQualifier")
    private NameQualifier nameQualifier;
    @JsonProperty("taxIDs")
    private List<TaxID> taxIDs = null;
    @JsonProperty("areasOFocus")
    private List<AreasOFocus> areasOFocus = null;
    @JsonProperty("providerLanguages")
    private List<String> providerLanguages = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("providerType")
    public String getProviderType() {
        return providerType;
    }

    @JsonProperty("providerType")
    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    @JsonProperty("providerActive")
    public ProviderActive getProviderActive() {
        return providerActive;
    }

    @JsonProperty("providerActive")
    public void setProviderActive(ProviderActive providerActive) {
        this.providerActive = providerActive;
    }

    @JsonProperty("nameQualifier")
    public NameQualifier getNameQualifier() {
        return nameQualifier;
    }

    @JsonProperty("nameQualifier")
    public void setNameQualifier(NameQualifier nameQualifier) {
        this.nameQualifier = nameQualifier;
    }

    @JsonProperty("taxIDs")
    public List<TaxID> getTaxIDs() {
        return taxIDs;
    }

    @JsonProperty("taxIDs")
    public void setTaxIDs(List<TaxID> taxIDs) {
        this.taxIDs = taxIDs;
    }

    @JsonProperty("areasOFocus")
    public List<AreasOFocus> getAreasOFocus() {
        return areasOFocus;
    }

    @JsonProperty("areasOFocus")
    public void setAreasOFocus(List<AreasOFocus> areasOFocus) {
        this.areasOFocus = areasOFocus;
    }

    @JsonProperty("providerLanguages")
    public List<String> getProviderLanguages() {
        return providerLanguages;
    }

    @JsonProperty("providerLanguages")
    public void setProviderLanguages(List<String> providerLanguages) {
        this.providerLanguages = providerLanguages;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("providerType", providerType).append("providerActive", providerActive).append("nameQualifier", nameQualifier).append("taxIDs", taxIDs).append("areasOFocus", areasOFocus).append("providerLanguages", providerLanguages).append("additionalProperties", additionalProperties).toString();
    }

}
