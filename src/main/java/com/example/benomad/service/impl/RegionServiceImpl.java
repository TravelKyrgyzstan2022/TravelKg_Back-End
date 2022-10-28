package com.example.benomad.service.impl;

import com.example.benomad.exception.RegionNotFoundException;
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
            regionDTOS.add(RegionMapper.entityToDto(region));
        }
        return regionDTOS;
    }

    @Override
    public RegionDTO getRegionById(Long id) throws RegionNotFoundException {
        return RegionMapper.entityToDto(regionRepository.findById(id).orElseThrow(RegionNotFoundException::new));
    }

    @Override
    public RegionDTO insertRegion(RegionDTO regionDTO) {
        regionRepository.save(RegionMapper.dtoToEntity(regionDTO));
        return regionDTO;
    }

    @Override
    public RegionDTO deleteRegionById(Long id) throws RegionNotFoundException {
        Region region = regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
        regionRepository.delete(region);
        return RegionMapper.entityToDto(region);
    }

    @Override
    public RegionDTO updateRegionById(Long id, RegionDTO regionDTO) throws RegionNotFoundException {
        regionRepository.findById(id).orElseThrow(RegionNotFoundException::new);
        regionRepository.save(RegionMapper.dtoToEntity(regionDTO));
        return regionDTO;
    }
}
