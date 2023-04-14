package com.phunghv.base.apimodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorRS extends BaseRS {

    private final String message;

    public ErrorRS(String errorMessage) {
        this.setCode(400);
        this.message = errorMessage;
    }
}
