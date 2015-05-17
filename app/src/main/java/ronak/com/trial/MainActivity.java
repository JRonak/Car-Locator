package ronak.com.trial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    TextView textView,textView1;
    Button button;
    Track track;
    SharedPreferences sharedPreferences;
    boolean location_saved=false;
    private final String FILENAME = "Myprefs";
    private final String SAVED = "saved";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "longitude";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        textView = (TextView) findViewById(R.id.gps_tv);
        textView1 = (TextView) findViewById(R.id.parked_tv);
        button = (Button) findViewById(R.id.button);
        sharedPreferences = getSharedPreferences(FILENAME,MODE_PRIVATE);
        location_saved = sharedPreferences.getBoolean(SAVED,false);
        if(!location_saved)
            button.setText("Save Location");
        else
        {
            textView1.setText("Last stored Location\nLongitude: "+sharedPreferences.getString(LONGITUDE,"Unknown")+"\nLatitude: "+
            sharedPreferences.getString(LATITUDE,"Unknown"));
            button.setText("Navigate");
        }
        track = new Track(this, textView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!track.gps_enabled())
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        if(!track.location_retrievable())
            Toast.makeText(this,"Location could not be retrieved",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        track.stop_updates();
    }

    public void clicked(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences(FILENAME,MODE_PRIVATE).edit();
        if(!location_saved)
        {
            double latitude = track.get_latitude();
            double longitude = track.get_longitude();
            if(latitude!=0&&longitude!=0)
            {
                editor.putString(LATITUDE,String.valueOf(latitude));
                editor.putString(LONGITUDE, String.valueOf(longitude));
                editor.putBoolean(SAVED, true);
                editor.apply();
                textView1.setText("Last stored Location\nLongitude: " + sharedPreferences.getString(LONGITUDE, "Unknown") + "\nLatitude: " +
                        sharedPreferences.getString(LATITUDE, "Unknown"));
                location_saved = true;
                button.setText("Navigate");
            }
        }
        else
        {
            textView1.setText(" ");
            editor.putBoolean(SAVED, false);
            editor.apply();
            button.setText("Save Location");
            String uri = "http://maps.google.com/maps?q=loc:"+sharedPreferences.getString(LATITUDE,null)+","+sharedPreferences.getString(LONGITUDE,null);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
            location_saved=false;
        }
    }

}
