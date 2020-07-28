
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
    "header",
    "profile",
    "profileAdditionalDetails",
    "alternateIDs",
    "specialties",
    "educationDetails",
    "addresses",
    "affiliations",
    "claimActionSanctions",
    "legacy"
})
public class RPAJsonTransaction {

    @JsonProperty("header")
    private Header header;
    @JsonProperty("profile")
    private Profile profile;
    @JsonProperty("profileAdditionalDetails")
    private ProfileAdditionalDetails profileAdditionalDetails;
    @JsonProperty("alternateIDs")
    private AlternateIDs alternateIDs;
    @JsonProperty("specialties")
    private List<Specialty> specialties = null;
    @JsonProperty("educationDetails")
    private List<EducationDetail> educationDetails = null;
    @JsonProperty("addresses")
    private List<Address> addresses = null;
    @JsonProperty("affiliations")
    private List<Affiliation> affiliations = null;
    @JsonProperty("claimActionSanctions")
    private List<ClaimActionSanction> claimActionSanctions = null;
    @JsonProperty("legacy")
    private Legacy legacy;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("header")
    public Header getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(Header header) {
        this.header = header;
    }

    @JsonProperty("profile")
    public Profile getProfile() {
        return profile;
    }

    @JsonProperty("profile")
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @JsonProperty("profileAdditionalDetails")
    public ProfileAdditionalDetails getProfileAdditionalDetails() {
        return profileAdditionalDetails;
    }

    @JsonProperty("profileAdditionalDetails")
    public void setProfileAdditionalDetails(ProfileAdditionalDetails profileAdditionalDetails) {
        this.profileAdditionalDetails = profileAdditionalDetails;
    }

    @JsonProperty("alternateIDs")
    public AlternateIDs getAlternateIDs() {
        return alternateIDs;
    }

    @JsonProperty("alternateIDs")
    public void setAlternateIDs(AlternateIDs alternateIDs) {
        this.alternateIDs = alternateIDs;
    }

    @JsonProperty("specialties")
    public List<Specialty> getSpecialties() {
        return specialties;
    }

    @JsonProperty("specialties")
    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }

    @JsonProperty("educationDetails")
    public List<EducationDetail> getEducationDetails() {
        return educationDetails;
    }

    @JsonProperty("educationDetails")
    public void setEducationDetails(List<EducationDetail> educationDetails) {
        this.educationDetails = educationDetails;
    }

    @JsonProperty("addresses")
    public List<Address> getAddresses() {
        return addresses;
    }

    @JsonProperty("addresses")
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @JsonProperty("affiliations")
    public List<Affiliation> getAffiliations() {
        return affiliations;
    }

    @JsonProperty("affiliations")
    public void setAffiliations(List<Affiliation> affiliations) {
        this.affiliations = affiliations;
    }

    @JsonProperty("claimActionSanctions")
    public List<ClaimActionSanction> getClaimActionSanctions() {
        return claimActionSanctions;
    }

    @JsonProperty("claimActionSanctions")
    public void setClaimActionSanctions(List<ClaimActionSanction> claimActionSanctions) {
        this.claimActionSanctions = claimActionSanctions;
    }

    @JsonProperty("legacy")
    public Legacy getLegacy() {
        return legacy;
    }

    @JsonProperty("legacy")
    public void setLegacy(Legacy legacy) {
        this.legacy = legacy;
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
        return new ToStringBuilder(this).append("header", header).append("profile", profile).append("profileAdditionalDetails", profileAdditionalDetails).append("alternateIDs", alternateIDs).append("specialties", specialties).append("educationDetails", educationDetails).append("addresses", addresses).append("affiliations", affiliations).append("claimActionSanctions", claimActionSanctions).append("legacy", legacy).append("additionalProperties", additionalProperties).toString();
    }

}
