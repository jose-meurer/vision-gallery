package com.josemeurer.visiongallery.service;

import com.azure.storage.blob.BlobServiceClient;
import com.josemeurer.visiongallery.dto.ImageMetadataResponseDTO;
import com.josemeurer.visiongallery.dto.ImageUploadResponseDTO;
import com.josemeurer.visiongallery.entity.ImageMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.josemeurer.visiongallery.repository.ImageMetadataRepository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static org.apache.commons.lang3.BooleanUtils.forEach;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final BlobServiceClient blobServiceClient;
    private final ImageMetadataRepository imageMetadataRepository;

    @Value("${azure.storage.container.name:images}")
    private String containerName;

    public ImageUploadResponseDTO uploadImage(MultipartFile file) throws IOException {
        if(file.isEmpty())
            throw new IllegalArgumentException("O arquivo enviado está vazio.");

        var uniqueFileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        var containerClient = blobServiceClient.getBlobContainerClient(containerName);
        if(!containerClient.exists())
            containerClient.create();

        var blobClient = containerClient.getBlobClient(uniqueFileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        var blobUrl = blobClient.getBlobUrl();

        var metadata = new ImageMetadata();
        metadata.setOriginalFilename(file.getOriginalFilename());
        metadata.setBlobUrl(blobUrl);
        metadata.setSizeInBytes(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.setUploadDate(java.time.LocalDateTime.now());

        var savedMetadata = imageMetadataRepository.save(metadata);
        return ImageUploadResponseDTO.builder()
                .id(savedMetadata.getId())
                .fileName(savedMetadata.getOriginalFilename())
                .url(blobUrl)
                .message("Upload realizado com sucesso!")
                .build();
    }

    public List<ImageMetadataResponseDTO> getAllImages(){
        var imagesIterable = imageMetadataRepository.findAll();

        return (StreamSupport.stream(imagesIterable.spliterator(), true)
                .map(e -> ImageMetadataResponseDTO.builder()
                        .id(e.getId())
                        .filename(e.getOriginalFilename())
                        .url(e.getBlobUrl())
                        .contentType(e.getContentType())
                        .sizeInBytes(e.getSizeInBytes())
                        .uploadDate(e.getUploadDate())
                        .build()
                ).toList());
    }
}
