package com.abiolagbode.nigeriastateandlgacode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Spinner stateSpinner,lgSpinner;
    ArrayList<String> StateList, LGList, LGCodes;
    String lgCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StateList= new ArrayList<>();

        LGList = new ArrayList<>();

        stateSpinner = (Spinner) findViewById(R.id.customerState);

        lgSpinner = (Spinner) findViewById(R.id.customerLG);

        loadSpinnerData();

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String state= adapterView.getItemAtPosition(position).toString();
                loadLocalGovernmentSpinner(position);
//                Toast.makeText(getApplicationContext(),state,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String lg= adapterView.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(),lg,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadLocalGovernmentSpinner(int position) {

        LGList = new ArrayList<>();
        LGCodes = new ArrayList<>();

        String stateCode = String.valueOf(position+1);

        String feed = readJSONFromAsset();

//        System.out.println("Selected State>>>>>>>> "+stateCode);

        try {

            JSONObject jsonObject = new JSONObject(feed);

            JSONArray jsonArray = jsonObject.getJSONArray("States");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String lgas = jsonObject1.getString("stateCode");

//                System.out.println("LGAS>>>>>>>>>>>>>>> "+lgas);

                if(lgas.equals(stateCode)){

//                    System.out.println("The selected code is "+stateCode+" and the selected test is "+lgas);

                    JSONArray jsonArray2 = jsonObject1.getJSONArray("lgas");

                    final int numberOfItemsInResp = jsonArray2.length();


                    for (int j = 0; j < numberOfItemsInResp; j++) {

                        JSONObject jsonObject3 = jsonArray2.getJSONObject(j);

                        String lgs = jsonObject3.getString("lgaName");

                        lgCodes = jsonObject3.getString("lgaCode");

                        LGList.add(lgs);
                        LGCodes.add(lgCodes);


                    }

                }

            }
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, LGList);
            adapter.setDropDownViewResource(R.layout.spinner_center_item);
            lgSpinner.setAdapter(adapter);
//            System.out.println(LGList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadSpinnerData() {

        String feed = readJSONFromAsset();

        try {

            JSONObject jsonObject = new JSONObject(feed);

            JSONArray jsonArray = jsonObject.getJSONArray("States");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                String states = jsonObject1.getString("stateName");

                StateList.add(states);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, StateList);
            adapter.setDropDownViewResource(R.layout.spinner_center_item);
            stateSpinner.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJSONFromAsset() {
        String json = null;
        try{
            InputStream is = getAssets().open("lgastate.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }



}
