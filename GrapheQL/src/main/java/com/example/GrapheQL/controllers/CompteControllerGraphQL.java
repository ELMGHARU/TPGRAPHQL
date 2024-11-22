package com.example.GrapheQL.controllers;

import com.example.GrapheQL.entities.Compte;
import com.example.GrapheQL.entities.Transaction;
import com.example.GrapheQL.entities.TypeTransaction;
import com.example.GrapheQL.repositories.CompteRepository;
import com.example.GrapheQL.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class CompteControllerGraphQL {

    private final CompteRepository compteRepository; // Déclaration du repository en tant que final
    private final TransactionRepository transactionRepository; // Déclaration du TransactionRepository

    @QueryMapping
    public List<Compte> allComptes() {
        return compteRepository.findAll();
    }

    @QueryMapping
    public Compte compteById(@Argument Long id) {
        Compte compte = compteRepository.findById(id).orElse(null);
        if (compte == null) {
            throw new RuntimeException(String.format("Compte %s not found", id));
        }
        return compte;
    }

    @MutationMapping
    public Compte saveCompte(@Argument Compte compte) {
        return compteRepository.save(compte);
    }

    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = compteRepository.count();
        double sum = compteRepository.sumSoldes(); // Vérifiez que sumSoldes est bien implémenté dans le repository
        double average = count > 0 ? sum / count : 0;

        return Map.of(
                "count", count,
                "sum", sum,
                "average", average
        );
    }

    @MutationMapping
    @Transactional // Assure que l'opération est atomique
    public Transaction addTransaction(@Argument TransactionRequest transactionRequest) {
        // Récupérer le compte
        Compte compte = compteRepository.findById(transactionRequest.getCompteId())
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        // Valider le montant de la transaction (par exemple, s'assurer qu'il est positif)
        if (transactionRequest.getMontant() <= 0) {
            throw new RuntimeException("Transaction amount must be greater than zero");
        }

        // Créer une nouvelle transaction
        Transaction transaction = new Transaction();
        transaction.setMontant(transactionRequest.getMontant());
        transaction.setDate(transactionRequest.getDate());
        transaction.setType(transactionRequest.getType());
        transaction.setCompte(compte);

        // Mettre à jour le solde du compte si nécessaire (par exemple, pour un retrait ou un dépôt)
        if (transactionRequest.getType() == TypeTransaction.WITHDRAWAL && compte.getSolde() < transactionRequest.getMontant()) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }

        if (transactionRequest.getType() == TypeTransaction.WITHDRAWAL) {
            compte.setSolde(compte.getSolde() - transactionRequest.getMontant());
        } else if (transactionRequest.getType() == TypeTransaction.DEPOSIT) {
            compte.setSolde(compte.getSolde() + transactionRequest.getMontant());
        }

        // Sauvegarder la transaction et les mises à jour du compte
        transactionRepository.save(transaction);
        compteRepository.save(compte);

        return transaction;
    }
}
