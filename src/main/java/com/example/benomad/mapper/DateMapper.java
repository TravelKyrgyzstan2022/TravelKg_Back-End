package com.example.benomad.mapper;

import com.example.benomad.dto.DateDTO;
import com.example.benomad.entity.Plan;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static List<DateDTO> plansToDates(List<Plan> plans){
        List<DateDTO> dates = new ArrayList<>();
        for (Plan p : plans){
            dates.add(new DateDTO(formatter.format(p.getDate())));
        }
        return dates;
    }
}
