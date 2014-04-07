package opticnav.ardroid.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import opticnav.ardd.ard.InstanceInfo;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;
import opticnav.ardroid.connection.ServerUIHandler;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        final ListView listView = (ListView)findViewById(R.id.instances_listview);
        final View loadingView = findViewById(R.id.instances_loading);
        final View noInstancesView = findViewById(R.id.instance_none);

        Application.getInstance().getServerUIHandler().listInstances(new ServerUIHandler.ListInstancesEvent() {
            @Override
            public void listInstances(List<InstanceInfo> instancesList) {
                loadingView.setVisibility(View.INVISIBLE);
                if (instancesList.isEmpty()) {
                    noInstancesView.setVisibility(View.VISIBLE);
                } else {
                    listView.setAdapter(new MySimpleArrayAdapter(LobbyActivity.this, instancesList));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            listView.setEnabled(false);

                            final InstanceInfo info = (InstanceInfo)adapterView.getItemAtPosition(i);
                            Application.getInstance().getServerUIHandler().joinInstance(info.getId());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler().tryDisconnect(this);
    }

    private static String timestampToHumanTime(long timestamp_ms) {
        final long now = System.currentTimeMillis();
        final long elapsed = now - timestamp_ms;

        if (elapsed < 5*1000) {
            // less than 5 seconds ago
            return "just now";
        } else if (elapsed < 60*1000) {
            // less than a minute ago
            return String.format("%d seconds ago", elapsed/1000);
        } else if (elapsed < 60*60*1000) {
            // less than an hour ago
            final long minutes = elapsed/(60*1000);
            if (minutes == 1) {
                return "a minute ago";
            } else {
                return String.format("%d minutes ago", minutes);
            }
        } else {
            // who cares, count them in hours
            final long hours = elapsed/(60*60*1000);
            if (hours == 1) {
                return "an hour ago";
            } else {
                return String.format("%d hours ago", hours);
            }
        }
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<InstanceInfo> {
        private final Context context;
        private final List<InstanceInfo> values;

        public MySimpleArrayAdapter(Context context, List<InstanceInfo> values) {
            super(context, R.layout.instance_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final InstanceInfo info = values.get(position);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.instance_list_item, parent, false);

            final TextView name, numberConnected, startTime;
            name = (TextView)rowView.findViewById(R.id.instance_list_item_name);
            numberConnected = (TextView)rowView.findViewById(R.id.instance_list_item_connected);
            startTime = (TextView)rowView.findViewById(R.id.instance_list_item_startdate);

            name.setText(info.getName());
            numberConnected.setText(Integer.toString(info.getNumberConnected()));
            startTime.setText(timestampToHumanTime(info.getStartTime()));

            return rowView;
        }
    }
}
