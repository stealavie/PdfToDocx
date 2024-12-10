package Controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import Models.BO.AccountBO;
import Models.Bean.Account;

import java.io.IOException;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

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
        // If the user is already logged in, redirect to home page
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/JSP/pdfToDocx/Nhap_du_lieu.jsp");
        } else {
            request.getRequestDispatcher("/JSP/Authentication/login.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Account account = accountBO.authenticateUser(username, password);

        if (account != null) {
            HttpSession session = request.getSession();
            session.setAttribute("account", account);
            response.sendRedirect(request.getContextPath() + "/JSP/pdfToDocx/Nhap_du_lieu.jsp");
        } else {
            response.getWriter().println("Invalid credentials.");
        }
    }
}
