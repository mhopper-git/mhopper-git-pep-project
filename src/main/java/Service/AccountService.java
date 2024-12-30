package Service;
import DAO.AccountDAO;
import Model.Account;
import java.util.*;

public class AccountService {
    
    private AccountDAO accountDAO;
    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public Account addAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        boolean validUsernameAndPassword = (username != null && username != "" && password.length() >= 4);
        boolean uniqueUsername = true;

        if (validUsernameAndPassword) {
            for (Account accountFromDB : accountDAO.getAllAccounts()) {
                if (username.equals(accountFromDB.getUsername())) {
                    uniqueUsername = false;
                }
                
            }

            if (uniqueUsername) {
                return accountDAO.insertAccount(account);
            }
        }

        return null;
    }
}
