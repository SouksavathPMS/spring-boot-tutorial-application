package com.kyedev.springboottutorial.app.springboottutorialapplication.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage {
    private HttpStatus statusCode;
    private String message;
}
