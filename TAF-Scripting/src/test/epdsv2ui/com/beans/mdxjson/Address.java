
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
    "addressDetails",
    "addressActive",
    "addressAdditionalDetails"
})
public class Address {

    @JsonProperty("addressDetails")
    private AddressDetails addressDetails;
    @JsonProperty("addressActive")
    private AddressActive addressActive;
    @JsonProperty("addressAdditionalDetails")
    private AddressAdditionalDetails addressAdditionalDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("addressDetails")
    public AddressDetails getAddressDetails() {
        return addressDetails;
    }

    @JsonProperty("addressDetails")
    public void setAddressDetails(AddressDetails addressDetails) {
        this.addressDetails = addressDetails;
    }

    @JsonProperty("addressActive")
    public AddressActive getAddressActive() {
        return addressActive;
    }

    @JsonProperty("addressActive")
    public void setAddressActive(AddressActive addressActive) {
        this.addressActive = addressActive;
    }

    @JsonProperty("addressAdditionalDetails")
    public AddressAdditionalDetails getAddressAdditionalDetails() {
        return addressAdditionalDetails;
    }

    @JsonProperty("addressAdditionalDetails")
    public void setAddressAdditionalDetails(AddressAdditionalDetails addressAdditionalDetails) {
        this.addressAdditionalDetails = addressAdditionalDetails;
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
        return new ToStringBuilder(this).append("addressDetails", addressDetails).append("addressActive", addressActive).append("addressAdditionalDetails", addressAdditionalDetails).append("additionalProperties", additionalProperties).toString();
    }

}
