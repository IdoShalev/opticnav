package opticnav.ardroid.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;

public class RegisterARDActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.activity_registerard);
        TextView confCodeView = (TextView)findViewById(R.id.confcode);
        confCodeView.setText(bundle.getString("confcode"));
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler().tryDisconnect(this);
    }
}
