package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.exception.StockFullException;
import com.yowyob.template.domain.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockFullException.class)
    public ProblemDetail handleStockException(StockFullException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Stock Overflow");
        problem.setType(URI.create("errors/stock-full"));
        return problem;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource Not Found");
        problem.setType(URI.create("errors/not-found"));
        return problem;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail handleValidationException(WebExchangeBindException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setTitle("Validation Error");
        problem.setType(URI.create("errors/validation-error"));
        problem.setProperty("errors", errors);
        return problem;
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicateKeyException(DuplicateKeyException ex) {
        String message = extractConstraintName(ex.getMessage());
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problem.setTitle("Duplicate Entry");
        problem.setType(URI.create("errors/duplicate-key"));
        return problem;
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = extractConstraintMessage(ex.getMessage());
        
        // Si c'est une violation de clé étrangère, retourner 400
        if (ex.getMessage() != null && ex.getMessage().contains("foreign key")) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
            problem.setTitle("Foreign Key Violation");
            problem.setType(URI.create("errors/foreign-key-violation"));
            return problem;
        }
        
        // Autre violation d'intégrité
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problem.setTitle("Data Integrity Violation");
        problem.setType(URI.create("errors/data-integrity"));
        return problem;
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.valueOf(ex.getStatusCode().value()), 
                ex.getReason() != null ? ex.getReason() : "Error"
        );
        problem.setTitle(ex.getStatusCode().toString());
        return problem;
    }
    
    /**
     * Extrait le nom de la contrainte du message d'erreur pour DuplicateKeyException
     */
    private String extractConstraintName(String message) {
        if (message == null) {
            return "Une entrée avec cette valeur existe déjà";
        }
        
        // Exemple: "duplicate key value violates unique constraint \"vehicle_vehicle_serial_number_key\""
        if (message.contains("vehicle_serial_number")) {
            return "Un véhicule avec ce numéro de série existe déjà";
        }
        if (message.contains("registration_number")) {
            return "Un véhicule avec ce numéro d'immatriculation existe déjà";
        }
        
        return "Une entrée avec cette valeur existe déjà";
    }
    
    /**
     * Extrait un message lisible pour les violations d'intégrité
     */
    private String extractConstraintMessage(String message) {
        if (message == null) {
            return "Violation de contrainte d'intégrité des données";
        }
        
        // Foreign key violations
        if (message.contains("vehicleownership_vehicle_id_fkey")) {
            return "Le véhicule spécifié n'existe pas";
        }
        if (message.contains("vehicle_id")) {
            return "Véhicule non trouvé";
        }
        
        return "Violation de contrainte d'intégrité des données";
    }
}