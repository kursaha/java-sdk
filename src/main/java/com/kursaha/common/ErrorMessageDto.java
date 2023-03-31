package com.kursaha.common;

import com.google.gson.JsonArray;
import lombok.Data;
import lombok.ToString;
/**
 * Response in case of error
 */
@Data
@ToString
public class ErrorMessageDto {
    private String status;
    private String type;
    private String title;
    private String message;
    private JsonArray fieldErrors;
}
