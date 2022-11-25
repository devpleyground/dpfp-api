package com.innople.devpleyground.dpfpapi.common.handlers;

import com.innople.devpleyground.dpfpapi.common.exceptions.DpfpCustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // To handle Unhandled error
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String apiNotSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DpfpCustomException.class)
    public void invalidRequest(HttpServletResponse response, DpfpCustomException exception) throws IOException {
        exception.printStackTrace();
        response.sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }
}
