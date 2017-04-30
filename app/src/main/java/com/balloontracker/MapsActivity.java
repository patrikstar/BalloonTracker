package com.balloontracker;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;



public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    GoogleMap mMap;
    LocationManager locationManager;
    FloatingActionButton fab, fab1, search, save, stats;
    Marker mMarker;
    Context context;
    @Nullable
    Bundle bundle;

    double oldlat = 0;
    double oldlng = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = new Intent(this, MyService.class);
        double[] destination = {50.4662615,30.5208362};
        intent.putExtra("destination", destination);
        startService(intent);
//        PowerManager mgr = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakeLock");
//        wakeLock.acquire();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkGPS();
        loadmap();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        search = (FloatingActionButton) findViewById(R.id.search);
        save = (FloatingActionButton) findViewById(R.id.save);
        stats = (FloatingActionButton) findViewById(R.id.stats);

//        file = new File("geo.txt");
//        Log.e("NULL", "File exist: " + file.exists());



    }

    private void loadmap() {
        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } catch (NullPointerException exception) {
            Log.e("navigation", exception.toString());
        }

    }

    private void checkGPS() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    finish();
                }
            });
            builder.create().show();
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        Log.wtf("NULL : ", "onMapReady" + map);
        mMap.setOnMapLoadedCallback(this);
        setUpMap();


    }


    private void init() {

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // Create new fragment and transaction
//                Fragment newFragment = new chartsFragment();
//                // consider using Java coding conventions (upper first char class names!!!)
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                // Replace whatever is in the fragment_container view with this fragment,
//                // and add the transaction to the back stack
//                transaction.replace(R.id.stats_fragment, newFragment);
//                transaction.addToBackStack(null);
//
//                // Commit the transaction
//                transaction.commit();
//                Bundle bundle = new Bundle();
//                bundle.putString("dataKey", "Value");
//                StatsFragment fragmentObject = new StatsFragment();
//                fragmentObject .setArguments(bundle);
//                if(fragmentObject!=null){
//                    FragmentManager fm = getFragmentManager();
//                    fm.beginTransaction().add(R.id.container, fragmentObject).commit();
//                }
                if (mMap!=null) {
//                    bundle.putString("lat", "suka");
//                    bundle.putString("lng", String.valueOf(mMap.getMyLocation().getLongitude()));
//                    bundle.putString("alt", String.valueOf(mMap.getMyLocation().getAltitude()));
                }
//                    if (bundle!= null){
                StatsFragment fragment =  new StatsFragment();
//                fragment.setArguments(bundle);
                if (fragment != null) {
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.map, fragment).addToBackStack("B").attach(fragment).commit();
//                }
            }}
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker mMarker) {
                mMarker.setVisible(false);
                Log.d("", mMarker.getTitle());
            }
        });

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                oldlat = location.getLatitude();
//                oldlng = location.getLongitude();
                if (oldlat != 0 || oldlng != 0) {
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(oldlat, oldlng), new LatLng(location.getLatitude(), location.getLongitude()))
                            .width(5)
                            .color(Color.RED));
                }
                oldlng = location.getLongitude();
                oldlat = location.getLatitude();
                String plus = "=|" + location.getLatitude() + "||" + location.getLongitude() + "||"+location.getAltitude()+"|=";
//                writeToFile(plus, context);
                Log.wtf("NULL : ", plus);
//                bundle.putDouble("lat",location.getLatitude());
//                bundle.putDouble("lng",location.getLongitude());
//                bundle.putDouble("alt",location.getAltitude());



            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("NULL : ", "fab");
                Toast.makeText(getApplicationContext(), "fab", Toast.LENGTH_SHORT).show();
                if (mMap != null) {

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                            .zoom(16)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate);
                    if (oldlat == 0 || oldlng == 0) {
                        oldlat = mMap.getMyLocation().getLatitude();
                        oldlng = mMap.getMyLocation().getLongitude();
                    }
                    if (mMarker != null) {
//                        mMarker.setPosition(latLng);
                        mMarker.setVisible(true);
                    } else {
                        mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_bl))
                                .title("Delete marker?"));

                    }
                }

//                Polyline line = mMap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                        .width(5)
//                        .color(Color.RED));
            }


        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("NULL : ", "fab1");
                Toast.makeText(getApplicationContext(), "fab1", Toast.LENGTH_SHORT).show();
                if (mMap != null) {

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                            .zoom(16)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate);
//                    if (oldlat == 0 || oldlng == 0) {
//                        oldlat = mMap.getMyLocation().getLatitude();
//                        oldlng = mMap.getMyLocation().getLongitude();
//                    }

                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish))
                            .title("Delete marker?"));


                }

//                Polyline line = mMap.addPolyline(new PolylineOptions()
//                        .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
//                        .width(5)
//                        .color(Color.RED));
            }


        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.wtf("NULL : ", "camera position");

                if (mMap != null) {

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latLng.latitude, latLng.longitude))
                            .zoom(16)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    mMap.animateCamera(cameraUpdate);
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent autocomplete =
                        null;
                try {
                    autocomplete = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MapsActivity.this);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(autocomplete, 1);


            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //

//                Toast.makeText(,file.exists(),Toast.LENGTH_SHORT).show();
//            Log.wtf("NULL", String.valueOf(file.exists()));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                        .zoom(16)
                        .build();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                mMap.animateCamera(cameraUpdate);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Log.wtf("NULL : ", "set up map");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.wtf("NULL : ", "proverka");
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isMapToolbarEnabled();

    }


    @Override
    public void onMapLoaded() {
        if (mMap != null) {
//                mMap.snapshot(this);
            Log.wtf("NULL : ", "map loaded");
            init();
        }
    }
}
