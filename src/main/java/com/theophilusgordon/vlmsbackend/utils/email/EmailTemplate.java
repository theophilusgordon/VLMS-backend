package com.theophilusgordon.vlmsbackend.utils.email;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("activate-account"),
    ACCOUNT_ACTIVATED("account-activated"),
    REQUEST_PASSWORD_RESET("request-password-reset"),
    PASSWORD_RESET_SUCCESS("password-reset-success"),
    CHECKIN_NOTIFICATION("checkin-notification"),
    CHECKOUT_NOTIFICATION("checkout-notification"),
    CHECKIN_SUCCESS("checkin-success");

    private final String templateName;

    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
}
