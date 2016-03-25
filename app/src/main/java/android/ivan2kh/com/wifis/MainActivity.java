package android.ivan2kh.com.wifis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv;
    TextView topic;

    WifiManager wifi;
    WifiScanReceiver wifiReciever;

    WifiManager.WifiLock wifiLock;

    long time = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        lv=(ListView)findViewById(R.id.listView);
        topic=(TextView)findViewById(R.id.textview);

        //Start scan
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiLock = wifi.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY, "Set wifi on for measurements");

        wifiReciever = new WifiScanReceiver();
//        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifi.startScan();
    }

    String wifis[];
    private class WifiScanReceiver extends BroadcastReceiver{
        public void onReceive(Context c, Intent intent) {
            long time2 = System.currentTimeMillis();


            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifi.startScan();

            wifis = new String[wifiScanList.size()];

            for(int i = 0; i < wifiScanList.size(); i++){
                ScanResult res = wifiScanList.get(i);
                wifis[i] = String.format("%d %s", res.level, res.SSID);
                //wifis[i] = ((wifiScanList.get(i)).toString());
            }
            topic.setText(String.format("Wifis %d %d", wifiScanList.size(), time2-time));
            time=time2;
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, R.id.comment, wifis));
        }
    }

    protected void onPause() {
        wifiLock.release();
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        wifiLock.acquire();
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

//    protected void fillTable() {
//        if(grid == null)
//            return;
//
//        TableRow tr = new TableRow(this);
//        tr.setLayoutParams(new LayoutParams(
//                LayoutParams.FILL_PARENT,
//                LayoutParams.WRAP_CONTENT));
//
//        TextView nodeName = new TextView(this);
//        nodeName.setText("hz");
//        nodeName.setTextColor(Color.RED);
//        nodeName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        nodeName.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        nodeName.setPadding(5, 5, 5, 5);
//        tr.addView(nodeName);  // Adding textView to tablerow.
//
//        TextView rssi = new TextView(this);
//        rssi.setText("-90");
//        rssi.setTextColor(Color.GREEN);
//        rssi.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        rssi.setPadding(5, 5, 5, 5);
//        rssi.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
//        tr.addView(rssi); // Adding textView to tablerow.
//
//        grid.addView(tr, new TableLayout.LayoutParams(
//                LayoutParams.FILL_PARENT,
//                LayoutParams.WRAP_CONTENT));
//    }

}
