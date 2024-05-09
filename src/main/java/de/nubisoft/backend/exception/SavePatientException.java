package de.nubisoft.backend.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class SavePatientException extends Exception {

    public SavePatientException(Throwable cause) {
        super(cause);
    }
}
