package com.theophilusgordon.vlmsbackend.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String resource, String id) {
        super(resource + " with id " + id + " not found");
    }

    public NotFoundException(String resource, int id) {
        super(resource + " with id " + id + " not found");
    }

    public NotFoundException(String resource, UUID id) {
        super(resource + " with id " + id.toString() + " not found");
    }
}

