package com.example.benomad.service.impl;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.PlaceTypeDTO;
import com.example.benomad.entity.PlaceType;
import com.example.benomad.mapper.PlaceTypeMapper;
import com.example.benomad.repository.PlaceTypeRepository;
import com.example.benomad.service.PlaceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceTypeServiceImpl implements PlaceTypeService {
    private final PlaceTypeRepository placeTypeRepository;
    @Override
    public List<PlaceTypeDTO> getAllPlaceTypes() {
        List<PlaceType> placeTypes = placeTypeRepository.findAll();
        List<PlaceTypeDTO> placeTypeDTOS = new ArrayList<>();
        for (PlaceType placeType : placeTypes) {
            placeTypeDTOS.add(PlaceTypeMapper.placeTypeToPlaceTypeDto(placeType));
        }
        return placeTypeDTOS;
    }

    @Override
    public PlaceTypeDTO getPlaceTypeById(Long id) throws NotFoundException {
        return PlaceTypeMapper.placeTypeToPlaceTypeDto(placeTypeRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public PlaceTypeDTO insertPlaceType(PlaceTypeDTO placeTypeDTO) {
        placeTypeRepository.save(PlaceTypeMapper.placeTypeDtoToPlaceType(placeTypeDTO));
        return placeTypeDTO;
    }

    @Override
    public PlaceTypeDTO deletePlaceTypeById(Long id) throws NotFoundException {
        PlaceType placeType = placeTypeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeTypeRepository.delete(placeType);
        return PlaceTypeMapper.placeTypeToPlaceTypeDto(placeType);
    }

    @Override
    public PlaceTypeDTO updatePlaceTypeById(Long id, PlaceTypeDTO placeTypeDTO) throws NotFoundException {
        placeTypeRepository.findById(id).orElseThrow(NotFoundException::new);
        placeTypeRepository.save(PlaceTypeMapper.placeTypeDtoToPlaceType(placeTypeDTO));
        return placeTypeDTO;
    }
}
