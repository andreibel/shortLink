package com.andreibel.shortlink.controller;

import com.andreibel.shortlink.dtos.ClickEventDTO;
import com.andreibel.shortlink.dtos.UrlMappingDTO;
import com.andreibel.shortlink.moduels.User;
import com.andreibel.shortlink.service.UrlMappingService;
import com.andreibel.shortlink.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing URL mappings and analytics.
 * <p>
 * Provides endpoints for creating short URLs, retrieving user URLs,
 * fetching analytics, and getting total click statistics.
 */
@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {
    /**
     * Service for URL mapping operations.
     */
    private final UrlMappingService urlMappingService;

    /**
     * Service for user-related operations.
     */
    private final UserService userService;

    /**
     * Creates a short URL for the given original URL.
     *
     * @param request   a map containing the original URL to shorten
     * @param principal the authenticated user principal
     * @return a {@link ResponseEntity} containing the created {@link UrlMappingDTO}
     */
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request, Principal principal) {
        String originalUrl = request.get("originalUrl");
        User user = userService.findByUsername(principal.getName());
        UrlMappingDTO urlMappingDTO = urlMappingService.createShortUrl(originalUrl, user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    /**
     * Retrieves all short URLs created by the authenticated user.
     *
     * @param principal the authenticated user principal
     * @return a {@link ResponseEntity} containing a list of {@link UrlMappingDTO}
     */
    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<UrlMappingDTO> userUrls = urlMappingService.getUserUrls(user);
        return ResponseEntity.ok(userUrls);
    }

    /**
     * Retrieves analytics (click events) for a specific short URL within a date range.
     *
     * @param shortUrl  the short URL token
     * @param startDate the start date-time in ISO format
     * @param endDate   the end date-time in ISO format
     * @return a {@link ResponseEntity} containing a list of {@link ClickEventDTO}
     */
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        List<ClickEventDTO> clickEventDTOS = urlMappingService.getClickEventsByDate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDTOS);
    }

    /**
     * Retrieves the total number of clicks per day for the authenticated user within a date range.
     *
     * @param principal the authenticated user principal
     * @param startDate the start date in ISO format (yyyy-MM-dd)
     * @param endDate   the end date in ISO format (yyyy-MM-dd)
     * @return a {@link ResponseEntity} containing a map of dates to click counts
     */
    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate, Long>> getTotalClicksByDate(Principal principal, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        User user = userService.findByUsername(principal.getName());
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        Map<LocalDate, Long> totalClicks = urlMappingService.getTotalClicksByUserAndDate(user, start, end);
        return ResponseEntity.ok(totalClicks);
    }
}