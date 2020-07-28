
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
    "affiliatedLegacyID",
    "affiliationType",
    "groupingCode",
    "groupingType",
    "pcpID",
    "affiliationActive",
    "npiDetails",
    "affliatedTaxIDs",
    "addresses",
    "networkAffiliations",
    "reimbursements"
})
public class Affiliation {

    @JsonProperty("affiliatedLegacyID")
    private String affiliatedLegacyID;
    @JsonProperty("affiliationType")
    private String affiliationType;
    @JsonProperty("groupingCode")
    private String groupingCode;
    @JsonProperty("groupingType")
    private String groupingType;
    @JsonProperty("pcpID")
    private String pcpID;
    @JsonProperty("affiliationActive")
    private AffiliationActive affiliationActive;
    @JsonProperty("npiDetails")
    private List<NpiDetail> npiDetails = null;
    @JsonProperty("affliatedTaxIDs")
    private List<AffliatedTaxID> affliatedTaxIDs = null;
    @JsonProperty("addresses")
    private List<Address> addresses = null;
    @JsonProperty("networkAffiliations")
    private List<NetworkAffiliation> networkAffiliations = null;
    @JsonProperty("reimbursements")
    private List<Reimbursement> reimbursements = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("affiliatedLegacyID")
    public String getAffiliatedLegacyID() {
        return affiliatedLegacyID;
    }

    @JsonProperty("affiliatedLegacyID")
    public void setAffiliatedLegacyID(String affiliatedLegacyID) {
        this.affiliatedLegacyID = affiliatedLegacyID;
    }

    @JsonProperty("affiliationType")
    public String getAffiliationType() {
        return affiliationType;
    }

    @JsonProperty("affiliationType")
    public void setAffiliationType(String affiliationType) {
        this.affiliationType = affiliationType;
    }

    @JsonProperty("groupingCode")
    public String getGroupingCode() {
        return groupingCode;
    }

    @JsonProperty("groupingCode")
    public void setGroupingCode(String groupingCode) {
        this.groupingCode = groupingCode;
    }

    @JsonProperty("groupingType")
    public String getGroupingType() {
        return groupingType;
    }

    @JsonProperty("groupingType")
    public void setGroupingType(String groupingType) {
        this.groupingType = groupingType;
    }

    @JsonProperty("pcpID")
    public String getPcpID() {
        return pcpID;
    }

    @JsonProperty("pcpID")
    public void setPcpID(String pcpID) {
        this.pcpID = pcpID;
    }

    @JsonProperty("affiliationActive")
    public AffiliationActive getAffiliationActive() {
        return affiliationActive;
    }

    @JsonProperty("affiliationActive")
    public void setAffiliationActive(AffiliationActive affiliationActive) {
        this.affiliationActive = affiliationActive;
    }

    @JsonProperty("npiDetails")
    public List<NpiDetail> getNpiDetails() {
        return npiDetails;
    }

    @JsonProperty("npiDetails")
    public void setNpiDetails(List<NpiDetail> npiDetails) {
        this.npiDetails = npiDetails;
    }

    @JsonProperty("affliatedTaxIDs")
    public List<AffliatedTaxID> getAffliatedTaxIDs() {
        return affliatedTaxIDs;
    }

    @JsonProperty("affliatedTaxIDs")
    public void setAffliatedTaxIDs(List<AffliatedTaxID> affliatedTaxIDs) {
        this.affliatedTaxIDs = affliatedTaxIDs;
    }

    @JsonProperty("addresses")
    public List<Address> getAddresses() {
        return addresses;
    }

    @JsonProperty("addresses")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @JsonProperty("networkAffiliations")
    public List<NetworkAffiliation> getNetworkAffiliations() {
        return networkAffiliations;
    }

    @JsonProperty("networkAffiliations")
    public void setNetworkAffiliations(List<NetworkAffiliation> networkAffiliations) {
        this.networkAffiliations = networkAffiliations;
    }

    @JsonProperty("reimbursements")
    public List<Reimbursement> getReimbursements() {
        return reimbursements;
    }

    @JsonProperty("reimbursements")
    public void setReimbursements(List<Reimbursement> reimbursements) {
        this.reimbursements = reimbursements;
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
        return new ToStringBuilder(this).append("affiliatedLegacyID", affiliatedLegacyID).append("affiliationType", affiliationType).append("groupingCode", groupingCode).append("groupingType", groupingType).append("pcpID", pcpID).append("affiliationActive", affiliationActive).append("npiDetails", npiDetails).append("affliatedTaxIDs", affliatedTaxIDs).append("addresses", addresses).append("networkAffiliations", networkAffiliations).append("reimbursements", reimbursements).append("additionalProperties", additionalProperties).toString();
    }

}
