package com.andreibel.shortlink.controller;

    import com.andreibel.shortlink.moduels.UrlMapping;
    import com.andreibel.shortlink.service.UrlMappingService;
    import lombok.AllArgsConstructor;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RestController;

    /**
     * REST controller responsible for handling URL redirection requests.
     * <p>
     * Resolves a short URL token to its original URL and issues an HTTP redirect.
     */
    @RestController
    @AllArgsConstructor
    public class RedirectController {

        /**
         * Service for URL mapping operations.
         */
        private final UrlMappingService urlMappingService;

        /**
         * Handles HTTP GET requests for a short URL token.
         * <p>
         * Looks up the original URL for the given short URL token and returns a 302 redirect response.
         * If the short URL does not exist, returns a 404 Not Found response.
         *
         * @param shortUrl the short URL token to resolve
         * @return a {@link ResponseEntity} with a 302 redirect to the original URL, or 404 if not found
         */
        @GetMapping("/{shortUrl}")
        public ResponseEntity<Void> getTotalClicksByDate(@PathVariable String shortUrl) {
            UrlMapping urlMapping = urlMappingService.getOriginalUrlByShortUrl(shortUrl);
            if (urlMapping != null) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("Location", urlMapping.getOriginalUrl());
                return ResponseEntity.status(302).headers(httpHeaders).build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    }