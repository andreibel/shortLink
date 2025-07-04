package com.andreibel.shortlink.dtos;

import lombok.Data;

import java.time.LocalDate;
/**
 * Data Transfer Object representing a click event for a shortened URL.
 * <p>
 * Contains the date of the click event and the number of clicks recorded on that date.
 */
@Data
public class ClickEventDTO {
    private LocalDate clickDate;
    private Long count;
}
