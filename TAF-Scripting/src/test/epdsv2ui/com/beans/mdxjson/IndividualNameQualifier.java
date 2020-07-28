
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
    "profileName",
    "aliasNames"
})
public class IndividualNameQualifier {

    @JsonProperty("profileName")
    private ProfileName profileName;
    @JsonProperty("aliasNames")
    private List<AliasName> aliasNames = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("profileName")
    public ProfileName getProfileName() {
        return profileName;
    }

    @JsonProperty("profileName")
    public void setProfileName(ProfileName profileName) {
        this.profileName = profileName;
    }

    @JsonProperty("aliasNames")
    public List<AliasName> getAliasNames() {
        return aliasNames;
    }

    @JsonProperty("aliasNames")
    public void setAliasNames(List<AliasName> aliasNames) {
        this.aliasNames = aliasNames;
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
        return new ToStringBuilder(this).append("profileName", profileName).append("aliasNames", aliasNames).append("additionalProperties", additionalProperties).toString();
    }

}
