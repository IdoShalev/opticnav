package opticnav.web.opservlets;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import opticnav.web.OperationServlet;
import opticnav.web.ResponseObject;

@WebServlet("/op/ARDInfo")
public class ARDInfo extends OperationServlet {
	private static final long serialVersionUID = 1L;

    @Override
    public void operation(ResourceBundle text, HttpServletRequest req,
            ResponseObject response) {
        int id = 42;
        String name = "Tacocat";
        response.addInteger("id", id);
        response.addString("name", name);
    }
}
