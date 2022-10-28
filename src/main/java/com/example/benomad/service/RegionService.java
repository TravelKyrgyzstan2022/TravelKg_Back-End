package com.example.benomad.service;

import com.example.benomad.exception.RegionNotFoundException;
import com.example.benomad.dto.RegionDTO;

import java.util.List;

public interface RegionService {
    List<RegionDTO> getAllRegions();
    RegionDTO getRegionById(Long id) throws RegionNotFoundException;
    RegionDTO insertRegion(RegionDTO regionDTO);
    RegionDTO deleteRegionById(Long id) throws RegionNotFoundException;
    RegionDTO updateRegionById(Long id,RegionDTO regionDTO) throws RegionNotFoundException;
}
