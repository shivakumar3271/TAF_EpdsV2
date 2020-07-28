
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
    "bundledPaymentProcessMode",
    "bundledPaymentProcessActive"
})
public class BundledPaymentProcess {

    @JsonProperty("bundledPaymentProcessMode")
    private String bundledPaymentProcessMode;
    @JsonProperty("bundledPaymentProcessActive")
    private BundledPaymentProcessActive bundledPaymentProcessActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bundledPaymentProcessMode")
    public String getBundledPaymentProcessMode() {
        return bundledPaymentProcessMode;
    }

    @JsonProperty("bundledPaymentProcessMode")
    public void setBundledPaymentProcessMode(String bundledPaymentProcessMode) {
        this.bundledPaymentProcessMode = bundledPaymentProcessMode;
    }

    @JsonProperty("bundledPaymentProcessActive")
    public BundledPaymentProcessActive getBundledPaymentProcessActive() {
        return bundledPaymentProcessActive;
    }

    @JsonProperty("bundledPaymentProcessActive")
    public void setBundledPaymentProcessActive(BundledPaymentProcessActive bundledPaymentProcessActive) {
        this.bundledPaymentProcessActive = bundledPaymentProcessActive;
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
        return new ToStringBuilder(this).append("bundledPaymentProcessMode", bundledPaymentProcessMode).append("bundledPaymentProcessActive", bundledPaymentProcessActive).append("additionalProperties", additionalProperties).toString();
    }

}
