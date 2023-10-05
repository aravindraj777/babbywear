package com.secondskin.babbywear.dto;


import lombok.*;


import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChartDTO {


    private String date;
    private  long orderCount;


    public void setDate(LocalDate localDate) {
        this.date = localDate.toString();
    }
    public LocalDate getDate() {
        return LocalDate.parse(date); // Parse the string back to LocalDate
    }



}
