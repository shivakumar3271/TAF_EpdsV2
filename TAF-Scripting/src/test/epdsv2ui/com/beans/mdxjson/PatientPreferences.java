
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
    "ageFrom",
    "ageTo",
    "patientGenderPreference",
    "memberCapacityCount",
    "hospitalBasedNONPARInds"
})
public class PatientPreferences {

    @JsonProperty("ageFrom")
    private String ageFrom;
    @JsonProperty("ageTo")
    private String ageTo;
    @JsonProperty("patientGenderPreference")
    private String patientGenderPreference;
    @JsonProperty("memberCapacityCount")
    private String memberCapacityCount;
    @JsonProperty("hospitalBasedNONPARInds")
    private List<String> hospitalBasedNONPARInds = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ageFrom")
    public String getAgeFrom() {
        return ageFrom;
    }

    @JsonProperty("ageFrom")
    public void setAgeFrom(String ageFrom) {
        this.ageFrom = ageFrom;
    }

    @JsonProperty("ageTo")
    public String getAgeTo() {
        return ageTo;
    }

    @JsonProperty("ageTo")
    public void setAgeTo(String ageTo) {
        this.ageTo = ageTo;
    }

    @JsonProperty("patientGenderPreference")
    public String getPatientGenderPreference() {
        return patientGenderPreference;
    }

    @JsonProperty("patientGenderPreference")
    public void setPatientGenderPreference(String patientGenderPreference) {
        this.patientGenderPreference = patientGenderPreference;
    }

    @JsonProperty("memberCapacityCount")
    public String getMemberCapacityCount() {
        return memberCapacityCount;
    }

    @JsonProperty("memberCapacityCount")
    public void setMemberCapacityCount(String memberCapacityCount) {
        this.memberCapacityCount = memberCapacityCount;
    }

    @JsonProperty("hospitalBasedNONPARInds")
    public List<String> getHospitalBasedNONPARInds() {
        return hospitalBasedNONPARInds;
    }

    @JsonProperty("hospitalBasedNONPARInds")
    public void setHospitalBasedNONPARInds(List<String> hospitalBasedNONPARInds) {
        this.hospitalBasedNONPARInds = hospitalBasedNONPARInds;
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
        return new ToStringBuilder(this).append("ageFrom", ageFrom).append("ageTo", ageTo).append("patientGenderPreference", patientGenderPreference).append("memberCapacityCount", memberCapacityCount).append("hospitalBasedNONPARInds", hospitalBasedNONPARInds).append("additionalProperties", additionalProperties).toString();
    }

}
