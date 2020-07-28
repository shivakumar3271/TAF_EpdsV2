
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
    "effectiveDate",
    "terminationDate",
    "terminationReasonCode"
})
public class BundledPaymentProcessActive {

    @JsonProperty("effectiveDate")
    private String effectiveDate;
    @JsonProperty("terminationDate")
    private String terminationDate;
    @JsonProperty("terminationReasonCode")
    private String terminationReasonCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("effectiveDate")
    public String getEffectiveDate() {
        return effectiveDate;
    }

    @JsonProperty("effectiveDate")
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @JsonProperty("terminationDate")
    public String getTerminationDate() {
        return terminationDate;
    }

    @JsonProperty("terminationDate")
    public void setTerminationDate(String terminationDate) {
        this.terminationDate = terminationDate;
    }

    @JsonProperty("terminationReasonCode")
    public String getTerminationReasonCode() {
        return terminationReasonCode;
    }

    @JsonProperty("terminationReasonCode")
    public void setTerminationReasonCode(String terminationReasonCode) {
        this.terminationReasonCode = terminationReasonCode;
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
        return new ToStringBuilder(this).append("effectiveDate", effectiveDate).append("terminationDate", terminationDate).append("terminationReasonCode", terminationReasonCode).append("additionalProperties", additionalProperties).toString();
    }

}
