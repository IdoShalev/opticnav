package opticnav.web.opservlets;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.web.OperationServlet;
import opticnav.web.ResponseObject;
import opticnav.web.util.InputUtil;

@WebServlet("/op/RegisterARD")
public class RegisterARD extends OperationServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void operation(ResourceBundle text, HttpServletRequest req,
            ResponseObject response) throws IOException
    {
        String confirmationCode = req.getParameter("code");
        boolean successful = false;
        String message;
        
        if (InputUtil.isEntered(confirmationCode)) {
            if (HexCode.isStringCodeValid(confirmationCode, Protocol.AdminClient.CONFCODE_BYTES)) {
                HexCode code = new HexCode(confirmationCode);
                
                try (AdminConnection b = getARDdAdminPool().getAdminBroker()) {
                    successful = b.registerARDWithConfCode(code) != 0;
                    
                    if (successful) {
                        message = "registerard.successful";
                    } else {
                        message = "registerard.nomatch";
                    }
                }
            } else {
                message = "registerard.badcode";
            }
        } else {
            message = "registerard.nocode";
        }
        
        response.addBoolean("successful", successful);
        response.addString("message", text.getString(message));
    }

}
