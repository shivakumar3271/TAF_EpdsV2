
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
    "providerSpecialProgramType",
    "programActive"
})
public class SpecialProgram {

    @JsonProperty("providerSpecialProgramType")
    private String providerSpecialProgramType;
    @JsonProperty("programActive")
    private ProgramActive programActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("providerSpecialProgramType")
    public String getProviderSpecialProgramType() {
        return providerSpecialProgramType;
    }

    @JsonProperty("providerSpecialProgramType")
    public void setProviderSpecialProgramType(String providerSpecialProgramType) {
        this.providerSpecialProgramType = providerSpecialProgramType;
    }

    @JsonProperty("programActive")
    public ProgramActive getProgramActive() {
        return programActive;
    }

    @JsonProperty("programActive")
    public void setProgramActive(ProgramActive programActive) {
        this.programActive = programActive;
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
        return new ToStringBuilder(this).append("providerSpecialProgramType", providerSpecialProgramType).append("programActive", programActive).append("additionalProperties", additionalProperties).toString();
    }

}
