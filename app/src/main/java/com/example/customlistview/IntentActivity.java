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

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class IntentActivity  extends AppCompatActivity {
    ImageView imageview;
    TextView title;
    TextView location;
    TextView hompage;
    TextView overview;

    String i_addr;
    String i_homepage;
    String i_overview;
    String i_fm;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        imageview = (ImageView)findViewById(R.id.imageview);
        title = (TextView)findViewById(R.id.title);
        hompage = (TextView)findViewById(R.id.homepage);
        overview = (TextView)findViewById(R.id.overview);
        location = (TextView)findViewById(R.id.location);

        Intent it = getIntent();
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

}
