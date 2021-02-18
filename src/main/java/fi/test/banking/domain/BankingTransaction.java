package fi.test.banking.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankingTransaction {
	private LocalDateTime date;
	private BigDecimal amount;
	private BigDecimal balance;

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
