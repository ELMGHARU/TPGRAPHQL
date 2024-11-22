package com.example.GrapheQL.controllers;

import com.example.GrapheQL.entities.TypeTransaction;

public class TransactionRequest {

    private Long compteId; // ID of the compte (account)
    private double montant; // Amount of the transaction
    private String date; // Date of the transaction
    private TypeTransaction type; // Type of the transaction (e.g., deposit, withdrawal)

    // Constructor
    public TransactionRequest(Long compteId, double montant, String date, TypeTransaction type) {
        this.compteId = compteId;
        this.montant = montant;
        this.date = date;
        this.type = type;
    }

    // Getters
    public Long getCompteId() {
        return compteId;
    }

    public double getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }

    public TypeTransaction getType() {
        return type;
    }

    // Setters (optional if needed)
    public void setCompteId(Long compteId) {
        this.compteId = compteId;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(TypeTransaction type) {
        this.type = type;
    }
}
