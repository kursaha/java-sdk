package com.kursaha.engagedatadrive.dto;

import com.kursaha.engagedatadrive.dto.enumeration.Gender;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the basic customer details.
 */
@Data
public class CustomerDto{
    /**
     * Remove any leading and trailing white space, and convert all characters to lowercase.
     */
    private String email;

    /**
     * Eliminate symbols and letters, strip leading zeroes, and remember to prefix the country code.
     */
    private String phoneNumber;

    /**
     * Lowercase only, no punctuation.
     */
    private String firstName;

    /**
     * Lowercase only, no punctuation.
     */
    private String lastName;

    /**
     * Utilize Gender enum.
     */
    private Gender gender;

    /**
     * Lowercase only, with no punctuation, no special characters, and no white space.
     */
    private String city;

    /**
     * Lowercase, with no punctuation, no special characters, and no white space.
     */
    private String state;

    /**
     * Must be in lowercase. We only accept
     * <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a>.
     */
    private String country;

    /**
     * Use lowercase, and no white space.
     */
    private String zip;

    /**
     * We only accept date of birth in <a href = "https://en.wikipedia.org/wiki/ISO_8601">ISO_8601</a> format.
     */
    private String dob;
}
