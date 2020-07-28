
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
    "days",
    "openTime",
    "closeTime"
})
public class SchedulingDetail {

    @JsonProperty("days")
    private String days;
    @JsonProperty("openTime")
    private String openTime;
    @JsonProperty("closeTime")
    private String closeTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("days")
    public String getDays() {
        return days;
    }

    @JsonProperty("days")
    public void setDays(String days) {
        this.days = days;
    }

    @JsonProperty("openTime")
    public String getOpenTime() {
        return openTime;
    }

    @JsonProperty("openTime")
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("closeTime")
    public String getCloseTime() {
        return closeTime;
    }

    @JsonProperty("closeTime")
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
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
        return new ToStringBuilder(this).append("days", days).append("openTime", openTime).append("closeTime", closeTime).append("additionalProperties", additionalProperties).toString();
    }

}
