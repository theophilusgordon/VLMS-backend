package com.theophilusgordon.vlmsbackend.exception;

import com.theophilusgordon.vlmsbackend.constants.ExceptionConstants;

public class EmailSendFailureException extends RuntimeException {
    public EmailSendFailureException() {
        super(ExceptionConstants.EMAIL_SEND_FAILURE);
    }
}
