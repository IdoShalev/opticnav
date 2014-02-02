package opticnav.web.test.opservlets;

import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import opticnav.web.OperationServlet;
import opticnav.web.ResponseObject;

@WebServlet("/Logout")
public class Logout extends OperationServlet {
	private static final long serialVersionUID = 1L;

    @Override
    public void operation(ResourceBundle text, HttpServletRequest req,
            ResponseObject response)
    {
        req.getSession().invalidate();
    }
}
