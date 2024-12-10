package Models.BO;

import Models.Bean.Account;
import Models.DAO.AccountDAO;

public class AccountBO {

    private AccountDAO accountDAO;

    public AccountBO() {
        accountDAO = new AccountDAO();
    }

    public Account authenticateUser(String username, String password) {
        return accountDAO.authenticateUser(username, password);
    }

    public boolean createAccount(Account account) {
        return accountDAO.createAccount(account);
    }

    public int getRecordLength() {
        return accountDAO.getLength();
    }
}
