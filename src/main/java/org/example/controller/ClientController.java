package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.dto.AccountResponse;
import org.example.dto.TransactionResponse;
import org.example.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    @Operation(summary = "Создать нового клиента", description = "Создает нового клиента с указанным именем и пин-кодом")
    @PostMapping("/create")
    public ResponseEntity<String> createNewClient(
            @RequestHeader("fullName") String fullName,
            @RequestHeader("pinCode") int pinCode) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.createClient(fullName, pinCode));
    }

    @Operation(summary = "Перевести баланс", description = "Перевод средств с одного счета на другой, с проверкой пин-кода")
    @PostMapping("/transfer")
    public ResponseEntity<String> transferBalance(
            @RequestHeader("fromId") UUID fromId,
            @RequestHeader("toId") UUID toId,
            @RequestHeader("amount") BigDecimal amount,
            @RequestHeader("pinCode") int pinCode) {
        return ResponseEntity.status(HttpStatus.OK).body(clientService.transferTo(fromId, toId, amount, pinCode));
    }

    @Operation(summary = "Получить все счета клиента", description = "Возвращает список всех счетов, связанных с данным клиентом")
    @GetMapping("/{clientId}/accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccountsForClient(@RequestHeader UUID clientId) {
        List<AccountResponse> accounts = clientService.getAccountsByClient(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(accounts);
    }

    @Operation(summary = "Получить все транзакции счета", description = "Возвращает все транзакции для указанного счета")
    @GetMapping("/account/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsForAccount(@RequestHeader UUID accountId) {
        List<TransactionResponse> transactions = clientService.getTransactionsByAccount(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }
}
