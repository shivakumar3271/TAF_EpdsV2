
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
    "networkID",
    "reimbursementValue",
    "reimbursementActive"
})
public class Reimbursement {

    @JsonProperty("networkID")
    private String networkID;
    @JsonProperty("reimbursementValue")
    private String reimbursementValue;
    @JsonProperty("reimbursementActive")
    private ReimbursementActive reimbursementActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("networkID")
    public String getNetworkID() {
        return networkID;
    }

    @JsonProperty("networkID")
    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    @JsonProperty("reimbursementValue")
    public String getReimbursementValue() {
        return reimbursementValue;
    }

    @JsonProperty("reimbursementValue")
    public void setReimbursementValue(String reimbursementValue) {
        this.reimbursementValue = reimbursementValue;
    }

    @JsonProperty("reimbursementActive")
    public ReimbursementActive getReimbursementActive() {
        return reimbursementActive;
    }

    @JsonProperty("reimbursementActive")
    public void setReimbursementActive(ReimbursementActive reimbursementActive) {
        this.reimbursementActive = reimbursementActive;
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
        return new ToStringBuilder(this).append("networkID", networkID).append("reimbursementValue", reimbursementValue).append("reimbursementActive", reimbursementActive).append("additionalProperties", additionalProperties).toString();
    }

}
