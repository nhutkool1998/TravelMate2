package cs486.nmnhut.travelmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;




import static cs486.nmnhut.travelmate.LoginActivity.db;

public class LocationHelper {
    public final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 100;
    private static LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private static LocationCallback mLocationCallback;
    private static String Username = "";
    LocationHelper(Activity activity, String username) {
        this.Username = username;
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60);
        locationRequest.setFastestInterval(300);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(((Context) activity));

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    String s = "location/" + LocationHelper.Username;
                    DatabaseReference ref = db.getReference(s);
                    ref.child("Lat").setValue(location.getLatitude());
                    ref.child("Lng").setValue(location.getLongitude());
                }
            }


        };
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

        }


    }
    @SuppressLint("MissingPermission")
    public void requestLocationUpdate()
    {
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, null);

    }

    public String getUsername() {
        return Username;
    }
    public void stopLoacationUpdates()
    {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
}
