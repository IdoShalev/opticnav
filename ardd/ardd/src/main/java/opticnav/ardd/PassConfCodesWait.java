package opticnav.ardd;

public class PassConfCodesWait {
    private PassConfCodes passConfCodes;

    public PassConfCodesWait(PassConfCodes passConfCodes) {
        this.passConfCodes = passConfCodes;
    }

    public PassConfCodes getPassConfCodes() {
        return passConfCodes;
    }
    
    public void notifyResult(int result) {
        // TODO
    }

    public int waitForResult() throws InterruptedException {
        // TODO
        return 0;
    }
}
