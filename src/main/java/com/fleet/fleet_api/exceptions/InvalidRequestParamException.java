package com.fleet.fleet_api.exceptions;

public class InvalidRequestParamException extends BusinessException {
    public InvalidRequestParamException(String message) {
        super(message);
    }
}