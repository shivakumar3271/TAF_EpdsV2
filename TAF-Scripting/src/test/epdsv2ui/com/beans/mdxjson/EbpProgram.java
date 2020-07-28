
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
    "ebpProgramID",
    "ebpContractStateCode",
    "ebpProgramsActive"
})
public class EbpProgram {

    @JsonProperty("ebpProgramID")
    private String ebpProgramID;
    @JsonProperty("ebpContractStateCode")
    private String ebpContractStateCode;
    @JsonProperty("ebpProgramsActive")
    private EbpProgramsActive ebpProgramsActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ebpProgramID")
    public String getEbpProgramID() {
        return ebpProgramID;
    }

    @JsonProperty("ebpProgramID")
    public void setEbpProgramID(String ebpProgramID) {
        this.ebpProgramID = ebpProgramID;
    }

    @JsonProperty("ebpContractStateCode")
    public String getEbpContractStateCode() {
        return ebpContractStateCode;
    }

    @JsonProperty("ebpContractStateCode")
    public void setEbpContractStateCode(String ebpContractStateCode) {
        this.ebpContractStateCode = ebpContractStateCode;
    }

    @JsonProperty("ebpProgramsActive")
    public EbpProgramsActive getEbpProgramsActive() {
        return ebpProgramsActive;
    }

    @JsonProperty("ebpProgramsActive")
    public void setEbpProgramsActive(EbpProgramsActive ebpProgramsActive) {
        this.ebpProgramsActive = ebpProgramsActive;
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
        return new ToStringBuilder(this).append("ebpProgramID", ebpProgramID).append("ebpContractStateCode", ebpContractStateCode).append("ebpProgramsActive", ebpProgramsActive).append("additionalProperties", additionalProperties).toString();
    }

}
