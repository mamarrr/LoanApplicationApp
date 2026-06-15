package mamarr.loanapplicationapp.dal;

import mamarr.loanapplicationapp.domain.LoanApplication;

final class LoanApplicationMapper {
    private LoanApplicationMapper() {
    }

    static LoanApplication toDomain(LoanApplicationJpaEntity entity) {
        return new LoanApplication(
                entity.getId(),
                entity.getUser().getId(),
                entity.getAmount(),
                entity.getTermMonths(),
                entity.getMonthlyIncome(),
                entity.getMonthlyExpenses(),
                entity.getExistingLiabilities(),
                entity.getInterestRate(),
                entity.getMonthlyPayment(),
                entity.getAvailableMonthlyIncome(),
                entity.getDebtToIncomeRatio(),
                entity.getRiskLevel(),
                entity.getStatus(),
                entity.getPurpose(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static LoanApplicationJpaEntity toEntity(LoanApplication application, UserJpaEntity user) {
        LoanApplicationJpaEntity entity = new LoanApplicationJpaEntity();
        entity.setId(application.id());
        entity.setUser(user);
        entity.setAmount(application.amount());
        entity.setTermMonths(application.termMonths());
        entity.setMonthlyIncome(application.monthlyIncome());
        entity.setMonthlyExpenses(application.monthlyExpenses());
        entity.setExistingLiabilities(application.existingLiabilities());
        entity.setInterestRate(application.interestRate());
        entity.setMonthlyPayment(application.monthlyPayment());
        entity.setAvailableMonthlyIncome(application.availableMonthlyIncome());
        entity.setDebtToIncomeRatio(application.debtToIncomeRatio());
        entity.setRiskLevel(application.riskLevel());
        entity.setStatus(application.status());
        entity.setPurpose(application.purpose());
        entity.setCreatedAt(application.createdAt());
        entity.setUpdatedAt(application.updatedAt());
        return entity;
    }
}
