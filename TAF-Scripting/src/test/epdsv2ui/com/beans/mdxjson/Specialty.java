
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
    "billingform",
    "primarySpecialtyIndicator",
    "specialtyCode",
    "boardCertificationAgencyName",
    "boardCertificationLifetimeCertificationIndicator",
    "boardCertificationRecertificationDate",
    "boardCertificationDate",
    "boardCertificationExpirationDate",
    "addresses",
    "networkAffiliations",
    "specialtyActive"
})
public class Specialty {

    @JsonProperty("billingform")
    private String billingform;
    @JsonProperty("primarySpecialtyIndicator")
    private Boolean primarySpecialtyIndicator;
    @JsonProperty("specialtyCode")
    private String specialtyCode;
    @JsonProperty("boardCertificationAgencyName")
    private String boardCertificationAgencyName;
    @JsonProperty("boardCertificationLifetimeCertificationIndicator")
    private String boardCertificationLifetimeCertificationIndicator;
    @JsonProperty("boardCertificationRecertificationDate")
    private String boardCertificationRecertificationDate;
    @JsonProperty("boardCertificationDate")
    private String boardCertificationDate;
    @JsonProperty("boardCertificationExpirationDate")
    private String boardCertificationExpirationDate;
    @JsonProperty("addresses")
    private List<Address> addresses = null;
    @JsonProperty("networkAffiliations")
    private List<NetworkAffiliation> networkAffiliations = null;
    @JsonProperty("specialtyActive")
    private SpecialtyActive specialtyActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("billingform")
    public String getBillingform() {
        return billingform;
    }

    @JsonProperty("billingform")
    public void setBillingform(String billingform) {
        this.billingform = billingform;
    }

    @JsonProperty("primarySpecialtyIndicator")
    public Boolean getPrimarySpecialtyIndicator() {
        return primarySpecialtyIndicator;
    }

    @JsonProperty("primarySpecialtyIndicator")
    public void setPrimarySpecialtyIndicator(Boolean primarySpecialtyIndicator) {
        this.primarySpecialtyIndicator = primarySpecialtyIndicator;
    }

    @JsonProperty("specialtyCode")
    public String getSpecialtyCode() {
        return specialtyCode;
    }

    @JsonProperty("specialtyCode")
    public void setSpecialtyCode(String specialtyCode) {
        this.specialtyCode = specialtyCode;
    }

    @JsonProperty("boardCertificationAgencyName")
    public String getBoardCertificationAgencyName() {
        return boardCertificationAgencyName;
    }

    @JsonProperty("boardCertificationAgencyName")
    public void setBoardCertificationAgencyName(String boardCertificationAgencyName) {
        this.boardCertificationAgencyName = boardCertificationAgencyName;
    }

    @JsonProperty("boardCertificationLifetimeCertificationIndicator")
    public String getBoardCertificationLifetimeCertificationIndicator() {
        return boardCertificationLifetimeCertificationIndicator;
    }

    @JsonProperty("boardCertificationLifetimeCertificationIndicator")
    public void setBoardCertificationLifetimeCertificationIndicator(String boardCertificationLifetimeCertificationIndicator) {
        this.boardCertificationLifetimeCertificationIndicator = boardCertificationLifetimeCertificationIndicator;
    }

    @JsonProperty("boardCertificationRecertificationDate")
    public String getBoardCertificationRecertificationDate() {
        return boardCertificationRecertificationDate;
    }

    @JsonProperty("boardCertificationRecertificationDate")
    public void setBoardCertificationRecertificationDate(String boardCertificationRecertificationDate) {
        this.boardCertificationRecertificationDate = boardCertificationRecertificationDate;
    }

    @JsonProperty("boardCertificationDate")
    public String getBoardCertificationDate() {
        return boardCertificationDate;
    }

    @JsonProperty("boardCertificationDate")
    public void setBoardCertificationDate(String boardCertificationDate) {
        this.boardCertificationDate = boardCertificationDate;
    }

    @JsonProperty("boardCertificationExpirationDate")
    public String getBoardCertificationExpirationDate() {
        return boardCertificationExpirationDate;
    }

    @JsonProperty("boardCertificationExpirationDate")
    public void setBoardCertificationExpirationDate(String boardCertificationExpirationDate) {
        this.boardCertificationExpirationDate = boardCertificationExpirationDate;
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

    @JsonProperty("specialtyActive")
    public SpecialtyActive getSpecialtyActive() {
        return specialtyActive;
    }

    @JsonProperty("specialtyActive")
    public void setSpecialtyActive(SpecialtyActive specialtyActive) {
        this.specialtyActive = specialtyActive;
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
        return new ToStringBuilder(this).append("billingform", billingform).append("primarySpecialtyIndicator", primarySpecialtyIndicator).append("specialtyCode", specialtyCode).append("boardCertificationAgencyName", boardCertificationAgencyName).append("boardCertificationLifetimeCertificationIndicator", boardCertificationLifetimeCertificationIndicator).append("boardCertificationRecertificationDate", boardCertificationRecertificationDate).append("boardCertificationDate", boardCertificationDate).append("boardCertificationExpirationDate", boardCertificationExpirationDate).append("addresses", addresses).append("networkAffiliations", networkAffiliations).append("specialtyActive", specialtyActive).append("additionalProperties", additionalProperties).toString();
    }

}
