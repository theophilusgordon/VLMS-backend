package com.theophilusgordon.vlmsbackend.exception;

public class UploadFailException extends RuntimeException {
    public UploadFailException(String message) {
        super(message);
    }
}
