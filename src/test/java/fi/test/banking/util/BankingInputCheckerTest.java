package fi.test.banking.util;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.test.banking.exception.AmountMustBeGreaterThanZeroException;
import fi.test.banking.util.BankingInputChecker;

class BankingInputCheckerTest {
	private BankingInputChecker inputChecker = new BankingInputChecker();

	@Test
	void testCheckAmountExceptionWhenAmountIsNull() {
		checkExceptionIsThrownWhenBankingTransactionAmountIsNotValid(null);
	}

	@Test
	void testCheckAmountExceptionWhenAmountIsNegative() {
		checkExceptionIsThrownWhenBankingTransactionAmountIsNotValid(BigDecimal.valueOf(-111.11));
	}

	@Test
	void testCheckAmountExceptioonWhenAmountIsZero() {
		checkExceptionIsThrownWhenBankingTransactionAmountIsNotValid(BigDecimal.ZERO);
	}

	@Test
	void testCheckAmountNoExceptionWhenAmountIsPositive() {
		inputChecker.checkTransactionAmount(BigDecimal.valueOf(111.11));
	}

	private void checkExceptionIsThrownWhenBankingTransactionAmountIsNotValid(BigDecimal amount) {
		Assertions.assertThrows(AmountMustBeGreaterThanZeroException.class, () -> {
			inputChecker.checkTransactionAmount(amount);
		});
	}

}
