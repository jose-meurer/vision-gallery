package com.josemeurer.visiongallery.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.josemeurer.visiongallery.entity.ImageMetadata;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMetadataRepository extends CosmosRepository<ImageMetadata, String> {
}
