
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
    "contactDetails",
    "areasOFocus",
    "officeDetails",
    "officeServiceTypes",
    "schedulingDetails",
    "specialPrograms",
    "remittanceDetails"
})
public class AddressAdditionalDetails {

    @JsonProperty("contactDetails")
    private List<ContactDetail> contactDetails = null;
    @JsonProperty("areasOFocus")
    private List<AreasOFocus> areasOFocus = null;
    @JsonProperty("officeDetails")
    private List<OfficeDetail> officeDetails = null;
    @JsonProperty("officeServiceTypes")
    private List<String> officeServiceTypes = null;
    @JsonProperty("schedulingDetails")
    private List<SchedulingDetail> schedulingDetails = null;
    @JsonProperty("specialPrograms")
    private List<SpecialProgram> specialPrograms = null;
    @JsonProperty("remittanceDetails")
    private List<RemittanceDetail> remittanceDetails = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("contactDetails")
    public List<ContactDetail> getContactDetails() {
        return contactDetails;
    }

    @JsonProperty("contactDetails")
    public void setContactDetails(List<ContactDetail> contactDetails) {
        this.contactDetails = contactDetails;
    }

    @JsonProperty("areasOFocus")
    public List<AreasOFocus> getAreasOFocus() {
        return areasOFocus;
    }

    @JsonProperty("areasOFocus")
    public void setAreasOFocus(List<AreasOFocus> areasOFocus) {
        this.areasOFocus = areasOFocus;
    }

    @JsonProperty("officeDetails")
    public List<OfficeDetail> getOfficeDetails() {
        return officeDetails;
    }

    @JsonProperty("officeDetails")
    public void setOfficeDetails(List<OfficeDetail> officeDetails) {
        this.officeDetails = officeDetails;
    }

    @JsonProperty("officeServiceTypes")
    public List<String> getOfficeServiceTypes() {
        return officeServiceTypes;
    }

    @JsonProperty("officeServiceTypes")
    public void setOfficeServiceTypes(List<String> officeServiceTypes) {
        this.officeServiceTypes = officeServiceTypes;
    }
    
    
    @JsonProperty("schedulingDetails")
    public List<SchedulingDetail> getSchedulingDetails() {
        return schedulingDetails;
    }

    @JsonProperty("schedulingDetails")
    public void setSchedulingDetails(List<SchedulingDetail> schedulingDetails) {
        this.schedulingDetails = schedulingDetails;
    }

    @JsonProperty("specialPrograms")
    public List<SpecialProgram> getSpecialPrograms() {
        return specialPrograms;
    }

    @JsonProperty("specialPrograms")
    public void setSpecialPrograms(List<SpecialProgram> specialPrograms) {
        this.specialPrograms = specialPrograms;
    }

    @JsonProperty("remittanceDetails")
    public List<RemittanceDetail> getRemittanceDetails() {
        return remittanceDetails;
    }

    @JsonProperty("remittanceDetails")
    public void setRemittanceDetails(List<RemittanceDetail> remittanceDetails) {
        this.remittanceDetails = remittanceDetails;
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
        return new ToStringBuilder(this).append("contactDetails", contactDetails).append("areasOFocus", areasOFocus).append("officeDetails", officeDetails).append("schedulingDetails", schedulingDetails).append("specialPrograms", specialPrograms).append("remittanceDetails", remittanceDetails).append("additionalProperties", additionalProperties).toString();
    }

}
