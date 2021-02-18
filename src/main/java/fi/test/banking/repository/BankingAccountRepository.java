package fi.test.banking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.test.banking.entity.BankingAccountEntity;

public interface BankingAccountRepository extends JpaRepository<BankingAccountEntity, Long> {
	Optional<BankingAccountEntity> findByIbanAccountNumber(String ibanAccountNumber);
}
