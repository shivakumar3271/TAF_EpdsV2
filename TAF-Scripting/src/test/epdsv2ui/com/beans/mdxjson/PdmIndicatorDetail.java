
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
    "pdmIndicator",
    "pdmIndicatorActive"
})
public class PdmIndicatorDetail {

    @JsonProperty("pdmIndicator")
    private String pdmIndicator;
    @JsonProperty("pdmIndicatorActive")
    private PdmIndicatorActive pdmIndicatorActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pdmIndicator")
    public String getPdmIndicator() {
        return pdmIndicator;
    }

    @JsonProperty("pdmIndicator")
    public void setPdmIndicator(String pdmIndicator) {
        this.pdmIndicator = pdmIndicator;
    }

    @JsonProperty("pdmIndicatorActive")
    public PdmIndicatorActive getPdmIndicatorActive() {
        return pdmIndicatorActive;
    }

    @JsonProperty("pdmIndicatorActive")
    public void setPdmIndicatorActive(PdmIndicatorActive pdmIndicatorActive) {
        this.pdmIndicatorActive = pdmIndicatorActive;
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
        return new ToStringBuilder(this).append("pdmIndicator", pdmIndicator).append("pdmIndicatorActive", pdmIndicatorActive).append("additionalProperties", additionalProperties).toString();
    }

}
