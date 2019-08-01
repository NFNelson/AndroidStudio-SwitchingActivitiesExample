package SwitchingActivitiesExample.nnelson9.bsse.asu.edu.SwitchingActivitiesExample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;


public class placeActivity extends Activity {
    EditText AddressTitle,AddressStreet,Elevation,Latitude,Longitude,name,description,category;
    JSONObject toModify = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_activity);
        //setting up edit text fields
        AddressTitle = (EditText)findViewById(R.id.editTextAddressTitle);
        AddressStreet = (EditText)findViewById(R.id.editTextaddressStreet);
        Elevation = (EditText)findViewById(R.id.editTextElevation);
        Latitude =(EditText)findViewById(R.id.editTextlatitude);
        Longitude=(EditText)findViewById(R.id.editTextlongitude);
        name=(EditText)findViewById(R.id.editTextName);
        description=(EditText)findViewById(R.id.editTextDiscription);
        category = (EditText)findViewById(R.id.editTextCategory);

        //get bundle that was sent
        Bundle bundle = getIntent().getExtras();
        //set text fields
         AddressTitle.setText(bundle.getString("address-title"));
         AddressStreet.setText(bundle.getString("address-street"));
         Elevation.setText(Double.toString(bundle.getDouble("elevation")));
         Latitude.setText(Double.toString(bundle.getDouble("latitude")));
         Longitude.setText(Double.toString(bundle.getDouble("longitude")));
         name.setText(bundle.getString("name"));
         description.setText(bundle.getString("description"));
         category.setText(bundle.getString("category"));


    }

    public void deleteWaypoint(View v){

        Intent intent = new Intent();
        intent.putExtra("Delete", "yes");
        intent.putExtra("place",name.getText());
        setResult(RESULT_OK, intent);
        placeActivity.this.finish();


    }
    public void saveChanges(View v){
        Intent intent = new Intent();
        intent.putExtra("Delete", "no");
        intent.putExtra("place",AddressTitle.getText().toString());
        try {
            toModify.put("address-title", AddressTitle.getText().toString());
            toModify.put("address-street",AddressStreet.getText().toString());
            toModify.put("elevation",Double.parseDouble(Elevation.getText().toString()));
            toModify.put("latitude",Double.parseDouble(Latitude.getText().toString()));
            toModify.put("longitude", Double.parseDouble(Longitude.getText().toString()));
            toModify.put("name",name.getText().toString());
            toModify.put("description",description.getText().toString());
            toModify.put("category",category.getText().toString());
            intent.putExtra("json",toModify.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setResult(RESULT_OK, intent);
        placeActivity.this.finish();

    }

}
