package com.example.benomad.service;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.RegionDTO;

import java.util.List;

public interface RegionService {
    List<RegionDTO> getAllRegions();
    RegionDTO getRegionById(Long id) throws NotFoundException;
    RegionDTO insertRegion(RegionDTO regionDTO);
    RegionDTO deleteRegionById(Long id) throws NotFoundException;
    RegionDTO updateRegionById(Long id,RegionDTO regionDTO) throws NotFoundException;
}
