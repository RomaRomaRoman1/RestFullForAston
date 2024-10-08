package org.example.service;

import org.example.dto.AccountResponse;
import org.example.dto.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ClientService {
    String createClient(String fullName, int PinCode);

    String transferTo(UUID fromId, UUID toId, BigDecimal amount, int pinCode);
    List<AccountResponse> getAccountsByClient(UUID clientId);
    List<TransactionResponse> getTransactionsByAccount(UUID accountId);
}
