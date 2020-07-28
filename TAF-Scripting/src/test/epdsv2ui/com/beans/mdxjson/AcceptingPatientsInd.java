
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
    "acceptingPatientsIndicator",
    "acceptingPatientsIndicatorActive"
})
public class AcceptingPatientsInd {

    @JsonProperty("acceptingPatientsIndicator")
    private String acceptingPatientsIndicator;
    @JsonProperty("acceptingPatientsIndicatorActive")
    private AcceptingPatientsIndicatorActive acceptingPatientsIndicatorActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("acceptingPatientsIndicator")
    public String getAcceptingPatientsIndicator() {
        return acceptingPatientsIndicator;
    }

    @JsonProperty("acceptingPatientsIndicator")
    public void setAcceptingPatientsIndicator(String acceptingPatientsIndicator) {
        this.acceptingPatientsIndicator = acceptingPatientsIndicator;
    }

    @JsonProperty("acceptingPatientsIndicatorActive")
    public AcceptingPatientsIndicatorActive getAcceptingPatientsIndicatorActive() {
        return acceptingPatientsIndicatorActive;
    }

    @JsonProperty("acceptingPatientsIndicatorActive")
    public void setAcceptingPatientsIndicatorActive(AcceptingPatientsIndicatorActive acceptingPatientsIndicatorActive) {
        this.acceptingPatientsIndicatorActive = acceptingPatientsIndicatorActive;
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
        return new ToStringBuilder(this).append("acceptingPatientsIndicator", acceptingPatientsIndicator).append("acceptingPatientsIndicatorActive", acceptingPatientsIndicatorActive).append("additionalProperties", additionalProperties).toString();
    }

}
