package Controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;

@WebServlet("/logoutServlet")
public class logoutServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the session object
        HttpSession session = request.getSession(false); // Don't create a new session if none exists

        // If there is an existing session, invalidate it (this removes the session
        // attributes)
        if (session != null) {
            session.invalidate(); // Invalidate the session, which will log the user out
        }
        response.sendRedirect("index.jsp");
    }
}
