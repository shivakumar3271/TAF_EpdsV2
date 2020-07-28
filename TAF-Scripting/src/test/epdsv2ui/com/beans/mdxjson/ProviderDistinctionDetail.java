
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
    "providerDistinctionCode",
    "providerDistinctionDetailsActive"
})
public class ProviderDistinctionDetail {

    @JsonProperty("providerDistinctionCode")
    private String providerDistinctionCode;
    @JsonProperty("providerDistinctionDetailsActive")
    private ProviderDistinctionDetailsActive providerDistinctionDetailsActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("providerDistinctionCode")
    public String getProviderDistinctionCode() {
        return providerDistinctionCode;
    }

    @JsonProperty("providerDistinctionCode")
    public void setProviderDistinctionCode(String providerDistinctionCode) {
        this.providerDistinctionCode = providerDistinctionCode;
    }

    @JsonProperty("providerDistinctionDetailsActive")
    public ProviderDistinctionDetailsActive getProviderDistinctionDetailsActive() {
        return providerDistinctionDetailsActive;
    }

    @JsonProperty("providerDistinctionDetailsActive")
    public void setProviderDistinctionDetailsActive(ProviderDistinctionDetailsActive providerDistinctionDetailsActive) {
        this.providerDistinctionDetailsActive = providerDistinctionDetailsActive;
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
        return new ToStringBuilder(this).append("providerDistinctionCode", providerDistinctionCode).append("providerDistinctionDetailsActive", providerDistinctionDetailsActive).append("additionalProperties", additionalProperties).toString();
    }

}
