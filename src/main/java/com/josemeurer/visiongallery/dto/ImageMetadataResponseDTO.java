package com.josemeurer.visiongallery.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ImageMetadataResponseDTO(String id,
                                       String filename,
                                       String url,
                                       long sizeInBytes,
                                       String contentType,
                                       LocalDateTime uploadDate) {
}
