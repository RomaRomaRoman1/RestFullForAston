package org.example.exceptions;

import java.math.BigDecimal;

public class NotEnoughBalance extends RuntimeException {
    public NotEnoughBalance(BigDecimal countOnBalance, BigDecimal countOnTransfer) {
        super("Your balance is : " + countOnBalance + " and need for transfer : " + countOnTransfer);
    }
}
