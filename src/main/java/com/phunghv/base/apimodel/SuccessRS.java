package com.phunghv.base.apimodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessRS<T> extends BaseRS {

    private final T data;

    public SuccessRS(T data) {
        this.setCode(200);
        this.data = data;
    }
}
