package opticnav.ardroid.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import opticnav.ardroid.Application;
import opticnav.ardroid.R;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        final ListView listView = (ListView)findViewById(R.id.instances_listview);

        final List<String> list = new ArrayList<String>();
        list.add("Test");
        list.add("Hello");

        listView.setAdapter(new MySimpleArrayAdapter(this, list));
    }

    @Override
    public void onBackPressed() {
        Application.getInstance().getServerUIHandler().tryDisconnect(this);
    }

    private class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final List<String> values;

        public MySimpleArrayAdapter(Context context, List<String> values) {
            super(context, R.layout.instance_list_item, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.instance_list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.instance_list_item_name);
            textView.setText(values.get(position));

            return rowView;
        }
    }
}
