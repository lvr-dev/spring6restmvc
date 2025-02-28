package guru.springframework.spring6restmvc.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ControllerAdvice
public class CustomerErrorController {

    @ExceptionHandler
    ResponseEntity handleJPAValidations(TransactionSystemException exception) {
      ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

      if (exception.getCause().getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();

        List errors = ve.getConstraintViolations().stream()
          .map(constrainViolation -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(constrainViolation.getPropertyPath().toString(),
              constrainViolation.getMessage());
              return errorMap;
          }).collect(Collectors.toList());

          return responseEntity.body(errors);
      }

      return responseEntity.build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){

        List errorList = exception.getFieldErrors().stream()
          .map(fieldError -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            return errorMap;
          }).collect(Collectors.toList());
          
        return ResponseEntity.badRequest().body(errorList);
    }
}
