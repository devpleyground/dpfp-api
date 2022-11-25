package com.innople.devpleyground.dpfpapi.common.exceptions;

import com.innople.devpleyground.dpfpapi.common.constants.ErrorConstants.ErrorCode;

public class DpfpCustomException extends BaseException {
    private static final long serialVersionUID = 137749532600099753L;
    private String ERROR_STATUS , ERROR_CODE, ERROR_MESSAGE, ERROR_CODE_MESSAGE;

    public DpfpCustomException() {
        super();
    }

    public DpfpCustomException(ErrorCode errorCode) {
        super("["+errorCode.getStatus() + "|"+ errorCode.getCode() + "]" + errorCode.toString() + "|" +  errorCode.getMessage());
        ERROR_STATUS = String.valueOf(errorCode.getStatus());
        ERROR_CODE  = errorCode.getCode() ;
        ERROR_CODE_MESSAGE = errorCode.toString() ;
        ERROR_MESSAGE = errorCode.getMessage() ;
    }

    public DpfpCustomException(ErrorCode errorCode, String message) {
        super(errorCode.toString() + "|" + message);
        ERROR_STATUS = String.valueOf(errorCode.getStatus());
        ERROR_CODE  = errorCode.getCode() ;
        ERROR_CODE_MESSAGE = errorCode.toString() ;
        ERROR_MESSAGE = message ;
    }
}
