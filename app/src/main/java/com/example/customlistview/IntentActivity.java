package com.example.customlistview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.Utmk;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;

import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class IntentActivity  extends AppCompatActivity implements OnMapReadyCallback {
    ImageView imageview;
    TextView title;
    TextView location;
    TextView hompage;
    TextView overview;

    String i_homepage;
    String i_overview;

    double mapx;
    double mapy;

    private MapView mapView;
   private static NaverMap naverMap;
    private Marker marker = new Marker();
    GeoPoint out_pt;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        imageview = (ImageView)findViewById(R.id.imageview);
        title = (TextView)findViewById(R.id.title);
        hompage = (TextView)findViewById(R.id.homepage);
        overview = (TextView)findViewById(R.id.overview);
        location = (TextView)findViewById(R.id.location);
        Intent it = getIntent();

        mapx = it.getDoubleExtra("mapx",0);
        mapy = it.getDoubleExtra("mapy",0);

        GeoPoint in_pt = new GeoPoint(mapx,mapy);
        out_pt = GeoTrans.convert(GeoTrans.GEO,GeoTrans.UTMK,in_pt);
        Log.i("위치", String.valueOf(out_pt.x));
        Log.i("위치", String.valueOf(out_pt.y));




        //marker.setPosition(new LatLng(it.getDoubleExtra("mapx",0), it.getDoubleExtra("mapy",0)));
        title.setText(it.getStringExtra("title"));
        AQuery aq = new AQuery(IntentActivity.this);
        aq.id(imageview).image(it.getStringExtra("ivPhoto"), true, false);
        HashMap<String, String> params = new HashMap<>();
        params.put("ServiceKey", "VDMAPJZ%2BYE0eMjGgo0nH2M1g2glifvydAD9f9Ns5kgAAdUY6Qzj5Y0dQtAY9IT7poZR9YXZA4e%2B3NSezxBfDug%3D%3D");
        params.put("MobileOS", "ETC");
        params.put("MobileApp", "AppTest");
        params.put("contentId", it.getStringExtra("contentId"));
        params.put("defaultYN", "Y");
        params.put("addrinfoYN", "Y");
        params.put("overviewYN", "Y");
        params.put("_type", "json");

        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";
        url = addParams(url, params);

        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject resutl, AjaxStatus status) {
                if (resutl != null) {
                    //sucess

                    Log.i("test", resutl.toString());

                    try {
                        JSONObject jar = resutl.optJSONObject("response").optJSONObject("body").optJSONObject("items").optJSONObject("item");
                        location.setText(jar.optString("addr1"));
                        i_homepage = jar.optString("homepage");
                        hompage.setText(stripHtml(i_homepage));
                        i_homepage = stripHtml(i_homepage);
                        i_overview = ( jar.optString("overview"));
                        overview.setText(stripHtml(i_overview));

                    } catch (Exception e) {
                        Toast.makeText(IntentActivity.this, "JSON Parsing 오류 발생", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //fail
                    Toast.makeText(IntentActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                }
            }
        }.timeout(20000));


       //네이버 지도
       mapView = (MapView)findViewById(R.id.map_view);
       mapView.onCreate(savedInstanceState);
       mapView.getMapAsync(this);





        hompage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(i_homepage));
                startActivity(intent);

            }
        });


    }


    private String addParams(String url, HashMap<String, String> mapParam) {
        StringBuilder stringBuilder = new StringBuilder(url + "?");

        if (mapParam != null) {
            for (String key : mapParam.keySet()) {
                stringBuilder.append(key + "=");
                stringBuilder.append(mapParam.get(key) + "&");
            }
        }
        return stringBuilder.toString();
    }


    //html 코드 제거
    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;

        Utmk utmk = new Utmk(out_pt.x, out_pt.y);
        LatLng latLng = utmk.toLatLng();

        CameraPosition cameraPosition = new CameraPosition(new LatLng(latLng.latitude,latLng.longitude),12);
        naverMap.setCameraPosition(cameraPosition);

        marker.setPosition(new LatLng(latLng.latitude, latLng.longitude));
        marker.setMap(naverMap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}


