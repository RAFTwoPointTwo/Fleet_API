package com.fleet.fleet_api.exceptions;

import com.fleet.fleet_api.models.FleetLog;
import com.fleet.fleet_api.repositories.FleetLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionsHandler {

    private final FleetLogRepository fleetLogRepository;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur d'absence de resource ] Lancée par le handler des < ResourceNotFoundException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur de duplication de resource ] Lancée par le handler des < DuplicateResourceException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidAssetStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidState(InvalidAssetStateException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur d'état d'asset ] Lancée par le handler des < InvalidAssetStateException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @ExceptionHandler(InvalidRequestParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestParam(InvalidRequestParamException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur de paramètre de rêquete ] Lancée par le handler des < InvalidRequestParamException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur d'auth ] Lancée par le handler des < AuthException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur de validation de requête ] Lancée par le handler des < MethodArgumentNotValidException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        String clearedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Champ invalide")
                .findFirst()
                .orElse("Données d'entrée invalides");

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                clearedMessage,
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur métier ] Lancée par le handler des < BusinessException >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtExceptions(Exception ex, HttpServletRequest request) {

        FleetLog log = new FleetLog();
        log.setEvent("[ Erreur interne inattendue ] Lancée par le handler des < Exception >. ** Détails : " + ex.getMessage());
        fleetLogRepository.save(log);

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Une erreur interne est survenue. Veuillez contacter un administrateur.",
                LocalDateTime.now(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}