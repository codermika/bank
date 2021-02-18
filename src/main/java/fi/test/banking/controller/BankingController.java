package fi.test.banking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.domain.BankingTransactionAmount;
import fi.test.banking.service.BankingService;
import fi.test.banking.util.BankingInputChecker;

@RestController
public class BankingController {
	@Autowired
	private BankingService service;

	@Autowired
	private BankingInputChecker inputChecker;

	@GetMapping("/api/accounts/{ibanAccountNumber}/statement")
	public List<BankingTransaction> getStatement(@PathVariable String ibanAccountNumber) {
		return service.getBankingTransactions(ibanAccountNumber);
	}

	@PostMapping("/api/accounts/{ibanAccountNumber}/deposit")
	public void deposit(@PathVariable String ibanAccountNumber,
			@RequestBody BankingTransactionAmount bankingTransactionAmount) {
		inputChecker.checkTransactionAmount(bankingTransactionAmount.getAmount());
		service.saveTransaction(ibanAccountNumber, bankingTransactionAmount.getAmount());
	}

	@PostMapping("/api/accounts/{ibanAccountNumber}/withdraw")
	public void withdraw(@PathVariable String ibanAccountNumber,
			@RequestBody BankingTransactionAmount bankingTransactionAmount) {
		inputChecker.checkTransactionAmount(bankingTransactionAmount.getAmount());
		service.saveTransaction(ibanAccountNumber, bankingTransactionAmount.getAmount().negate());
	}
}
