package com.phunghv.base.controller;

import com.phunghv.base.apimodel.BaseRS;
import com.phunghv.base.apimodel.ErrorRS;
import com.phunghv.base.apimodel.SuccessRS;

public class BaseController {

    protected <T> BaseRS ok(T data) {
        return new SuccessRS<>(data);
    }

    protected BaseRS error(String errorMessage) {
        return new ErrorRS(errorMessage);
    }
}
