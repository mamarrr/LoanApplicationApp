package mamarr.loanapplicationapp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "mamarr.loanapplicationapp")
public class LoanApplicationAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplicationAppApplication.class, args);
    }
}
