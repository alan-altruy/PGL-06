package be.ac.umons.g06.server.model;

import be.ac.umons.g06.server.model.account.Account;
import be.ac.umons.g06.server.security.services.UserDetailsImpl;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public class Util {

    private static UserDetailsImpl getUserDetails(Principal principal) {
        return (UserDetailsImpl) ((Authentication) principal).getPrincipal();
    }
    public static boolean isPrincipalCustomer(Principal principal) {
        return getUserDetails(principal).getUser().getUsername().startsWith("CUSTOMER");
    }

    public static Customer getPrincipalAsCustomer(Principal principal) {
        return (Customer) getUserDetails(principal).getUser();
    }

    public static Bank getPrincipalAsBank(Principal principal) {
        return (Bank) getUserDetails(principal).getUser();
    }

    public static boolean checkPrincipalInvolvedInAccount(Account account, Principal principal) {
        if (isPrincipalCustomer(principal))
            return account.getOwnership().isCustomerInvolved(getPrincipalAsCustomer(principal));
        else
            return account.getBank().equals(getPrincipalAsBank(principal));
    }
}
