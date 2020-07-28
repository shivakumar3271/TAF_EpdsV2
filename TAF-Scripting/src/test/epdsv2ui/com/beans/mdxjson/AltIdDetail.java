
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
    "altIDSource",
    "altIDType",
    "altIDValue",
    "alternateIDActive"
})
public class AltIdDetail {

    @JsonProperty("altIDSource")
    private String altIDSource;
    @JsonProperty("altIDType")
    private String altIDType;
    @JsonProperty("altIDValue")
    private String altIDValue;
    @JsonProperty("alternateIDActive")
    private AlternateIDActive alternateIDActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("altIDSource")
    public String getAltIDSource() {
        return altIDSource;
    }

    @JsonProperty("altIDSource")
    public void setAltIDSource(String altIDSource) {
        this.altIDSource = altIDSource;
    }

    @JsonProperty("altIDType")
    public String getAltIDType() {
        return altIDType;
    }

    @JsonProperty("altIDType")
    public void setAltIDType(String altIDType) {
        this.altIDType = altIDType;
    }

    @JsonProperty("altIDValue")
    public String getAltIDValue() {
        return altIDValue;
    }

    @JsonProperty("altIDValue")
    public void setAltIDValue(String altIDValue) {
        this.altIDValue = altIDValue;
    }

    @JsonProperty("alternateIDActive")
    public AlternateIDActive getAlternateIDActive() {
        return alternateIDActive;
    }

    @JsonProperty("alternateIDActive")
    public void setAlternateIDActive(AlternateIDActive alternateIDActive) {
        this.alternateIDActive = alternateIDActive;
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
        return new ToStringBuilder(this).append("altIDSource", altIDSource).append("altIDType", altIDType).append("altIDValue", altIDValue).append("alternateIDActive", alternateIDActive).append("additionalProperties", additionalProperties).toString();
    }

}
