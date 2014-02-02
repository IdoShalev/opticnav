package opticnav.web.test.opservlets;

import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import opticnav.web.OperationServlet;
import opticnav.web.ResponseObject;
import opticnav.web.test.beans.User;
import opticnav.web.util.InputUtil;

@WebServlet("/Login")
public class Login extends OperationServlet {
	private static final long serialVersionUID = 1L;

    @Override
    public void operation(ResourceBundle lang, HttpServletRequest req, ResponseObject response) {
        String user = req.getParameter("user");
        String pass = req.getParameter("pass");
        
        if (InputUtil.isEntered(user, pass)) {
            if (user.equals("admin") && pass.equals("password")) {
                req.getSession().setAttribute("user", new User(user, 22));
                response.addBoolean("successful", true);
                response.addString("message", lang.getString("successful"));
            } else {
                response.addBoolean("successful", false);
                response.addString("message", lang.getString("login.invalid"));
            }
        } else {
            response.addBoolean("successful", false);
            response.addString("message", lang.getString("login.missing"));
        }
    }
}
