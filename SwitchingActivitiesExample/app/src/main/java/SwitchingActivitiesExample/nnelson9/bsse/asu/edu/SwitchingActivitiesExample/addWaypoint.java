package SwitchingActivitiesExample.nnelson9.bsse.asu.edu.SwitchingActivitiesExample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
//Copyright 2018 Nathan Nelson
//@author Nathan Nelson, Mailto:nnelson9@asu.edu
//@version Jan-16-2019

public class addWaypoint extends Activity{

    //information to be saved
    EditText placename;
    EditText placeDescription;
    EditText placeCategory;
    EditText placeAddressTitle;
    EditText placeFullAddress;
    EditText placeElevation;
    EditText placeLongitude;
    EditText placeLatitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this gets called first
        setContentView(R.layout.activity_add_waypoint);
        placename = findViewById(R.id.placeNameEditText);
        placeDescription = findViewById(R.id.placeDescriptionEditText);
        placeCategory = findViewById(R.id.PlaceCategoryEditText);
        placeAddressTitle = findViewById(R.id.PlaceAddressTitleEditText);
        placeFullAddress = findViewById(R.id.placeFullAddressEditText);
        placeElevation = findViewById(R.id.placeElevationEditText);
        placeLongitude = findViewById(R.id.placeLongitudeEditText);
        placeLatitude = findViewById(R.id.placeLatitudeEditText);




    }

    public void saveWaypoint(View v){
        //turns user entered info into correct json format
        if(placename==null||placeAddressTitle==null||placeCategory==null||placeDescription==null||placeFullAddress==null||placeElevation==null||placeLatitude==null||placeLongitude==null){
            Toast.makeText(getApplicationContext(), "Please Enter all fields!",Toast.LENGTH_LONG).show();
        }
        else {

            try {
                //adding json to library.
                JSONObject newplace = new JSONObject();
                newplace.put("address-title", placeAddressTitle.getText().toString());
                newplace.put("address-street",placeFullAddress.getText().toString());
                newplace.put("elevation",Double.parseDouble(placeElevation.getText().toString()));
                newplace.put("latitude",Double.parseDouble(placeLatitude.getText().toString()));
                newplace.put("longitude", Double.parseDouble(placeLongitude.getText().toString()));
                newplace.put("name",placename.getText().toString());
                newplace.put("description",placeDescription.getText().toString());
                newplace.put("category",placeCategory.getText().toString());
                //read old json
                Intent intent = new Intent();
                intent.putExtra("newJsonObj", newplace.toString());
                setResult(RESULT_OK, intent);

            }

            catch (JSONException e){
                e.printStackTrace();
            }
            catch(NumberFormatException e)
            {
                //not a double
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Please Enter a number for elevation, latitude, and longitude!",Toast.LENGTH_LONG).show();
            }

            //goes back to main activity
            //
            addWaypoint.this.finish();
        }
    }
}
