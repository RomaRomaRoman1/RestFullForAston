package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    @Schema(description = "Полное имя клиента")
    private String clientName;
    @Schema(description = "ID счета")
    private UUID accountId;
    @Schema(description = "Текущий баланс счета")
    private BigDecimal balance;
}