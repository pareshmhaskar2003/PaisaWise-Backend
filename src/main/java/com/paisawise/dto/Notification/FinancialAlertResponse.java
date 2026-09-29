package com.paisawise.dto.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialAlertResponse {

    private String type;
    private String title;
    private String message;
    private String severity;
}