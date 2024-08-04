package com.theophilusgordon.vlmsbackend.exception;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExceptionResponse {
    private String message;
    private int status;
}
