
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
    "accessibilityCode",
    "staffLanguage",
    "acceptingPatients",
    "officeTechnology",
    "officeRestriction",
    "officeSystemName",
    "officeServiceCertificationCode"
})
public class OfficeDetail {

    @JsonProperty("accessibilityCode")
    private List<String> accessibilityCode;
    @JsonProperty("staffLanguage")
    private List<String> staffLanguage;
    @JsonProperty("acceptingPatients")
    private String acceptingPatients;
    @JsonProperty("officeTechnology")
    private List<String> officeTechnology;
    @JsonProperty("officeRestriction")
    private List<String> officeRestriction;
    @JsonProperty("officeSystemName")
    private List<String> officeSystemName;
    @JsonProperty("officeServiceCertificationCode")
    private List<String> officeServiceCertificationCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accessibilityCode")
    public List<String> getAccessibilityCode() {
        return accessibilityCode;
    }

    @JsonProperty("accessibilityCode")
    public void setAccessibilityCode(List<String> accessibilityCode) {
        this.accessibilityCode = accessibilityCode;
    }

    @JsonProperty("staffLanguage")
    public List<String> getStaffLanguage() {
        return staffLanguage;
    }

    @JsonProperty("staffLanguage")
    public void setStaffLanguage(List<String> staffLanguage) {
        this.staffLanguage = staffLanguage;
    }

    @JsonProperty("acceptingPatients")
    public String getAcceptingPatients() {
        return acceptingPatients;
    }

    @JsonProperty("acceptingPatients")
    public void setAcceptingPatients(String acceptingPatients) {
        this.acceptingPatients = acceptingPatients;
    }

    @JsonProperty("officeTechnology")
    public List<String> getOfficeTechnology() {
        return officeTechnology;
    }

    @JsonProperty("officeTechnology")
    public void setOfficeTechnology(List<String> officeTechnology) {
        this.officeTechnology = officeTechnology;
    }

    @JsonProperty("officeRestriction")
    public List<String> getOfficeRestriction() {
        return officeRestriction;
    }

    @JsonProperty("officeRestriction")
    public void setOfficeRestriction(List<String> officeRestriction) {
        this.officeRestriction = officeRestriction;
    }

    @JsonProperty("officeSystemName")
    public List<String> getOfficeSystemName() {
        return officeSystemName;
    }

    @JsonProperty("officeSystemName")
    public void setOfficeSystemName(List<String> officeSystemName) {
        this.officeSystemName = officeSystemName;
    }

    @JsonProperty("officeServiceCertificationCode")
    public List<String> getOfficeServiceCertificationCode() {
        return officeServiceCertificationCode;
    }

    @JsonProperty("officeServiceCertificationCode")
    public void setOfficeServiceCertificationCode(List<String> officeServiceCertificationCode) {
        this.officeServiceCertificationCode = officeServiceCertificationCode;
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
        return new ToStringBuilder(this).append("accessibilityCode", accessibilityCode).append("staffLanguage", staffLanguage).append("acceptingPatients", acceptingPatients).append("officeTechnology", officeTechnology).append("officeRestriction", officeRestriction).append("officeSystemName", officeSystemName).append("officeServiceCertificationCode", officeServiceCertificationCode).append("additionalProperties", additionalProperties).toString();
    }

}
