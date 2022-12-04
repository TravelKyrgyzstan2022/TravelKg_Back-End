package com.example.benomad.service;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.SupportDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface SupportService {
    List<SupportDTO> getAllSupports();
    SupportDTO getSupportById(Long id) throws ContentNotFoundException;
    SupportDTO insertSupport(Long id);
    SupportDTO deleteSupportById(Long id) throws ContentNotFoundException;
}
