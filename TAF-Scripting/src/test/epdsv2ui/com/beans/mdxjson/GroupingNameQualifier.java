
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
    "groupingType",
    "groupingName",
    "groupingSiteCode"
})
public class GroupingNameQualifier {

    @JsonProperty("groupingType")
    private String groupingType;
    @JsonProperty("groupingName")
    private String groupingName;
    @JsonProperty("groupingSiteCode")
    private String groupingSiteCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("groupingType")
    public String getGroupingType() {
        return groupingType;
    }

    @JsonProperty("groupingType")
    public void setGroupingType(String groupingType) {
        this.groupingType = groupingType;
    }

    @JsonProperty("groupingName")
    public String getGroupingName() {
        return groupingName;
    }

    @JsonProperty("groupingName")
    public void setGroupingName(String groupingName) {
        this.groupingName = groupingName;
    }

    @JsonProperty("groupingSiteCode")
    public String getGroupingSiteCode() {
        return groupingSiteCode;
    }

    @JsonProperty("groupingSiteCode")
    public void setGroupingSiteCode(String groupingSiteCode) {
        this.groupingSiteCode = groupingSiteCode;
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
        return new ToStringBuilder(this).append("groupingType", groupingType).append("groupingName", groupingName).append("groupingSiteCode", groupingSiteCode).append("additionalProperties", additionalProperties).toString();
    }

}
