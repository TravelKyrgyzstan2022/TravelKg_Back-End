package com.example.benomad.mapper;

import com.example.benomad.dto.RegionDTO;
import com.example.benomad.entity.Region;

public class RegionMapper {
    public static Region dtoToEntity(RegionDTO regionDTO) {
        return Region.builder()
                .id(regionDTO.getId())
                .name(regionDTO.getName())
                .build();
    }

    public static RegionDTO entityToDto(Region region) {
        return RegionDTO.builder()
                .id(region.getId())
                .name(region.getName())
                .build();
    }


}
