package mamarr.loanapplicationapp.dal;

import mamarr.loanapplicationapp.domain.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface LoanApplicationSpringDataRepository extends JpaRepository<LoanApplicationJpaEntity, UUID> {
    List<LoanApplicationJpaEntity> findByUserId(UUID userId);

    Optional<LoanApplicationJpaEntity> findByIdAndUserId(UUID id, UUID userId);

    List<LoanApplicationJpaEntity> findByStatus(ApplicationStatus status);
}
