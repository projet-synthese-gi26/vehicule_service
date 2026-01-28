package com.yowyob.template.infrastructure.adapters.inbound.rest;

import com.yowyob.template.domain.exception.NotFoundException;
import com.yowyob.template.domain.exception.StockFullException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        problem.setTitle("Ressource non trouvée");
        problem.setType(URI.create("errors/not-found"));
        return problem;
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ProblemDetail handleValidationException(WebExchangeBindException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Erreur de validation");
        problem.setTitle("Données invalides");
        problem.setType(URI.create("errors/validation-error"));
        problem.setProperty("errors", errors);
        return problem;
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public ProblemDetail handleDuplicateKeyException(DuplicateKeyException ex) {
        // On log l'erreur technique pour le développeur
        log.error("Erreur de clé dupliquée (SQL) : ", ex);

        String message = extractConstraintName(ex.getMessage());
        
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, message);
        problem.setTitle("Conflit de données (Doublon)");
        problem.setType(URI.create("errors/duplicate-key"));
        
        // On ajoute le détail technique pour aider au debug (visible dans le JSON)
        problem.setProperty("technical_detail", ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage());
        
        return problem;
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Violation d'intégrité (SQL) : ", ex);
        String message = extractConstraintMessage(ex.getMessage());
        
        // Si c'est une violation de clé étrangère, retourner 400
        HttpStatus status = ex.getMessage() != null && ex.getMessage().contains("foreign key") 
                ? HttpStatus.BAD_REQUEST 
                : HttpStatus.CONFLICT;

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
        problem.setTitle("Violation de contrainte");
        problem.setType(URI.create("errors/data-integrity"));
        return problem;
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatusException(ResponseStatusException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.valueOf(ex.getStatusCode().value()), 
                ex.getReason() != null ? ex.getReason() : "Erreur HTTP"
        );
        problem.setTitle("Erreur " + ex.getStatusCode().value());
        return problem;
    }
    
    /**
     * Extrait le nom de la contrainte du message d'erreur pour DuplicateKeyException
     */
    private String extractConstraintName(String message) {
        if (message == null) {
            return "Une entrée avec cette valeur existe déjà";
        }
        
        // Gestion spécifique des contraintes définies dans schema.sql
        if (message.contains("idx_unique_primary_vehicle_per_role")) {
            return "Vous avez déjà un véhicule défini comme 'Principal'. Un seul est autorisé par rôle.";
        }
        if (message.contains("vehicle_serial_number")) {
            return "Un véhicule avec ce numéro de série (VIN) existe déjà.";
        }
        if (message.contains("registration_number")) {
            return "Un véhicule avec ce numéro d'immatriculation existe déjà.";
        }
        // Pour les tables de référence (Smart Creation)
        if (message.contains("make_name")) return "Cette marque existe déjà (concurrence).";
        if (message.contains("model_name")) return "Ce modèle existe déjà (concurrence).";
        
        return "Une entrée avec cette valeur existe déjà.";
    }
    
    /**
     * Extrait un message lisible pour les violations d'intégrité
     */
    private String extractConstraintMessage(String message) {
        if (message == null) return "Violation de contrainte d'intégrité des données";
        
        if (message.contains("vehicleownership_vehicle_id_fkey")) return "Le véhicule spécifié n'existe pas.";
        if (message.contains("vehicleownership_party_id_fkey")) return "L'utilisateur spécifié n'existe pas.";
        
        return "Violation de contrainte d'intégrité des données (Clé étrangère ou Check failed).";
    }
}