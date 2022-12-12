package com.example.benomad.util;

import com.example.benomad.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    public static void validate(String dateString, DateTimeFormatter formatter){
        try {
            LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getMessage());
        }
    }

    public static void validateDateTime(String dateString, DateTimeFormatter formatter){
        try {
            LocalDateTime.parse(dateString, formatter);
        } catch (DateTimeParseException e){
            throw new InvalidDateException(e.getMessage());
        }
    }

    public static LocalDate parseDate(String dateString, DateTimeFormatter formatter){
        validate(dateString, formatter);
        return LocalDate.parse(dateString, formatter);
    }

    public static LocalDateTime parseDateTime(String dateString, DateTimeFormatter formatter){
        validateDateTime(dateString, formatter);
        return LocalDateTime.parse(dateString, formatter);
    }

    public static String integerToDateString(Integer n){
        if(n > 9){
            return "" + n;
        }else{
            return "0" + n;
        }
    }
}
