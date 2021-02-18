package fi.test.banking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.entity.BankingAccountEntity;
import fi.test.banking.entity.BankingTransactionEntity;
import fi.test.banking.exception.BankingAccountNotFoundException;
import fi.test.banking.exception.NoBalanceForWithdrawException;
import fi.test.banking.repository.BankingAccountRepository;
import fi.test.banking.service.BankingService;
import fi.test.banking.util.BankingTransformer;

@Service
public class BankingServiceImpl implements BankingService {
	@Autowired
	private BankingAccountRepository repository;

	@Autowired
	private BankingTransformer transformer;

	@Override
	public List<BankingTransaction> getBankingTransactions(String ibanAccountNumber) {
		BankingAccountEntity bankingAccountEntity = repository.findByIbanAccountNumber(ibanAccountNumber)
				.orElseThrow(BankingAccountNotFoundException::new);
		return transformer.transform(bankingAccountEntity.getBankTransactions());
	}

	@Override
	public void saveTransaction(String ibanAccountNumber, BigDecimal amount) {
		BankingAccountEntity bankingAccountEntity = repository.findByIbanAccountNumber(ibanAccountNumber)
				.orElseThrow(BankingAccountNotFoundException::new);

		BigDecimal newBalance = bankingAccountEntity.getBalance().add(amount);

		if (BigDecimal.ZERO.compareTo(newBalance) == 1) {
			throw new NoBalanceForWithdrawException();
		}

		bankingAccountEntity.setBalance(newBalance);
		addTransaction(amount, bankingAccountEntity, newBalance);

		repository.save(bankingAccountEntity);
	}

	private void addTransaction(BigDecimal amount, BankingAccountEntity bankingAccountEntity, BigDecimal newBalance) {
		BankingTransactionEntity transactionEntity = new BankingTransactionEntity();
		bankingAccountEntity.getBankTransactions().add(transactionEntity);
		transactionEntity.setDate(LocalDateTime.now());
		transactionEntity.setAmount(amount);
		transactionEntity.setBalance(newBalance);
	}
}
