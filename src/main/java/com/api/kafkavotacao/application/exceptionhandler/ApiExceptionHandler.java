package com.api.kafkavotacao.application.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Campo inválido", status.value(),
                "Um ou mais campos não foram preenchidos corretamente");
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Lista inválida",
                HttpStatus.BAD_REQUEST.value(),
                "Lista e(ou) campo(s) não foram preenchidos corretamente");
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : violations) {
            String message = constraintViolation.getMessage();
            String field = "";
            for (Node node : constraintViolation.getPropertyPath()) {
                if (node.getName().equalsIgnoreCase("payloadList")
                        || node.getName().equalsIgnoreCase("<list element>")) {
                    field = "Informações de titulares";
                } else {
                    field = node.getName();
                }
            }
            error.addValidationError(field, message);
        }
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Não foi possível processar solicitação",
                status.value(),
                "Corpo da solicitação ou campo(s) inexistente(s), valor do campo não correspondente ao tipo esperado ou erro de sintaxe");
        return ResponseEntity.badRequest().body(error);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Formato da solicitação não suportado",
                status.value(), "Solicitação recusada porque o formato não é suportado (Formato suportado: JSON)");
        return ResponseEntity.status(status.value()).body(error);
    }

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<?> handleUnknownHostException(UnknownHostException ex) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Erro ao enviar mensagens",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Host desconhecido");
        log.error("Error connecting to node. Unkown host.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<?> handleKafkaTimeout(TimeoutException ex) {
        ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), "Serviço de mensageria indisponível",
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Não foi possível realizar conexão com o serviço de mensageria ou tópico não foi localizado");
        log.error("Connection could not be established. Broker may not be available.");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        if (body == null) {
            ApiErrorResponse error = new ApiErrorResponse(LocalDateTime.now(), status.getReasonPhrase(), status.value(),
                    null);
            return super.handleExceptionInternal(ex, error, headers, status, request);
        } else {
            return super.handleExceptionInternal(ex, body, headers, status, request);
        }
    }
}
