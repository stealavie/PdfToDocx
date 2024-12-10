package Controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Models.BO.AccountBO;
import Models.Bean.Account;

import java.io.IOException;

@WebServlet("/signupServlet")
public class signupServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AccountBO accountBO;

    @Override
    public void init() throws ServletException {
        super.init();
        accountBO = new AccountBO(); // Create the BO instance
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/JSP/Authentication/signup.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Create the new Account object
        Account account = new Account();
        account.setId(accountBO.getRecordLength() + 1);
        account.setUsername(username);
        account.setPassword(password); // In a real-world app, hash the password here

        // Save the account to the database
        boolean isCreated = accountBO.createAccount(account);

        if (isCreated) {
            // Account created successfully, redirect to login page
            response.sendRedirect("loginServlet");
        } else {
            // Account creation failed, set error message
            request.setAttribute("errorMessage",
                    "There was an error creating your account. Please try again." + username + password);
            request.getRequestDispatcher("/JSP/Authentication/signup.jsp").forward(request, response);
        }
    }
}
