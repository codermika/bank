package fi.test.banking.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.entity.BankingTransactionEntity;
import fi.test.banking.util.BankingTransformer;

class BankingTransformerTest {
	private static final BigDecimal TRANSACTION_BALANCE = BigDecimal.valueOf(210.56);
	private static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(110.56);
	private static final LocalDateTime TRANSACTION_DATE_TIME = createTestDateTime();

	@Test
	void testTransform() {
		List<BankingTransactionEntity> transactionEntitys = initializeTest();
		List<BankingTransaction> bankingTransactions = new BankingTransformer().transform(transactionEntitys);
		doAsserts(bankingTransactions);
	}

	private void doAsserts(List<BankingTransaction> bankingTransactions) {
		assertEquals(1, bankingTransactions.size());
		BankingTransaction transaction = bankingTransactions.get(0);
		assertEquals(TRANSACTION_DATE_TIME, transaction.getDate());
		assertEquals(TRANSACTION_AMOUNT, transaction.getAmount());
		assertEquals(TRANSACTION_BALANCE, transaction.getBalance());
	}

	private List<BankingTransactionEntity> initializeTest() {
		BankingTransactionEntity transactionEntity = new BankingTransactionEntity();
		transactionEntity.setDate(TRANSACTION_DATE_TIME);
		transactionEntity.setAmount(TRANSACTION_AMOUNT);
		transactionEntity.setBalance(TRANSACTION_BALANCE);

		List<BankingTransactionEntity> transactionEntitys = new ArrayList<>();
		transactionEntitys.add(transactionEntity);
		return transactionEntitys;
	}

	private static LocalDateTime createTestDateTime() {
		String str = "2020-04-08 12:30";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		return dateTime;
	}
}
