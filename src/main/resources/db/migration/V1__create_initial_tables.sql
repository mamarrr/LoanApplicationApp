CREATE TABLE app_users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE loan_applications (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    term_months INTEGER NOT NULL,
    monthly_income NUMERIC(12, 2) NOT NULL,
    monthly_expenses NUMERIC(12, 2) NOT NULL,
    existing_liabilities NUMERIC(12, 2) NOT NULL,
    interest_rate NUMERIC(8, 4) NOT NULL,
    monthly_payment NUMERIC(12, 2) NOT NULL,
    available_monthly_income NUMERIC(12, 2) NOT NULL,
    debt_to_income_ratio NUMERIC(6, 2) NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    purpose VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_loan_applications_user
        FOREIGN KEY (user_id)
        REFERENCES app_users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_app_users_email ON app_users(email);
CREATE INDEX idx_loan_applications_user_id ON loan_applications(user_id);
CREATE INDEX idx_loan_applications_status ON loan_applications(status);
