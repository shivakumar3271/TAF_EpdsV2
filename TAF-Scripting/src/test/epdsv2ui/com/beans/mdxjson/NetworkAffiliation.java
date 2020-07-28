
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
    "networkContractedState",
    "networkSourceSystem",
    "networkID",
    "directoryIndicator",
    "networkActive",
    "patientPreferences",
    "acceptingPatientsInds",
    "timelyFilingInds"
})
public class NetworkAffiliation {

    @JsonProperty("networkContractedState")
    private String networkContractedState;
    @JsonProperty("networkSourceSystem")
    private String networkSourceSystem;
    @JsonProperty("networkID")
    private String networkID;
    @JsonProperty("directoryIndicator")
    private String directoryIndicator;
    @JsonProperty("networkActive")
    private NetworkActive networkActive;
    @JsonProperty("patientPreferences")
    private PatientPreferences patientPreferences;
    @JsonProperty("acceptingPatientsInds")
    private List<AcceptingPatientsInd> acceptingPatientsInds = null;
    @JsonProperty("timelyFilingInds")
    private List<TimelyFilingInd> timelyFilingInds = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("networkContractedState")
    public String getNetworkContractedState() {
        return networkContractedState;
    }

    @JsonProperty("networkContractedState")
    public void setNetworkContractedState(String networkContractedState) {
        this.networkContractedState = networkContractedState;
    }

    @JsonProperty("networkSourceSystem")
    public String getNetworkSourceSystem() {
        return networkSourceSystem;
    }

    @JsonProperty("networkSourceSystem")
    public void setNetworkSourceSystem(String networkSourceSystem) {
        this.networkSourceSystem = networkSourceSystem;
    }

    @JsonProperty("networkID")
    public String getNetworkID() {
        return networkID;
    }

    @JsonProperty("networkID")
    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    @JsonProperty("directoryIndicator")
    public String getDirectoryIndicator() {
        return directoryIndicator;
    }

    @JsonProperty("directoryIndicator")
    public void setDirectoryIndicator(String directoryIndicator) {
        this.directoryIndicator = directoryIndicator;
    }

    @JsonProperty("networkActive")
    public NetworkActive getNetworkActive() {
        return networkActive;
    }

    @JsonProperty("networkActive")
    public void setNetworkActive(NetworkActive networkActive) {
        this.networkActive = networkActive;
    }

    @JsonProperty("patientPreferences")
    public PatientPreferences getPatientPreferences() {
        return patientPreferences;
    }

    @JsonProperty("patientPreferences")
    public void setPatientPreferences(PatientPreferences patientPreferences) {
        this.patientPreferences = patientPreferences;
    }

    @JsonProperty("acceptingPatientsInds")
    public List<AcceptingPatientsInd> getAcceptingPatientsInds() {
        return acceptingPatientsInds;
    }

    @JsonProperty("acceptingPatientsInds")
    public void setAcceptingPatientsInds(List<AcceptingPatientsInd> acceptingPatientsInds) {
        this.acceptingPatientsInds = acceptingPatientsInds;
    }

    @JsonProperty("timelyFilingInds")
    public List<TimelyFilingInd> getTimelyFilingInds() {
        return timelyFilingInds;
    }

    @JsonProperty("timelyFilingInds")
    public void setTimelyFilingInds(List<TimelyFilingInd> timelyFilingInds) {
        this.timelyFilingInds = timelyFilingInds;
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
        return new ToStringBuilder(this).append("networkContractedState", networkContractedState).append("networkSourceSystem", networkSourceSystem).append("networkID", networkID).append("directoryIndicator", directoryIndicator).append("networkActive", networkActive).append("patientPreferences", patientPreferences).append("acceptingPatientsInds", acceptingPatientsInds).append("timelyFilingInds", timelyFilingInds).append("additionalProperties", additionalProperties).toString();
    }

}
