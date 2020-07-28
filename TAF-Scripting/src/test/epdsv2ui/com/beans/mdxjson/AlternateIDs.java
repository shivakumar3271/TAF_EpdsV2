
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
    "npiDetails",
    "altIdDetails"
})
public class AlternateIDs {

    @JsonProperty("npiDetails")
    private List<NpiDetail> npiDetails = null;
    @JsonProperty("altIdDetails")
    private List<AltIdDetail> altIdDetails = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("npiDetails")
    public List<NpiDetail> getNpiDetails() {
        return npiDetails;
    }

    @JsonProperty("npiDetails")
    public void setNpiDetails(List<NpiDetail> npiDetails) {
        this.npiDetails = npiDetails;
    }

    @JsonProperty("altIdDetails")
    public List<AltIdDetail> getAltIdDetails() {
        return altIdDetails;
    }

    @JsonProperty("altIdDetails")
    public void setAltIdDetails(List<AltIdDetail> altIdDetails) {
        this.altIdDetails = altIdDetails;
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
        return new ToStringBuilder(this).append("npiDetails", npiDetails).append("altIdDetails", altIdDetails).append("additionalProperties", additionalProperties).toString();
    }

}
