package com.paisawise.controller;

import com.paisawise.dto.Notification.FinancialAlertResponse;
import com.paisawise.service.FinancialAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/financial-alerts")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FinancialAlertController {

    private final FinancialAlertService financialAlertService;

    @GetMapping
    public ResponseEntity<List<FinancialAlertResponse>> getAlerts() {

        return ResponseEntity.ok(
                financialAlertService.getAlerts()
        );
    }
}
