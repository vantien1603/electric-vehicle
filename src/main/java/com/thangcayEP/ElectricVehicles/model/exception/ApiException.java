package com.thangcayEP.ElectricVehicles.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus
public class ApiException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public ApiException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }
}
