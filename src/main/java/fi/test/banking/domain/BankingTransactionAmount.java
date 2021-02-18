package fi.test.banking.domain;

import java.math.BigDecimal;

public class BankingTransactionAmount {
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
