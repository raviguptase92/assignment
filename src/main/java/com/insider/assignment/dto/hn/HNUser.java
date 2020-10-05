package com.insider.assignment.dto.hn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.insider.assignment.utils.UnixTimeStampDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HNUser {

    @JsonProperty("id")
    private String id;

    @JsonProperty("about")
    private String about;

    @JsonDeserialize(using = UnixTimeStampDeserializer.class)
    @JsonProperty("created")
    private Date created;

    private Integer age;

    public void calculateAge() {
        if(this.created != null) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(created);
                LocalDate createdAt = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                LocalDate now = LocalDate.now();
                Period age = Period.between(createdAt, now);
                this.age = age.getYears();
            } catch (Exception ex) {
                System.out.print("Error occurred while calculating age from : "+ created);
            }
        }
    }
}
