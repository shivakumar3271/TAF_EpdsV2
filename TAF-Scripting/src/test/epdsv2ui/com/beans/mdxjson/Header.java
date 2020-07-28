
package com.beans.mdxjson;

import java.util.HashMap;
import java.util.List;
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
    "transactionTypes",
    "legacyID",
    "legacySystemID",
    "legacySystemName",
    "spsIDs"
})
public class Header {

    @JsonProperty("transactionTypes")
    private List<String> transactionTypes = null;
    @JsonProperty("legacyID")
    private String legacyID;
    @JsonProperty("legacySystemID")
    private String legacySystemID;
    @JsonProperty("legacySystemName")
    private String legacySystemName;
    @JsonProperty("spsIDs")
    private List<String> spsIDs = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("transactionTypes")
    public List<String> getTransactionTypes() {
        return transactionTypes;
    }

    @JsonProperty("transactionTypes")
    public void setTransactionTypes(List<String> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    @JsonProperty("legacyID")
    public String getLegacyID() {
        return legacyID;
    }

    @JsonProperty("legacyID")
    public void setLegacyID(String legacyID) {
        this.legacyID = legacyID;
    }

    @JsonProperty("legacySystemID")
    public String getLegacySystemID() {
        return legacySystemID;
    }

    @JsonProperty("legacySystemID")
    public void setLegacySystemID(String legacySystemID) {
        this.legacySystemID = legacySystemID;
    }

    @JsonProperty("legacySystemName")
    public String getLegacySystemName() {
        return legacySystemName;
    }

    @JsonProperty("legacySystemName")
    public void setLegacySystemName(String legacySystemName) {
        this.legacySystemName = legacySystemName;
    }

    @JsonProperty("spsIDs")
    public List<String> getSpsIDs() {
        return spsIDs;
    }

    @JsonProperty("spsIDs")
    public void setSpsIDs(List<String> spsIDs) {
        this.spsIDs = spsIDs;
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
        return new ToStringBuilder(this).append("transactionTypes", transactionTypes).append("legacyID", legacyID).append("legacySystemID", legacySystemID).append("legacySystemName", legacySystemName).append("spsIDs", spsIDs).append("additionalProperties", additionalProperties).toString();
    }

}
