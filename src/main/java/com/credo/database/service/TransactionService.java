package com.credo.database.service;

import com.credo.database.domain.Transaction;
import com.credo.database.repository.TransactionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Transaction}.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Save a transaction.
     *
     * @param transaction the entity to save.
     * @return the persisted entity.
     */
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        return transactionRepository.save(transaction);
    }

    /**
     * Partially update a transaction.
     *
     * @param transaction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Transaction> partialUpdate(Transaction transaction) {
        log.debug("Request to partially update Transaction : {}", transaction);

        return transactionRepository
            .findById(transaction.getId())
            .map(
                existingTransaction -> {
                    if (transaction.getTotalAmount() != null) {
                        existingTransaction.setTotalAmount(transaction.getTotalAmount());
                    }
                    if (transaction.getDate() != null) {
                        existingTransaction.setDate(transaction.getDate());
                    }
                    if (transaction.getGenericSubItemsPurchased() != null) {
                        existingTransaction.setGenericSubItemsPurchased(transaction.getGenericSubItemsPurchased());
                    }
                    if (transaction.getCostSubItemsPurchased() != null) {
                        existingTransaction.setCostSubItemsPurchased(transaction.getCostSubItemsPurchased());
                    }
                    if (transaction.getNumberOfMemberships() != null) {
                        existingTransaction.setNumberOfMemberships(transaction.getNumberOfMemberships());
                    }
                    if (transaction.getDonation() != null) {
                        existingTransaction.setDonation(transaction.getDonation());
                    }
                    if (transaction.getEventDonation() != null) {
                        existingTransaction.setEventDonation(transaction.getEventDonation());
                    }
                    if (transaction.getNotes() != null) {
                        existingTransaction.setNotes(transaction.getNotes());
                    }

                    return existingTransaction;
                }
            )
            .map(transactionRepository::save);
    }

    /**
     * Get all the transactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        return transactionRepository.findAll(pageable);
    }

    /**
     * Get one transaction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Transaction> findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        return transactionRepository.findById(id);
    }

    /**
     * Delete the transaction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.deleteById(id);
    }
}
