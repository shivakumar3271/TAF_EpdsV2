
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
    "pdmIndicatorDetails",
    "credentialingDetails",
    "providerDistinctionDetails",
    "ebpPrograms",
    "bundledPaymentProcesses"
})
public class ProfileAdditionalDetails {

    @JsonProperty("pdmIndicatorDetails")
    private List<PdmIndicatorDetail> pdmIndicatorDetails = null;
    @JsonProperty("credentialingDetails")
    private List<CredentialingDetail> credentialingDetails = null;
    @JsonProperty("providerDistinctionDetails")
    private List<ProviderDistinctionDetail> providerDistinctionDetails = null;
    @JsonProperty("ebpPrograms")
    private List<EbpProgram> ebpPrograms = null;
    @JsonProperty("bundledPaymentProcesses")
    private List<BundledPaymentProcess> bundledPaymentProcesses = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pdmIndicatorDetails")
    public List<PdmIndicatorDetail> getPdmIndicatorDetails() {
        return pdmIndicatorDetails;
    }

    @JsonProperty("pdmIndicatorDetails")
    public void setPdmIndicatorDetails(List<PdmIndicatorDetail> pdmIndicatorDetails) {
        this.pdmIndicatorDetails = pdmIndicatorDetails;
    }

    @JsonProperty("credentialingDetails")
    public List<CredentialingDetail> getCredentialingDetails() {
        return credentialingDetails;
    }

    @JsonProperty("credentialingDetails")
    public void setCredentialingDetails(List<CredentialingDetail> credentialingDetails) {
        this.credentialingDetails = credentialingDetails;
    }

    @JsonProperty("providerDistinctionDetails")
    public List<ProviderDistinctionDetail> getProviderDistinctionDetails() {
        return providerDistinctionDetails;
    }

    @JsonProperty("providerDistinctionDetails")
    public void setProviderDistinctionDetails(List<ProviderDistinctionDetail> providerDistinctionDetails) {
        this.providerDistinctionDetails = providerDistinctionDetails;
    }

    @JsonProperty("ebpPrograms")
    public List<EbpProgram> getEbpPrograms() {
        return ebpPrograms;
    }

    @JsonProperty("ebpPrograms")
    public void setEbpPrograms(List<EbpProgram> ebpPrograms) {
        this.ebpPrograms = ebpPrograms;
    }

    @JsonProperty("bundledPaymentProcesses")
    public List<BundledPaymentProcess> getBundledPaymentProcesses() {
        return bundledPaymentProcesses;
    }

    @JsonProperty("bundledPaymentProcesses")
    public void setBundledPaymentProcesses(List<BundledPaymentProcess> bundledPaymentProcesses) {
        this.bundledPaymentProcesses = bundledPaymentProcesses;
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
        return new ToStringBuilder(this).append("pdmIndicatorDetails", pdmIndicatorDetails).append("credentialingDetails", credentialingDetails).append("providerDistinctionDetails", providerDistinctionDetails).append("ebpPrograms", ebpPrograms).append("bundledPaymentProcesses", bundledPaymentProcesses).append("additionalProperties", additionalProperties).toString();
    }

}
