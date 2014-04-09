package opticnav.ardroid.ui;

import android.os.Bundle;
import android.widget.TextView;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_confcode)
public class ConfCodeActivity extends RoboActivity {
    @InjectView(R.id.confcode)
    private TextView confCodeView;

    @InjectExtra("confcode")
    private String confCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confCodeView.setText(confCode);
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler().tryDisconnect(this);
    }
}
