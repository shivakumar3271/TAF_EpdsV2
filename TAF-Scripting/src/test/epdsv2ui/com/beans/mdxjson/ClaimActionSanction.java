
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
    "claimActionSanctionType",
    "claimActionSanctionValue",
    "claimActionSanctionCriteria",
    "lowRange",
    "highRange",
    "claimActionSanctionActive"
})
public class ClaimActionSanction {

    @JsonProperty("claimActionSanctionType")
    private String claimActionSanctionType;
    @JsonProperty("claimActionSanctionValue")
    private String claimActionSanctionValue;
    @JsonProperty("claimActionSanctionCriteria")
    private String claimActionSanctionCriteria;
    @JsonProperty("lowRange")
    private String lowRange;
    @JsonProperty("highRange")
    private String highRange;
    @JsonProperty("claimActionSanctionActive")
    private ClaimActionSanctionActive claimActionSanctionActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("claimActionSanctionType")
    public String getClaimActionSanctionType() {
        return claimActionSanctionType;
    }

    @JsonProperty("claimActionSanctionType")
    public void setClaimActionSanctionType(String claimActionSanctionType) {
        this.claimActionSanctionType = claimActionSanctionType;
    }

    @JsonProperty("claimActionSanctionValue")
    public String getClaimActionSanctionValue() {
        return claimActionSanctionValue;
    }

    @JsonProperty("claimActionSanctionValue")
    public void setClaimActionSanctionValue(String claimActionSanctionValue) {
        this.claimActionSanctionValue = claimActionSanctionValue;
    }

    @JsonProperty("claimActionSanctionCriteria")
    public String getClaimActionSanctionCriteria() {
        return claimActionSanctionCriteria;
    }

    @JsonProperty("claimActionSanctionCriteria")
    public void setClaimActionSanctionCriteria(String claimActionSanctionCriteria) {
        this.claimActionSanctionCriteria = claimActionSanctionCriteria;
    }

    @JsonProperty("lowRange")
    public String getLowRange() {
        return lowRange;
    }

    @JsonProperty("lowRange")
    public void setLowRange(String lowRange) {
        this.lowRange = lowRange;
    }

    @JsonProperty("highRange")
    public String getHighRange() {
        return highRange;
    }

    @JsonProperty("highRange")
    public void setHighRange(String highRange) {
        this.highRange = highRange;
    }

    @JsonProperty("claimActionSanctionActive")
    public ClaimActionSanctionActive getClaimActionSanctionActive() {
        return claimActionSanctionActive;
    }

    @JsonProperty("claimActionSanctionActive")
    public void setClaimActionSanctionActive(ClaimActionSanctionActive claimActionSanctionActive) {
        this.claimActionSanctionActive = claimActionSanctionActive;
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
        return new ToStringBuilder(this).append("claimActionSanctionType", claimActionSanctionType).append("claimActionSanctionValue", claimActionSanctionValue).append("claimActionSanctionCriteria", claimActionSanctionCriteria).append("lowRange", lowRange).append("highRange", highRange).append("claimActionSanctionActive", claimActionSanctionActive).append("additionalProperties", additionalProperties).toString();
    }

}
