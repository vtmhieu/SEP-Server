package group25.sep.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
class ExceptionHandlerTest {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handle(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        int statusCode = ex.getStatusCode().value();
        String reasonPhrase = (ex.getStatusCode() instanceof HttpStatus)
                ? ((HttpStatus) ex.getStatusCode()).getReasonPhrase()
                : "Error";

        body.put("status", statusCode);
        body.put("error", reasonPhrase);
        body.put("message", ex.getReason());
        return new ResponseEntity<>(body, ex.getStatusCode());
    }
}
