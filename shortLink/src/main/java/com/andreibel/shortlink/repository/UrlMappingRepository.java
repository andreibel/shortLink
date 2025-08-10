package com.andreibel.shortlink.repository;

import com.andreibel.shortlink.moduels.UrlMapping;
import com.andreibel.shortlink.moduels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link UrlMapping} entities.
 */
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    /**
     * Finds a UrlMapping by its short URL.
     *
     * @param shortUrl the short URL string
     * @return the UrlMapping entity, or null if not found
     */
    UrlMapping findByShortUrl(String shortUrl);

    /**
     * Finds all UrlMappings created by a specific user.
     *
     * @param createdBy the user who created the mappings
     * @return list of UrlMapping entities
     */
    List<UrlMapping> findByUser(User createdBy);

    UrlMapping findByShortUrlAndUser(String shortUrl, User user);
}