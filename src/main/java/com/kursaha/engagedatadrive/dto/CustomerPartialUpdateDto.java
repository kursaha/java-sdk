package com.kursaha.engagedatadrive.dto;

import lombok.Data;

/**
 * Send the payload to Kursaha for creating or updating a customer
 */
@Data
public class CustomerPartialUpdateDto {
    private String customerId;

    private CustomerDto customerDto;
}
