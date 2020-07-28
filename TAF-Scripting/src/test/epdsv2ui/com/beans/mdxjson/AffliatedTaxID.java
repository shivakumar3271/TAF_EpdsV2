
package com.beans.mdxjson;

import java.util.HashMap;
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
    "taxIDValue",
    "taxIDIndicator",
    "taxIDActive"
})
public class AffliatedTaxID {

    @JsonProperty("taxIDValue")
    private String taxIDValue;
    @JsonProperty("taxIDIndicator")
    private String taxIDIndicator;
    @JsonProperty("taxIDActive")
    private TaxIDActive taxIDActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("taxIDValue")
    public String getTaxIDValue() {
        return taxIDValue;
    }

    @JsonProperty("taxIDValue")
    public void setTaxIDValue(String taxIDValue) {
        this.taxIDValue = taxIDValue;
    }

    @JsonProperty("taxIDIndicator")
    public String getTaxIDIndicator() {
        return taxIDIndicator;
    }

    @JsonProperty("taxIDIndicator")
    public void setTaxIDIndicator(String taxIDIndicator) {
        this.taxIDIndicator = taxIDIndicator;
    }

    @JsonProperty("taxIDActive")
    public TaxIDActive getTaxIDActive() {
        return taxIDActive;
    }

    @JsonProperty("taxIDActive")
    public void setTaxIDActive(TaxIDActive taxIDActive) {
        this.taxIDActive = taxIDActive;
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
        return new ToStringBuilder(this).append("taxIDValue", taxIDValue).append("taxIDIndicator", taxIDIndicator).append("taxIDActive", taxIDActive).append("additionalProperties", additionalProperties).toString();
    }

}
