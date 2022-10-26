package com.example.benomad.service.impl;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.RegionDTO;
import com.example.benomad.entity.Region;
import com.example.benomad.mapper.RegionMapper;
import com.example.benomad.repository.RegionRepository;
import com.example.benomad.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRepository regionRepository;
    @Override
    public List<RegionDTO> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        List<RegionDTO> regionDTOS = new ArrayList<>();
        for (Region region : regions) {
            regionDTOS.add(RegionMapper.regionToRegionDto(region));
        }
        return regionDTOS;
    }

    @Override
    public RegionDTO getRegionById(Long id) throws NotFoundException {
        return RegionMapper.regionToRegionDto(regionRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public RegionDTO insertRegion(RegionDTO regionDTO) {
        regionRepository.save(RegionMapper.regionDtoToRegion(regionDTO));
        return regionDTO;
    }

    @Override
    public RegionDTO deleteRegionById(Long id) throws NotFoundException {
        Region region = regionRepository.findById(id).orElseThrow(NotFoundException::new);
        regionRepository.delete(region);
        return RegionMapper.regionToRegionDto(region);
    }

    @Override
    public RegionDTO updateRegionById(Long id, RegionDTO regionDTO) throws NotFoundException {
        regionRepository.findById(id).orElseThrow(NotFoundException::new);
        regionRepository.save(RegionMapper.regionDtoToRegion(regionDTO));
        return regionDTO;
    }
}
