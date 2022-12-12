package com.example.benomad.service.akinator;

import com.example.benomad.dto.PlaceDTO;

import java.util.List;

//coming soon
public interface AkinatorService {
    List<PlaceDTO> getResults(String answers);
}
