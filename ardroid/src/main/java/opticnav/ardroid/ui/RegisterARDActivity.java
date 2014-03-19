package opticnav.ardroid.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
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
}
