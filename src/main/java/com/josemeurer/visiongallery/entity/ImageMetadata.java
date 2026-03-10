package com.josemeurer.visiongallery.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Container(containerName = "images", ru = "400")
public class ImageMetadata {

    @Id
    @GeneratedValue
    private String id;

    @PartitionKey
    private String originalFilename;

    private String blobUrl;
    private long sizeInBytes;
    private String contentType;
    private LocalDateTime uploadDate;
}
