package mamarr.loanapplicationapp.app;

import jakarta.validation.Valid;
import mamarr.loanapplicationapp.app.dto.CreateLoanApplicationRequest;
import mamarr.loanapplicationapp.app.dto.LoanApplicationResponse;
import mamarr.loanapplicationapp.bll.CreateLoanApplicationCommand;
import mamarr.loanapplicationapp.bll.contracts.ILoanApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {
    private final ILoanApplicationService service;

    public LoanApplicationController(ILoanApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplicationResponse create(@Valid @RequestBody CreateLoanApplicationRequest request) {
        return ApiMapper.toResponse(service.createForCurrentUser(new CreateLoanApplicationCommand(
                request.amount(),
                request.termMonths(),
                request.monthlyIncome(),
                request.monthlyExpenses(),
                request.existingLiabilities(),
                request.purpose()
        )));
    }

    @GetMapping
    public List<LoanApplicationResponse> listMine() {
        return service.listForCurrentUser().stream().map(ApiMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public LoanApplicationResponse getMine(@PathVariable UUID id) {
        return ApiMapper.toResponse(service.getForCurrentUser(id));
    }
}
