
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
    "checkName",
    "remittanceNPI",
    "remittanceActive"
})
public class RemittanceDetail {

    @JsonProperty("checkName")
    private String checkName;
    @JsonProperty("remittanceNPI")
    private RemittanceNPI remittanceNPI;
    @JsonProperty("remittanceActive")
    private RemittanceActive remittanceActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("checkName")
    public String getCheckName() {
        return checkName;
    }

    @JsonProperty("checkName")
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    @JsonProperty("remittanceNPI")
    public RemittanceNPI getRemittanceNPI() {
        return remittanceNPI;
    }

    @JsonProperty("remittanceNPI")
    public void setRemittanceNPI(RemittanceNPI remittanceNPI) {
        this.remittanceNPI = remittanceNPI;
    }

    @JsonProperty("remittanceActive")
    public RemittanceActive getRemittanceActive() {
        return remittanceActive;
    }

    @JsonProperty("remittanceActive")
    public void setRemittanceActive(RemittanceActive remittanceActive) {
        this.remittanceActive = remittanceActive;
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
        return new ToStringBuilder(this).append("checkName", checkName).append("remittanceNPI", remittanceNPI).append("remittanceActive", remittanceActive).append("additionalProperties", additionalProperties).toString();
    }

}
