package com.fleet.fleet_api.exceptions;

public class InvalidAssetStateException extends BusinessException {
    public InvalidAssetStateException(String message) {
        super(message);
    }
}