package com.kursaha.mailkeets.dto;

import com.google.gson.JsonArray;
import lombok.Data;

@Data
public class ErrorMessageDto {
    private String status;
    private String type;
    private String title;
    private String message;
    private JsonArray fieldErrors;
}
