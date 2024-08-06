package com.theophilusgordon.vlmsbackend.utils.email;

import lombok.Getter;

@Getter
//@AllArgsConstructor
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("activate-account"),
    ACCOUNT_ACTIVATED("account-activated");

    private final String templateName;

    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
}
