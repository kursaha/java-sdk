package com.kursaha.engagedatadrive.dto;

import lombok.Data;
import java.util.Map;

/**
 * This class accepts the event details.
 */
@Data
public class EventData {
    private String productId;
    private String category;
    private String subCategory1;
    private String subCategory2;
    private String subCategory3;
    private String brand;
    private Double price;
    private String deviceId;
    private String sessionId;
}
