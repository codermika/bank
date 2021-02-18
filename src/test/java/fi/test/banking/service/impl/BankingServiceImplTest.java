package fi.test.banking.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.entity.BankingAccountEntity;
import fi.test.banking.entity.BankingTransactionEntity;
import fi.test.banking.exception.BankingAccountNotFoundException;
import fi.test.banking.exception.NoBalanceForWithdrawException;
import fi.test.banking.repository.BankingAccountRepository;
import fi.test.banking.service.impl.BankingServiceImpl;
import fi.test.banking.util.BankingTransformer;

class BankingServiceImplTest {
	private static final String TEST_IBAN = "FI123";
	private static final BigDecimal ACCOUNT_ORIGINAL_BALANCE = BigDecimal.valueOf(111.11);
	private static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(222.22);

	@InjectMocks
	private BankingServiceImpl serviceImpl;

	@Mock
	private BankingAccountRepository repository;

	@Mock
	private BankingTransformer transformer;

	@BeforeEach
	public void setUp() {
		openMocks(this);
	}

	@Test
	void testGetBankingTransactions() {
		BankingAccountEntity accountEntity = new BankingAccountEntity();
		when(repository.findByIbanAccountNumber(TEST_IBAN)).thenReturn(Optional.of(accountEntity));

		List<BankingTransaction> transactions = new ArrayList<>();
		when(transformer.transform(accountEntity.getBankTransactions())).thenReturn(transactions);

		assertSame(transactions, serviceImpl.getBankingTransactions(TEST_IBAN));
	}

	@Test
	void testGetBankingTransactionsException() {
		when(repository.findByIbanAccountNumber(TEST_IBAN)).thenReturn(Optional.empty());

		Assertions.assertThrows(BankingAccountNotFoundException.class, () -> {
			serviceImpl.getBankingTransactions(TEST_IBAN);
		});
	}

	@Test
	void testSaveTransaction() {
		doSaveTransactionTest(TRANSACTION_AMOUNT);
	}

	@Test
	void testSaveTransactionBalanceGoesZero() {
		doSaveTransactionTest(ACCOUNT_ORIGINAL_BALANCE.negate());
	}

	@Test
	void testSaveTransactionNoBalanceForWithdrawException() {
		initializeSaveTransactionTest();

		BigDecimal amount = TRANSACTION_AMOUNT.negate();
		Assertions.assertThrows(NoBalanceForWithdrawException.class, () -> {
			serviceImpl.saveTransaction(TEST_IBAN, amount);
		});
	}

	@Test
	void testSaveTransactionBankingAccountNotFoundException() {
		when(repository.findByIbanAccountNumber(TEST_IBAN)).thenReturn(Optional.empty());

		BigDecimal amount = BigDecimal.valueOf(111.11);
		Assertions.assertThrows(BankingAccountNotFoundException.class, () -> {
			serviceImpl.saveTransaction(TEST_IBAN, amount);
		});
	}

	private void doSaveTransactionTest(BigDecimal transactionAmount) {
		initializeSaveTransactionTest();
		serviceImpl.saveTransaction(TEST_IBAN, transactionAmount);
		doVerificationsForSaveTransactionTest(transactionAmount);
	}

	private void doVerificationsForSaveTransactionTest(BigDecimal transactionAmount) {
		ArgumentCaptor<BankingAccountEntity> accountCaptor = ArgumentCaptor.forClass(BankingAccountEntity.class);
		verify(repository).save(accountCaptor.capture());

		BankingAccountEntity savedAccountEntity = accountCaptor.getValue();
		BigDecimal newBalance = ACCOUNT_ORIGINAL_BALANCE.add(transactionAmount);
		assertEquals(newBalance, savedAccountEntity.getBalance());
		assertEquals(1, savedAccountEntity.getBankTransactions().size());

		BankingTransactionEntity savedTransaction = savedAccountEntity.getBankTransactions().get(0);
		assertEquals(transactionAmount, savedTransaction.getAmount());
		assertEquals(newBalance, savedTransaction.getBalance());
		assertNotNull(savedTransaction.getDate());
	}

	private void initializeSaveTransactionTest() {
		BankingAccountEntity originalAccountEntity = new BankingAccountEntity();
		originalAccountEntity.setBalance(ACCOUNT_ORIGINAL_BALANCE);
		when(repository.findByIbanAccountNumber(TEST_IBAN)).thenReturn(Optional.of(originalAccountEntity));
	}
}
