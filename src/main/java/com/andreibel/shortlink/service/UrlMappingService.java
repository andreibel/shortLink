package com.andreibel.shortlink.service;

import com.andreibel.shortlink.dtos.ClickEventDTO;
import com.andreibel.shortlink.dtos.UrlMappingDTO;
import com.andreibel.shortlink.moduels.ClickEvent;
import com.andreibel.shortlink.moduels.UrlMapping;
import com.andreibel.shortlink.moduels.User;
import com.andreibel.shortlink.repository.ClickEventRepository;
import com.andreibel.shortlink.repository.UrlMappingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * Service for managing URL mappings and click events.
 */
@Service
@AllArgsConstructor
public class UrlMappingService {
    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    /**
     * Creates a new short URL mapping for the given original URL and user.
     *
     * @param originalUrl the original URL to shorten
     * @param user the user creating the short URL
     * @return the created UrlMappingDTO
     */
    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        UrlMapping saved = urlMappingRepository.save(urlMapping);
        return convertMapToDto(saved);
    }

    /**
     * Retrieves all URL mappings for a given user.
     *
     * @param user the user whose URLs to retrieve
     * @return list of UrlMappingDTOs
     */
    public List<UrlMappingDTO> getUserUrls(User user) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        return urlMappings.stream()
                .map(this::convertMapToDto)
                .toList();
    }

    private UrlMappingDTO convertMapToDto(UrlMapping urlMapping) {
        UrlMappingDTO dto = new UrlMappingDTO();
        dto.setId(urlMapping.getId());
        dto.setOriginalUrl(urlMapping.getOriginalUrl());
        dto.setShortUrl(urlMapping.getShortUrl());
        dto.setClickCount(urlMapping.getClickCount());
        dto.setCreatedDate(urlMapping.getCreatedDate());
        dto.setUsername(urlMapping.getUser().getUsername());
        return dto;
    }

    private String generateShortUrl() {
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            shortUrl.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return shortUrl.toString();
    }

    /**
     * Retrieves click events for a short URL grouped by date within a time range.
     *
     * @param shortUrl the short URL
     * @param start the start datetime
     * @param end the end datetime
     * @return list of ClickEventDTOs grouped by date, or null if URL not found
     */
    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
                    .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                    .entrySet().stream()
                    .map(entry -> {
                        ClickEventDTO clickEventDTO = new ClickEventDTO();
                        clickEventDTO.setClickDate(entry.getKey());
                        clickEventDTO.setCount(entry.getValue());
                        return clickEventDTO;
                    })
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * Retrieves total clicks for all URLs of a user grouped by date within a date range.
     *
     * @param user the user
     * @param start the start date
     * @param end the end date
     * @return map of LocalDate to click count
     */
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        return clickEvents.stream()
                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));

    }

    /**
     * Retrieves the original URL mapping by its short URL and logs the click event.
     *
     * @param shortUrl the short URL
     * @return the UrlMapping, or null if not found
     */
    public UrlMapping getOriginalUrlByShortUrl(String shortUrl) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if (urlMapping != null) {
            // Increment click count
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            // Log click event
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setUrlMapping(urlMapping);
            clickEvent.setClickDate(LocalDateTime.now());
            clickEventRepository.save(clickEvent);
        }
        return urlMapping;
    }
}
