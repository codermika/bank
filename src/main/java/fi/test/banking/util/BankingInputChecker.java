package fi.test.banking.util;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import fi.test.banking.exception.AmountMustBeGreaterThanZeroException;

@Service
public class BankingInputChecker {
	public void checkTransactionAmount(BigDecimal amount) {
		if (amount == null || BigDecimal.ZERO.compareTo(amount) >= 0) {
			throw new AmountMustBeGreaterThanZeroException();
		}
	}
}
