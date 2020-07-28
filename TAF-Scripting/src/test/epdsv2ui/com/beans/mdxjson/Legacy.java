
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
    "contractIndicator",
    "nwm"
})
public class Legacy {

    @JsonProperty("contractIndicator")
    private String contractIndicator;
    @JsonProperty("nwm")
    private String nwm;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("contractIndicator")
    public String getContractIndicator() {
        return contractIndicator;
    }

    @JsonProperty("contractIndicator")
    public void setContractIndicator(String contractIndicator) {
        this.contractIndicator = contractIndicator;
    }

    @JsonProperty("nwm")
    public String getNwm() {
        return nwm;
    }

    @JsonProperty("nwm")
    public void setNwm(String nwm) {
        this.nwm = nwm;
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
        return new ToStringBuilder(this).append("contractIndicator", contractIndicator).append("nwm", nwm).append("additionalProperties", additionalProperties).toString();
    }

}
