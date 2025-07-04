package com.andreibel.shortlink.moduels;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a mapping between an original URL and its shortened version.
 * Tracks metadata such as creation timestamp, click count, and the user owner.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "url_mapping")
public class UrlMapping {
    /**
     * Unique identifier for this URL mapping (auto-generated).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The original long URL before shortening.
     */
    private String originalUrl;

    /**
     * The generated short URL token.
     */
    private String shortUrl;

    /**
     * Total number of times this short URL has been accessed.
     */
    private int clickCount = 0;

    /**
     * Timestamp when this URL mapping was created.
     */
    private LocalDateTime createdDate;

    /**
     * The user who created this URL mapping.
     * Many-to-one relationship loaded lazily.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * List of click events associated with this URL mapping.
     * One-to-many relationship with cascade all and orphan removal.
     */
    @OneToMany(mappedBy = "urlMapping", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClickEvent> clickEvents;
}