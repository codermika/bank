package fi.test.banking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import fi.test.banking.BankingApplication;

@SpringJUnitConfig(BankingApplication.class)
@SpringBootTest
class BankingApplicationTests {

	@Test
	void contextLoads() {
	}

}
