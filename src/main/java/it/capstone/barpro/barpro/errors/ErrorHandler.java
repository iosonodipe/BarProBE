package it.capstone.barpro.barpro.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException error){
        Map errorResponse = new HashMap();
        error.getBindingResult().getAllErrors().forEach(
                er->{
                    FieldError frError = (FieldError) er; // recupera il nome del campo non valido
                    String nomeCampo = frError.getField(); // recupera il nome del campo non valido
                    String errorMessage = er.getDefaultMessage(); // recupera il messaggio dell'annotazione @NotEmpy dentro il nostro Dto
                    errorResponse.put(nomeCampo, errorMessage);
                }
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
