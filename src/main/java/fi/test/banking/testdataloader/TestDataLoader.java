package fi.test.banking.testdataloader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.test.banking.entity.BankingAccountEntity;
import fi.test.banking.entity.BankingTransactionEntity;
import fi.test.banking.repository.BankingAccountRepository;

/**
 * The TestDataLoader class, remove this before production.
 * 
 * @author mika
 *
 */
@Configuration
class TestDataLoader {
	@Bean
	CommandLineRunner initDatabase(BankingAccountRepository repository) {
		return args -> {
			BankingTransactionEntity transaction = new BankingTransactionEntity();
			transaction.setAmount(BigDecimal.valueOf(111.11));
			transaction.setBalance(BigDecimal.valueOf(222.22));

			String str = "2006-07-29 12:34";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
			transaction.setDate(dateTime);

			BankingAccountEntity account = new BankingAccountEntity();
			account.getBankTransactions().add(transaction);
			account.setIbanAccountNumber("FI123");
			account.setBalance(BigDecimal.valueOf(222.22));

			repository.save(account);
		};
	}
}
