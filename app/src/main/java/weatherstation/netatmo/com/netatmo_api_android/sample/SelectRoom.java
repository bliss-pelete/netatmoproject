package weatherstation.netatmo.com.netatmo_api_android.sample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import weatherstation.netatmo.com.netatmo_api_android.R;
import weatherstation.netatmo.com.netatmo_api_android.api.NetatmoUtils;
import weatherstation.netatmo.com.netatmo_api_android.api.model.Module;
import weatherstation.netatmo.com.netatmo_api_android.api.model.Station;

public class SelectRoom extends AppCompatActivity {


    CustomAdapter mAdapter;
    List<Module> mListItems = new ArrayList<>();

    List<Station> mDevices;
    int mCompletedRequest;
    SampleHttpClient sampleHttpClient;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);
    }

    public void launchRoomView (View view) {

        Intent main = new Intent(this,MainActivity.class);
        startActivity(main);
       // initActionBar();

    }

    private void initActionBar(){
        mAdapter = new CustomAdapter(this, mListItems);
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(mAdapter);


        setSupportProgressBarIndeterminateVisibility(true);

        //final MainActivity activity = this;


        sampleHttpClient.getDevicesList(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject res = null;
                        try {
                            res = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (res != null) {
                            mDevices = NetatmoUtils.parseDevicesList(res);

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> stationName = new ArrayList<>();
                                    for (Station station : mDevices) {
                                        stationName.add(station.getName());
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                            getApplicationContext(),
                                            android.R.layout.simple_spinner_dropdown_item,
                                            stationName);

                                    ActionBar actionBar = getSupportActionBar();
                                    actionBar.setDisplayShowTitleEnabled(false);
                                    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                                 //   actionBar.setListNavigationCallbacks(adapter, activity);

                                }
                            });

                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setSupportProgressBarIndeterminateVisibility(false);
                            }
                        });



                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Tag", error.toString());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                setSupportProgressBarIndeterminateVisibility(false);
                            }
                        });
                    }
                });

    }
    }
