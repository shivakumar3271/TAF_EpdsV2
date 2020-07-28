
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
    "credentialingSource",
    "credentialingStatus",
    "credentialingActive"
})
public class CredentialingDetail {

    @JsonProperty("credentialingSource")
    private String credentialingSource;
    @JsonProperty("credentialingStatus")
    private String credentialingStatus;
    @JsonProperty("credentialingActive")
    private CredentialingActive credentialingActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("credentialingSource")
    public String getCredentialingSource() {
        return credentialingSource;
    }

    @JsonProperty("credentialingSource")
    public void setCredentialingSource(String credentialingSource) {
        this.credentialingSource = credentialingSource;
    }

    @JsonProperty("credentialingStatus")
    public String getCredentialingStatus() {
        return credentialingStatus;
    }

    @JsonProperty("credentialingStatus")
    public void setCredentialingStatus(String credentialingStatus) {
        this.credentialingStatus = credentialingStatus;
    }

    @JsonProperty("credentialingActive")
    public CredentialingActive getCredentialingActive() {
        return credentialingActive;
    }

    @JsonProperty("credentialingActive")
    public void setCredentialingActive(CredentialingActive credentialingActive) {
        this.credentialingActive = credentialingActive;
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
        return new ToStringBuilder(this).append("credentialingSource", credentialingSource).append("credentialingStatus", credentialingStatus).append("credentialingActive", credentialingActive).append("additionalProperties", additionalProperties).toString();
    }

}
