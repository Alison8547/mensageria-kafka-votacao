package com.api.kafkavotacao.application.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@JsonInclude(Include.NON_EMPTY)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    final LocalDateTime timestamp;
    final String title;
    final Integer status;
    final String detail;
    List<ValidationError> errors;

    @Data
    @JsonInclude(Include.NON_EMPTY)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class ValidationError {

        final String field;
        final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
