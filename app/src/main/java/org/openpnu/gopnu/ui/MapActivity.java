package org.openpnu.gopnu.ui;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.openpnu.gopnu.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, Button.OnClickListener {

    GoogleMap googleMap;
    Button LocationInputBtn;
    MapFragment mapFragment;
    public static double Glatitude;
    public static double Glongtitude;
    public static String Gaddress;
    public static Geocoder geocoder;
    LocationManager manager;
    GPSListener gpsListener;
    LatLng curPoint;
    Location lastLocation;
    MarkerOptions optFirst;
    int mark_count;
    EditText AddressInput;
    //   Button SearchButton;
    int gpsEnable;
    Marker temp;
    LocationManager locationManager;
    int i = 0;
    static public ArrayList<PendingIntent> pendingList = new ArrayList<PendingIntent>();

    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Glatitude = 0;
        Glongtitude = 0;
        gpsEnable = 1;

        mark_count = 0;
        LocationInputBtn = (Button) findViewById(R.id.location_input_button);
        LocationInputBtn.setOnClickListener(this);
        AddressInput = (EditText) findViewById(R.id.address_text);
        //SearchButton = (Button) findViewById(R.id.search_button);
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
        curPoint = new LatLng(37.555744, 126.970431);
        optFirst = new MarkerOptions();
        optFirst.position(curPoint);
        gpsListener = new GPSListener();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        startLocationService();

        AddressInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(MapActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                    //intent.

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                Gaddress = (String) place.getAddress();
                Glatitude = place.getLatLng().latitude;
                Glongtitude = place.getLatLng().longitude;

                MarkerOptions mOption = new MarkerOptions();
                mOption.title((String) place.getName());
                mOption.position(place.getLatLng());
                mOption.snippet(Gaddress);
                temp.remove();
                optFirst = mOption;
                optFirst.draggable(true);

                temp = googleMap.addMarker(optFirst);
                temp.showInfoWindow();

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));

                //Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                //Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled operation.
            }
        }
    }

    public boolean startLocationService() {

        long minTime = 10;
        float minDistance = 0;
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            return false;
        } else {
            try {

                // GPS를 이용한 위치 요청
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDistance, gpsListener);

                // 네트워크를 이용한 위치 요청
                manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        minTime, minDistance, gpsListener);
                // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인

                lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    Glatitude = lastLocation.getLatitude();
                    Glongtitude = lastLocation.getLongitude();


                }
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
            return true;
            //   Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.location_input_button:
                // 버튼누르겨 화면 끄고 나서
                // 더블로 롱티듀드 래티듀드 받아옴
                //mapFragment.onStop();
                 //Toast.makeText(this, "제대로", Toast.LENGTH_SHORT).show();

                Random rnd = new Random();
                i = rnd.nextInt(500);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                PendingIntent intent1 = register(i, Glatitude, Glongtitude, "hum", Gaddress, 500, -1);

                if (intent1 != null) {
                    pendingList.add(intent1);
                }
                Toast.makeText(this, "근접경보 설정됨", Toast.LENGTH_SHORT).show();


                manager.removeUpdates(gpsListener);
                super.onStop();
                finish();
                break;
            default:
                finish();
                break;

        }
    }

    public class GPSListener implements LocationListener {
        //위치 정보가 확인될 때 자동 호출되는 메소드

        public void onLocationChanged(Location location) {
            if(gpsEnable==1) {
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                String msg = "Latitude : " + latitude + "\nLongitude:" + longitude;
                //Log.v("GPSListener", msg);
                Glatitude = latitude;
                Glongtitude = longitude;
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                showCurrentlocation(latitude, longitude);
                gpsEnable=0;

            }
            else
            {
                manager.removeUpdates(gpsListener);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {


        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }

    private void showCurrentlocation(Double latitude, Double longitude) {
        curPoint = new LatLng(latitude, longitude);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        //마커추가
        List<Address> addressList = null;
        try {
            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
            addressList =  geocoder.getFromLocation(latitude,longitude, 10); // 최대 검색 결과 개수
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // System.out.println(addressList.get(0).toString());
        // 콤마를 기준으로 split

        Address a=addressList.get(0);
        String []splitStr = a.toString().split(",");
        int countCountry=a.getCountryName().length();
        Gaddress = splitStr[0].substring(splitStr[0].indexOf("\"") +countCountry+2,splitStr[0].length() - 2); // 주소
        String feature = a.getFeatureName();

        optFirst.position(curPoint);// 위도 • 경도
        optFirst.title(feature);// 제목 미리보기
        optFirst.snippet(Gaddress);
        temp=googleMap.addMarker(optFirst);
        temp.showInfoWindow();
        //optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_back));

    }
    public void onMapReady(GoogleMap map) {//맵의 onCreate 쯤된다.
        googleMap = map;
        geocoder = new Geocoder(this);

        // Toast.makeText(this, "set map", Toast.LENGTH_SHORT).show();

        setUpMap();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                // 마커 타이틀
                Glatitude = point.latitude; // 위도
                Glongtitude = point.longitude; // 경도

                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocation(Glatitude, Glongtitude, 10); // 최대 검색 결과 개수
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // System.out.println(addressList.get(0).toString());
                // 콤마를 기준으로 split
                Address a = addressList.get(0);
                String[] splitStr = a.toString().split(",");
                int countCountry = a.getCountryName().length();
                Gaddress = splitStr[0].substring(splitStr[0].indexOf("\"") + countCountry + 2, splitStr[0].length() - 2); // 주소
                String feature = a.getFeatureName();

                MarkerOptions tempMark = new MarkerOptions();
                tempMark.title(feature);
                // 마커의 스니펫(간단한 텍스트) 설정
                tempMark.snippet(Gaddress);
                // LatLng: 위도 경도 쌍을 나타냄
                tempMark.position(point);
                // 마커(핀) 추가
                //if(optFirst.)
                //curPoint.
                //googleMap.clear();
                temp.remove();
                optFirst = tempMark;
                optFirst.draggable(true);

                temp = googleMap.addMarker(optFirst);
                temp.showInfoWindow();
            }
        });




    }


    public void setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);

        }
        else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            googleMap.setMyLocationEnabled(true);
            googleMap.setTrafficEnabled(true);
            googleMap.setIndoorEnabled(true);
            googleMap.setBuildingsEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
        }
    }
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},0);
            // googleMap.setMyLocationEnabled(true);
        }
    }

    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},0);
            // googleMap.setMyLocationEnabled(false);
        }


    }
    public void onBackPressed()
    {
        Glatitude=0;
        Gaddress="";
        Glongtitude=0;


        super.onBackPressed();
    }

    private PendingIntent register(int id, double latitude, double longtitude, String content,String address, float radius
            , long expiration) {

        Intent intent = new Intent("proximityAlert");
        intent.putExtra("id", id);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longtitude", longtitude);
        intent.putExtra("content", content);
        intent.putExtra("address",address);

        //인텐트 대기
        //조건에 맞을 때 실행
        // 여기서는 근접했을 때 실행
        //PendingIntent.FLAG_CANCEL_CURRENT 은 현재 등록된 것이 있으면 등록된것을
        //취소하고 현재 이것을 실행
        //조건이 맞으면 내가 받겠다.
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(this, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "제대로", Toast.LENGTH_SHORT).show();
            return null;
        }
        locationManager.addProximityAlert(latitude, longtitude, radius, expiration, pendingIntent);
        Toast.makeText(this, "되나?", Toast.LENGTH_SHORT).show();
        return pendingIntent;
    }

}