package net.unicornuniversity.bdij.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class OperationForbiddenException extends RuntimeException {
    public OperationForbiddenException(String message) {
        super(message);
    }
}


