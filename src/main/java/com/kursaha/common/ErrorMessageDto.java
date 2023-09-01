package com.kursaha.common;

import com.google.gson.JsonArray;
import lombok.Data;
import lombok.ToString;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ErrorMessageDto{");
        sb.append("status='").append(status).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", fieldErrors=").append(fieldErrors);
        sb.append('}');
        return sb.toString();
    }
}
