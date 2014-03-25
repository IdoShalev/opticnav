package opticnav.ardroid.ui;

import android.app.Activity;
import android.os.Bundle;
import opticnav.ardroid.Application;

public class LobbyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler().tryDisconnect(this);
    }
}
