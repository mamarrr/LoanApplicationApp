package mamarr.loanapplicationapp.dal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import mamarr.loanapplicationapp.domain.RiskLevel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "loan_applications")
public class LoanApplicationJpaEntity {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "term_months", nullable = false)
    private int termMonths;

    @Column(name = "monthly_income", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyIncome;

    @Column(name = "monthly_expenses", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyExpenses;

    @Column(name = "existing_liabilities", nullable = false, precision = 12, scale = 2)
    private BigDecimal existingLiabilities;

    @Column(name = "interest_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "monthly_payment", nullable = false, precision = 12, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(name = "available_monthly_income", nullable = false, precision = 12, scale = 2)
    private BigDecimal availableMonthlyIncome;

    @Column(name = "debt_to_income_ratio", nullable = false, precision = 6, scale = 2)
    private BigDecimal debtToIncomeRatio;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    private String purpose;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected LoanApplicationJpaEntity() {
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

    public UserJpaEntity getUser() { return user; }

    public void setUser(UserJpaEntity user) { this.user = user; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public int getTermMonths() { return termMonths; }

    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }

    public BigDecimal getMonthlyIncome() { return monthlyIncome; }

    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }

    public BigDecimal getMonthlyExpenses() { return monthlyExpenses; }

    public void setMonthlyExpenses(BigDecimal monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }

    public BigDecimal getExistingLiabilities() { return existingLiabilities; }

    public void setExistingLiabilities(BigDecimal existingLiabilities) { this.existingLiabilities = existingLiabilities; }

    public BigDecimal getInterestRate() { return interestRate; }

    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }

    public BigDecimal getMonthlyPayment() { return monthlyPayment; }

    public void setMonthlyPayment(BigDecimal monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public BigDecimal getAvailableMonthlyIncome() { return availableMonthlyIncome; }

    public void setAvailableMonthlyIncome(BigDecimal availableMonthlyIncome) { this.availableMonthlyIncome = availableMonthlyIncome; }

    public BigDecimal getDebtToIncomeRatio() { return debtToIncomeRatio; }

    public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) { this.debtToIncomeRatio = debtToIncomeRatio; }

    public RiskLevel getRiskLevel() { return riskLevel; }

    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public ApplicationStatus getStatus() { return status; }

    public void setStatus(ApplicationStatus status) { this.status = status; }

    public String getPurpose() { return purpose; }

    public void setPurpose(String purpose) { this.purpose = purpose; }

    public Instant getCreatedAt() { return createdAt; }

    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
