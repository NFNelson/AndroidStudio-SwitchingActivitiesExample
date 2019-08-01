package SwitchingActivitiesExample.nnelson9.bsse.asu.edu.SwitchingActivitiesExample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//Copyright 2018 Nathan Nelson

//@author Nathan Nelson, Mailto:nnelson9@asu.edu
//@version Jan-16-2019s

public class MainActivity extends AppCompatActivity {

    private Spinner firstSpinner,secondSpinner;
    private ListView placesListview;
    private EditText Bearing,Distance;
    private ArrayAdapter<String> listAdapter ;
    JSONArray Places = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declaring and setting all objects
        firstSpinner = (Spinner)findViewById(R.id.firstSpinner);
        secondSpinner =(Spinner)findViewById(R.id.SecondSpinner);
        placesListview =(ListView)findViewById(R.id.ListView);
        Bearing = (EditText)findViewById(R.id.bearing);
        Distance = (EditText)findViewById(R.id.distance);
        String jsonstring;



        try {
            InputStream is = getAssets().open("places.json");
            //InputStream is = this.getApplicationContext().getResources().openRawResource(R.raw.places);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            //create json object
            jsonstring = new String(buffer,"UTF-8");
            Places= new JSONArray(jsonstring);
            //Places.put(JSONarrayplaces);

            ArrayList<String> placesList = new ArrayList<String>();

            for(int i =0;i<Places.length();i++){
                JSONObject obj = Places.getJSONObject(i);
                placesList.add(obj.getString("name"));
            }
            listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row,R.id.textlistitem, placesList);
            ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_row,R.id.spinneritem, placesList);

            placesListview.setAdapter(listAdapter);
            firstSpinner.setAdapter(spinnerAdapter);
            secondSpinner.setAdapter(spinnerAdapter);
            //answer.setText("hey");
            placesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //loop through posistion

                    for(int i = 0;i<listAdapter.getCount();i++){
                        if(position==i){
                            try {
                                Intent placeIntent = new Intent(view.getContext(), placeActivity.class);
                                JSONObject tosend = Places.getJSONObject(i);
                                Bundle placeBundle = new Bundle();
                                placeBundle.putString("address-title",tosend.getString("address-title"));
                                placeBundle.putString("address-street",tosend.getString("address-street"));
                                placeBundle.putDouble("elevation",tosend.getDouble("elevation"));
                                placeBundle.putDouble("latitude",tosend.getDouble("latitude"));
                                placeBundle.putDouble("longitude",tosend.getDouble("longitude"));
                                placeBundle.putString("name",tosend.getString("name"));
                                placeBundle.putString("description",tosend.getString("description"));
                                placeBundle.putString("category",tosend.getString("category"));
                                placeIntent.putExtras(placeBundle);

                                startActivityForResult(placeIntent, 2);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }


                        }
                    }
                }
            });

        }
        catch (IOException ex){
            android.util.Log.w(this.getClass().getSimpleName(),"exception reading places.json");

        }
        catch (JSONException e){
            e.printStackTrace();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu,menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.addbutton:
                Intent intent = new Intent(MainActivity.this, addWaypoint.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("newJsonObj");
                try {
                    JSONObject newPlaceFromIntent = new JSONObject(strEditText);
                    listAdapter.add(newPlaceFromIntent.getString("name"));
                    Places.put(newPlaceFromIntent);
                    placesListview.setAdapter(listAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode==2){
            if(resultCode==RESULT_OK){
                String action= data.getStringExtra("Delete");


                if(action=="yes"){
                    String toDelete = data.getStringExtra("place");
                    for(int i = 0;i<Places.length();i++){
                        try {
                            JSONObject toremove = Places.getJSONObject(i);
                            if(toremove.getString("name").equals(toDelete)){
                                Places = (JSONArray) Places.remove(i);
                                listAdapter.remove(toDelete);
                                placesListview.setAdapter(listAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
                else if(action=="no"){
                    String toDelete = data.getStringExtra("place");
                    for(int i = 0;i<Places.length();i++){
                        try {
                            JSONObject toremove = Places.getJSONObject(i);
                            if(toremove.getString("name").equals(toDelete)){
                                Places = (JSONArray) Places.remove(i);
                                listAdapter.remove(toDelete);
                                listAdapter.notifyDataSetChanged();
                                placesListview.setAdapter(listAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    String strEditText = data.getStringExtra("json");
                    try {
                        JSONObject editedPlaceFromIntent = new JSONObject(strEditText);
                        listAdapter.add(editedPlaceFromIntent.getString("name"));
                        Places.put(editedPlaceFromIntent);
                        placesListview.setAdapter(listAdapter);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

        }

    }


    public void CalcDistance(View v){
        //get lat an lon from both spinners calc the distance between them
        String firstpointname = firstSpinner.getSelectedItem().toString();
        String secondpointname = secondSpinner.getSelectedItem().toString();
        Double Lat1=0.0,Lon1=0.0,Ele1=0.0,Lat2=0.0,Lon2=0.0,Ele2=0.0;

        JSONObject point = new JSONObject();
        for(int i=0;i<Places.length();i++){
            try {
                point = Places.getJSONObject(i);
                if (point.getString("name").equals(firstpointname)){
                    Lat1 =point.getDouble("latitude");
                    Lon1 = point.getDouble("longitude");
                    Ele1 = point.getDouble("elevation");
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        for(int i=0;i<Places.length();i++){
            try {
                point = Places.getJSONObject(i);
                if (point.getString("name").equals(secondpointname)){
                    Lat2 =point.getDouble("latitude");
                    Lon2 = point.getDouble("longitude");
                    Ele2 = point.getDouble("elevation");
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        double lonDifference= Math.toRadians(Lon2-Lon1);
        double y= Math.sin(lonDifference)*Math.cos(Lat2);
        double x=Math.cos(Lat1)*Math.sin(Lat2)-Math.sin(Lat1)*Math.cos(Lat2)*Math.cos(lonDifference);
        double bearing = Math.toDegrees(Math.atan2(y, x))+360;
        double remainder = bearing% 360;
        Bearing.setText(Double.toString(remainder));

        final int R = 6371;
        double latDifference = Math.toRadians(Lat2-Lat1);
        double a = Math.sin(latDifference / 2) * Math.sin(latDifference / 2)+ Math.cos(Math.toRadians(Lat1)) * Math.cos(Math.toRadians(Lat2)) * Math.sin(lonDifference / 2) * Math.sin(lonDifference / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;
        double height = Ele1 - Ele2;
        distance = Math.pow(distance, 2) + Math.pow(height, 2);
        distance= Math.sqrt(distance);
        Distance.setText(Double.toString(distance));

        /*
        final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);
    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = el1 - el2;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
         */










    }
}
