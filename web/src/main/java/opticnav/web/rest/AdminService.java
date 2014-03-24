package opticnav.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminService extends Controller {
    private static class AccountPOJO {
        public final int id;
        public final String name;
        public final String passCode;
        public final boolean live;
        
        public AccountPOJO(int id, String name, String passCode, boolean live) {
            this.id = id;
            this.name = name;
            this.passCode = passCode;
            this.live = live;
        }
    }
    
    @RequestMapping(value="/accounts", method=RequestMethod.GET)
    public List<AccountPOJO> accounts() {
        // TODO
        // Query a list of accounts, including name, account_id and ard_id
        // Cross-check the ARD id with the AdminBroker to check if the ARD is live
        
        List<AccountPOJO> list = new ArrayList<>();
        
        list.add(new AccountPOJO(1, "Bobby", null, false));
        list.add(new AccountPOJO(2, "Karl", "5488C798AF1D95E0240F17F939768C0E", true));
        return list;
    }
}
