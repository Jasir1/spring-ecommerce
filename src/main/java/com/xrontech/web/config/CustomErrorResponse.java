package com.xrontech.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CustomErrorResponse {
    private Integer httpStatus;
    private String exception;
    private String message;
}
