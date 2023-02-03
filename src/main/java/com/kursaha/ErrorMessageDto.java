package com.kursaha;

import com.google.gson.JsonArray;
import lombok.Data;

/**
 * Response in case of error
 */
@Data
public class ErrorMessageDto {
    private String status;
    private String type;
    private String title;
    private String message;
    private JsonArray fieldErrors;
}
