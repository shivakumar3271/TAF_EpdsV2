
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
    "areaOfFocus",
    "areaOfFocusActive"
})
public class AreasOFocus {

    @JsonProperty("areaOfFocus")
    private String areaOfFocus;
    @JsonProperty("areaOfFocusActive")
    private AreaOfFocusActive areaOfFocusActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("areaOfFocus")
    public String getAreaOfFocus() {
        return areaOfFocus;
    }

    @JsonProperty("areaOfFocus")
    public void setAreaOfFocus(String areaOfFocus) {
        this.areaOfFocus = areaOfFocus;
    }

    @JsonProperty("areaOfFocusActive")
    public AreaOfFocusActive getAreaOfFocusActive() {
        return areaOfFocusActive;
    }

    @JsonProperty("areaOfFocusActive")
    public void setAreaOfFocusActive(AreaOfFocusActive areaOfFocusActive) {
        this.areaOfFocusActive = areaOfFocusActive;
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
        return new ToStringBuilder(this).append("areaOfFocus", areaOfFocus).append("areaOfFocusActive", areaOfFocusActive).append("additionalProperties", additionalProperties).toString();
    }

}
