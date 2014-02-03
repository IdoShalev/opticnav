package opticnav.web.opservlets;

import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import opticnav.web.OperationServlet;
import opticnav.web.ResponseObject;
import opticnav.web.util.InputUtil;

@WebServlet("/op/RegisterARD")
public class RegisterARD extends OperationServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void operation(ResourceBundle text, HttpServletRequest req,
            ResponseObject response)
    {
        String confirmationCode = req.getParameter("code");
        boolean successful = false;
        String message;
        
        if (InputUtil.isEntered(confirmationCode)) {
            successful = confirmationCode.equalsIgnoreCase("wXXyZ");
            
            if (successful) {
                message = "registerard.successful";
            } else {
                message = "registerard.nomatch";
            }
        } else {
            message = "registerard.nocode";
        }
        
        response.addBoolean("successful", successful);
        response.addString("message", text.getString(message));
    }

}
