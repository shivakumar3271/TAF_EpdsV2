
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
    "npiEligibilityType",
    "npiType",
    "npiValue",
    "npiActive"
})
public class NpiDetail {

    @JsonProperty("npiEligibilityType")
    private String npiEligibilityType;
    @JsonProperty("npiType")
    private String npiType;
    @JsonProperty("npiValue")
    private String npiValue;
    @JsonProperty("npiActive")
    private NpiActive npiActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("npiEligibilityType")
    public String getNpiEligibilityType() {
        return npiEligibilityType;
    }

    @JsonProperty("npiEligibilityType")
    public void setNpiEligibilityType(String npiEligibilityType) {
        this.npiEligibilityType = npiEligibilityType;
    }

    @JsonProperty("npiType")
    public String getNpiType() {
        return npiType;
    }

    @JsonProperty("npiType")
    public void setNpiType(String npiType) {
        this.npiType = npiType;
    }

    @JsonProperty("npiValue")
    public String getNpiValue() {
        return npiValue;
    }

    @JsonProperty("npiValue")
    public void setNpiValue(String npiValue) {
        this.npiValue = npiValue;
    }

    @JsonProperty("npiActive")
    public NpiActive getNpiActive() {
        return npiActive;
    }

    @JsonProperty("npiActive")
    public void setNpiActive(NpiActive npiActive) {
        this.npiActive = npiActive;
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
        return new ToStringBuilder(this).append("npiEligibilityType", npiEligibilityType).append("npiType", npiType).append("npiValue", npiValue).append("npiActive", npiActive).append("additionalProperties", additionalProperties).toString();
    }

}
