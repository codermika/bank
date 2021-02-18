package fi.test.banking.service;

import java.math.BigDecimal;
import java.util.List;

import fi.test.banking.domain.BankingTransaction;

public interface BankingService {
	List<BankingTransaction> getBankingTransactions(String ibanAccountNumber);

	void saveTransaction(String ibanAccountNumber, BigDecimal amount);
}
