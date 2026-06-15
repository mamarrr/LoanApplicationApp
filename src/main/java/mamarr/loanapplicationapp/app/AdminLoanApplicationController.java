package mamarr.loanapplicationapp.app;

import jakarta.validation.Valid;
import mamarr.loanapplicationapp.app.dto.LoanApplicationResponse;
import mamarr.loanapplicationapp.app.dto.UpdateApplicationStatusRequest;
import mamarr.loanapplicationapp.bll.contracts.LoanApplicationService;
import mamarr.loanapplicationapp.domain.ApplicationStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/applications")
public class AdminLoanApplicationController {
    private final LoanApplicationService service;

    public AdminLoanApplicationController(LoanApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoanApplicationResponse> listAll(@RequestParam(required = false) String status) {
        return service.listAll(Optional.ofNullable(status).map(this::parseStatus)).stream().map(ApiMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public LoanApplicationResponse getById(@PathVariable UUID id) {
        return ApiMapper.toResponse(service.getByIdForAdmin(id));
    }

    @PatchMapping("/{id}/status")
    public LoanApplicationResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateApplicationStatusRequest request
    ) {
        return ApiMapper.toResponse(service.updateStatus(id, parseStatus(request.status())));
    }

    private ApplicationStatus parseStatus(String status) {
        try {
            return ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("status must be PENDING, APPROVED, or REJECTED");
        }
    }
}
