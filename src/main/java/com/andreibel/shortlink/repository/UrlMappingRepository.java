package com.andreibel.shortlink.repository;

import com.andreibel.shortlink.moduels.UrlMapping;
import com.andreibel.shortlink.moduels.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    UrlMapping findByShortUrl(String shortUrl);

    List<UrlMapping> findByUser(User createdBy);
}