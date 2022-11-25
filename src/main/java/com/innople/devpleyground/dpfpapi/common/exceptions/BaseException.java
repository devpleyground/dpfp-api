package com.innople.devpleyground.dpfpapi.common.exceptions;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -4254971491763062220L;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}
