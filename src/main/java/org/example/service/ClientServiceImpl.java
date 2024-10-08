package org.example.service;

import org.example.dto.AccountResponse;
import org.example.dto.TransactionResponse;
import org.example.entity.*;
import org.example.exceptions.ClientNotFound;
import org.example.exceptions.InvalidPinCode;
import org.example.exceptions.NotEnoughBalance;
import org.example.repository.AccountRepository;
import org.example.repository.ClientsRepository;
import org.example.repository.TransactionHistoryRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
public class ClientServiceImpl implements ClientService {
    private final ClientsRepository clientsRepository;
    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public ClientServiceImpl(ClientsRepository clientsRepository, AccountRepository accountRepository, TransactionHistoryRepository transactionHistoryRepository) {
        this.clientsRepository = clientsRepository;
        this.accountRepository = accountRepository;
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    public String createClient(String fullName, int pinCode) {
        Client client = new Client();
        client.setFullName(fullName);
        client.setPinCode(pinCode);

        // Создаем первый счет для клиента
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1000)); // Стартовый баланс
        account.setClient(client); // Устанавливаем клиента как владельца счета

        // Сохраняем клиента и его счета
        client.getAccounts().add(account);
        clientsRepository.save(client);

        return "Client created with account ID: " + account.getId();
    }

    public String transferTo(UUID fromId, UUID toId, BigDecimal amount, int pinCode) {
        // Поиск счетов отправителя и получателя
        Account fromAccount = accountRepository.findById(fromId)
                .orElseThrow(() -> new ClientNotFound(fromId));
        Account toAccount = accountRepository.findById(toId)
                .orElseThrow(() -> new ClientNotFound(toId));

        if (fromAccount.getClient().getPinCode() != pinCode) {
            throw new InvalidPinCode();
        }

        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            // Создание записи в истории транзакций
            TransactionHistory fromTransaction = new TransactionHistory(null, fromAccount, amount.negate(), "TRANSFER", LocalDateTime.now());
            TransactionHistory toTransaction = new TransactionHistory(null, toAccount, amount, "TRANSFER", LocalDateTime.now());

            transactionHistoryRepository.save(fromTransaction);
            transactionHistoryRepository.save(toTransaction);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            return "Transfer successful";
        } else {
            throw  new NotEnoughBalance(fromAccount.getBalance(), amount);
        }
    }
    @Override
    public List<AccountResponse> getAccountsByClient(UUID clientId) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        return accounts.stream()
                .map(account -> new AccountResponse(account.getClient().getFullName(), account.getId(), account.getBalance()))
                .collect(Collectors.toList());
    }
    @Override
    public List<TransactionResponse> getTransactionsByAccount(UUID accountId) {
        List<TransactionHistory> transactions = transactionHistoryRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(transaction -> new TransactionResponse(transaction.getId(), transaction.getAmount(), transaction.getOperationType(), transaction.getTimestamp()))
                .collect(Collectors.toList());
    }
}
