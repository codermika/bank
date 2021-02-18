package fi.test.banking.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.test.banking.BankingApplication;
import fi.test.banking.controller.BankingController;
import fi.test.banking.domain.BankingTransaction;
import fi.test.banking.domain.BankingTransactionAmount;
import fi.test.banking.service.BankingService;
import fi.test.banking.util.BankingInputChecker;

@SpringJUnitConfig(BankingApplication.class)
@WebMvcTest(BankingController.class)
class BankingControllerTest {
	private static final BigDecimal TEST_AMOUNT = BigDecimal.valueOf(111.11);

	private static final String TEST_IBAN = "FI123";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BankingService service;

	@MockBean
	private BankingInputChecker inputChecker;

	@Value("classpath:expected_response_get_statement.json")
	private Resource expectedGetStamentResponse;

	@Test
	void testGetStatement() throws Exception {
		initializeTest();

		mvc.perform(get("/api/accounts/" + TEST_IBAN + "/statement").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(string(expectedGetStamentResponse)));
	}

	@Test
	void testDeposit() throws Exception {
		doDepositOrWithdrawTest("deposit", TEST_AMOUNT);
	}

	@Test
	void testWithdraw() throws Exception {
		doDepositOrWithdrawTest("withdraw", TEST_AMOUNT.negate());
	}

	private void doDepositOrWithdrawTest(String operation, BigDecimal expectedSaveTransactionValue)
			throws Exception, JsonProcessingException {
		BankingTransactionAmount amount = new BankingTransactionAmount();
		amount.setAmount(TEST_AMOUNT);

		mvc.perform(post("/api/accounts/" + TEST_IBAN + "/" + operation)
				.content(new ObjectMapper().writeValueAsString(amount)).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());

		verify(inputChecker).checkTransactionAmount(TEST_AMOUNT);
		verify(service).saveTransaction(TEST_IBAN, expectedSaveTransactionValue);
	}

	private void initializeTest() {
		List<BankingTransaction> transactions = new ArrayList<>();
		BankingTransaction transaction = new BankingTransaction();
		transactions.add(transaction);
		transaction.setDate(createTestDateTime());
		transaction.setAmount(TEST_AMOUNT);
		transaction.setBalance(BigDecimal.valueOf(222.22));
		when(service.getBankingTransactions(TEST_IBAN)).thenReturn(transactions);
	}

	private static LocalDateTime createTestDateTime() {
		String str = "2020-04-08 12:30";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
		return dateTime;
	}

	private String string(Resource res) throws Exception {
		return StreamUtils.copyToString(res.getInputStream(), Charset.forName("UTF-8"));
	}
}
