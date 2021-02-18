package fi.test.banking.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.entity.BankingTransactionEntity;

@Service
public class BankingTransformer {
	public List<BankingTransaction> transform(List<BankingTransactionEntity> bankingTransactionEntitys) {
		List<BankingTransaction> bankingTransactions = new ArrayList<>();

		for (BankingTransactionEntity bankingTransactionEntity : bankingTransactionEntitys) {
			BankingTransaction bankingTransaction = new BankingTransaction();
			bankingTransactions.add(bankingTransaction);

			bankingTransaction.setDate(bankingTransactionEntity.getDate());
			bankingTransaction.setAmount(bankingTransactionEntity.getAmount());
			bankingTransaction.setBalance(bankingTransactionEntity.getBalance());
		}

		return bankingTransactions;
	}
}
