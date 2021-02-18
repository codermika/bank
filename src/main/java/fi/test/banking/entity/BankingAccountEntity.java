package fi.test.banking.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class BankingAccountEntity {
	private @Id @GeneratedValue Long id;
	private String ibanAccountNumber;
	private BigDecimal balance;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BankingTransactionEntity> bankTransactions = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIbanAccountNumber() {
		return ibanAccountNumber;
	}

	public void setIbanAccountNumber(String ibanAccountNumber) {
		this.ibanAccountNumber = ibanAccountNumber;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<BankingTransactionEntity> getBankTransactions() {
		return bankTransactions;
	}

	public void setBankTransactions(List<BankingTransactionEntity> bankTransactions) {
		this.bankTransactions = bankTransactions;
	}
}
