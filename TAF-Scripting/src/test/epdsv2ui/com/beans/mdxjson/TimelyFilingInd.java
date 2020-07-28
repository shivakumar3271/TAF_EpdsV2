
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
    "timelyFiling",
    "timelyFilingActive"
})
public class TimelyFilingInd {

    @JsonProperty("timelyFiling")
    private String timelyFiling;
    @JsonProperty("timelyFilingActive")
    private TimelyFilingActive timelyFilingActive;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("timelyFiling")
    public String getTimelyFiling() {
        return timelyFiling;
    }

    @JsonProperty("timelyFiling")
    public void setTimelyFiling(String timelyFiling) {
        this.timelyFiling = timelyFiling;
    }

    @JsonProperty("timelyFilingActive")
    public TimelyFilingActive getTimelyFilingActive() {
        return timelyFilingActive;
    }

    @JsonProperty("timelyFilingActive")
    public void setTimelyFilingActive(TimelyFilingActive timelyFilingActive) {
        this.timelyFilingActive = timelyFilingActive;
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
        return new ToStringBuilder(this).append("timelyFiling", timelyFiling).append("timelyFilingActive", timelyFilingActive).append("additionalProperties", additionalProperties).toString();
    }

}
