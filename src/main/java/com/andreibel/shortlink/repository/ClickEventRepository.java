package com.andreibel.shortlink.repository;

import com.andreibel.shortlink.moduels.ClickEvent;
import com.andreibel.shortlink.moduels.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for managing {@link ClickEvent} entities.
 * <p>
 * Provides methods to query click events by URL mapping and date range.
 */
@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    /**
     * Finds click events for a specific short URL within a given date range.
     *
     * @param shortUrl  the short URL token to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate   the end of the date range (inclusive)
     * @return a list of matching {@link ClickEvent} entities
     */
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping shortUrl, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds click events for a list of short URLs within a given date range.
     *
     * @param shortUrls a list of short URL tokens to filter by
     * @param startDate the start of the date range (inclusive)
     * @param endDate   the end of the date range (inclusive)
     * @return a list of matching {@link ClickEvent} entities
     */
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> shortUrls, LocalDateTime startDate, LocalDateTime endDate);
}