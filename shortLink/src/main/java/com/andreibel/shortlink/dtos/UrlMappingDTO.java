package com.andreibel.shortlink.dtos;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for URL mapping information.
 * <p>
 * Encapsulates details about a shortened URL, including its original URL,
 * the generated short URL, click count, creation date, and the username of the owner.
 */
@Data
public class UrlMappingDTO {
    private Long id;
    private String originalUrl;
    private String shortUrl;
    private int clickCount;
    private LocalDateTime createdDate;
    private String username;
}
