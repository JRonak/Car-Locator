package ronak.com.trial;

import android.content.Context;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by ronak on 05-05-2015.
 */
 
 
public class Track implements LocationListener{

    Context context;
    LocationManager locationManager;
    TextView textView;
    Location location;
    Geocoder geocoder;
    public Track(Context context, TextView textView)
    {
        this.context = context;
        this.textView = textView;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.geocoder = new Geocoder(context);
    }

    boolean gps_enabled()
    {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    boolean location_retrievable()
    {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,1,this);
        if(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!=null)
            return true;
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2000,1,this);
        if(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null)
            return true;
        stop_updates();
        return false;
    }

    void stop_updates()
    {
        locationManager.removeUpdates(this);
    }

    Location getLocation()
    {
        return location;
    }

    double get_longitude()
    {
        return location.getLongitude();
    }

    double get_latitude()
    {
        return location.getLatitude();
    }



    @Override
    public void onLocationChanged(Location location) {
            String address=" ";
        try {

            address = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getAddressLine(0)+" "+geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0).getAddressLine(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView.setText("Longitude: "+location.getLongitude()+"\nLatitude: "+location.getLatitude()+"\nAccuracy: "+location.getAccuracy()
            +"\n"+address);
            this.location=location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
