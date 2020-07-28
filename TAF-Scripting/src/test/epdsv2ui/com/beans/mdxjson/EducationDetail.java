
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
    "educationTypeCode",
    "schoolName",
    "degree",
    "schoolGraduationYear",
    "startDate",
    "endDate"
})
public class EducationDetail {

    @JsonProperty("educationTypeCode")
    private String educationTypeCode;
    @JsonProperty("schoolName")
    private String schoolName;
    @JsonProperty("degree")
    private String degree;
    @JsonProperty("schoolGraduationYear")
    private String schoolGraduationYear;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("endDate")
    private String endDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("educationTypeCode")
    public String getEducationTypeCode() {
        return educationTypeCode;
    }

    @JsonProperty("educationTypeCode")
    public void setEducationTypeCode(String educationTypeCode) {
        this.educationTypeCode = educationTypeCode;
    }

    @JsonProperty("schoolName")
    public String getSchoolName() {
        return schoolName;
    }

    @JsonProperty("schoolName")
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @JsonProperty("degree")
    public String getDegree() {
        return degree;
    }

    @JsonProperty("degree")
    public void setDegree(String degree) {
        this.degree = degree;
    }

    @JsonProperty("schoolGraduationYear")
    public String getSchoolGraduationYear() {
        return schoolGraduationYear;
    }

    @JsonProperty("schoolGraduationYear")
    public void setSchoolGraduationYear(String schoolGraduationYear) {
        this.schoolGraduationYear = schoolGraduationYear;
    }

    @JsonProperty("startDate")
    public String getStartDate() {
        return startDate;
    }

    @JsonProperty("startDate")
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @JsonProperty("endDate")
    public String getEndDate() {
        return endDate;
    }

    @JsonProperty("endDate")
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
        return new ToStringBuilder(this).append("educationTypeCode", educationTypeCode).append("schoolName", schoolName).append("degree", degree).append("schoolGraduationYear", schoolGraduationYear).append("startDate", startDate).append("endDate", endDate).append("additionalProperties", additionalProperties).toString();
    }

}
