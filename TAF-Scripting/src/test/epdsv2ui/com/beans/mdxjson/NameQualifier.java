
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
    "groupingNameQualifier",
    "individualNameQualifier",
    "orgNameQualifier"
})
public class NameQualifier {

    @JsonProperty("groupingNameQualifier")
    private GroupingNameQualifier groupingNameQualifier;
    @JsonProperty("individualNameQualifier")
    private IndividualNameQualifier individualNameQualifier;
    @JsonProperty("orgNameQualifier")
    private OrgNameQualifier orgNameQualifier;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("groupingNameQualifier")
    public GroupingNameQualifier getGroupingNameQualifier() {
        return groupingNameQualifier;
    }

    @JsonProperty("groupingNameQualifier")
    public void setGroupingNameQualifier(GroupingNameQualifier groupingNameQualifier) {
        this.groupingNameQualifier = groupingNameQualifier;
    }

    @JsonProperty("individualNameQualifier")
    public IndividualNameQualifier getIndividualNameQualifier() {
        return individualNameQualifier;
    }

    @JsonProperty("individualNameQualifier")
    public void setIndividualNameQualifier(IndividualNameQualifier individualNameQualifier) {
        this.individualNameQualifier = individualNameQualifier;
    }

    @JsonProperty("orgNameQualifier")
    public OrgNameQualifier getOrgNameQualifier() {
        return orgNameQualifier;
    }

    @JsonProperty("orgNameQualifier")
    public void setOrgNameQualifier(OrgNameQualifier orgNameQualifier) {
        this.orgNameQualifier = orgNameQualifier;
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
        return new ToStringBuilder(this).append("groupingNameQualifier", groupingNameQualifier).append("individualNameQualifier", individualNameQualifier).append("orgNameQualifier", orgNameQualifier).append("additionalProperties", additionalProperties).toString();
    }

}
