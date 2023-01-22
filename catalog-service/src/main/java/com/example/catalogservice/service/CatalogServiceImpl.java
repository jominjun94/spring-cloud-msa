package com.example.catalogservice.service;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.jpa.CatalogRepository;
import com.example.catalogservice.vo.ResponseCatalog;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
public class CatalogServiceImpl implements CatalogService{
    CatalogRepository catalogRepository;

    @Autowired
    public CatalogServiceImpl(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Override
    public List<ResponseCatalog> getAllCatalogs() {

        Iterable<CatalogEntity> catalogEntities = catalogRepository.findAll();
        List<ResponseCatalog> result = new ArrayList<>();
        catalogEntities.forEach(v->{
            result.add(new ModelMapper().map(v,ResponseCatalog.class));
        });

        return result;
    }
}
