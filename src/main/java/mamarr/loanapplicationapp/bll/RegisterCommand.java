package mamarr.loanapplicationapp.bll;

public record RegisterCommand(String email, String password, String firstName, String lastName) {
}
