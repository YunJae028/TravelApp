package com.example.customlistview;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubActivity extends AppCompatActivity {

    private ListViewItem data;

    ListView listView = null;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_main);

        listView = (ListView) findViewById(R.id.listview); //activity_main.xml 에서 listview id값 연걸
        // Adapter 생성
        adapter = new ListViewAdapter(SubActivity.this);
        adapter.setTelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewItem item = adapter.getItem((Integer) v.getTag());

                String tel = "tel:" + item.getTel();
//                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });
        adapter.setMapClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewItem item = adapter.getItem((Integer) v.getTag());
                Uri gmmIntentUri = Uri.parse("geo:" + item.getMapy() + "," + item.getMapx());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        listView.setAdapter(adapter);

        //데이터 로드 부분
        loadData();
    }


    private void loadData() {
        String localNum;
        Intent it = getIntent();
        localNum = it.getStringExtra("localNum");
        AQuery aq = new AQuery(SubActivity.this);

        HashMap<String, String> params = new HashMap<>();
        params.put("ServiceKey", "VDMAPJZ%2BYE0eMjGgo0nH2M1g2glifvydAD9f9Ns5kgAAdUY6Qzj5Y0dQtAY9IT7poZR9YXZA4e%2B3NSezxBfDug%3D%3D");
        params.put("numOfRows", "10");
        params.put("pageNo", "1");
        params.put("MobileOS", "ETC");
        params.put("MobileApp", "AppTest");
        params.put("arrange", "P");
        params.put("listYN", "Y");
        params.put("areaCode", localNum);//1 서울 //39 제주도 //5 광주 // 6 부산
        params.put("_type", "json");

        String url = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
        url = addParams(url, params);

        aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject resutl, AjaxStatus status) {
                if (resutl != null) {
                    //sucess
                    Log.i("test", resutl.toString());

                    try {
                        JSONArray jar = resutl.optJSONObject("response").optJSONObject("body").optJSONObject("items").optJSONArray("item");

                        ArrayList<ListViewItem> arItem = new ArrayList<ListViewItem>();
                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject jobj = jar.optJSONObject(i);

                            ListViewItem item = new ListViewItem();
                            item.setTitle(jobj.optString("title"));
                            item.setAddress(jobj.optString("addr1"));
                            item.setFirstimage(jobj.optString("firstimage"));
                            item.setMapx(jobj.optDouble("mapx"));
                            item.setMapy(jobj.optDouble("mapy"));
                            item.setTel(jobj.optString("tel"));
                            item.setContentId(jobj.optString("contentid"));
                            item.setReadcount(jobj.optString("readcount"));
                            arItem.add(item);
                        }

                        if (arItem.size() > 0) {
                            adapter.getArItem().addAll(arItem);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SubActivity.this, "JSON Parsing 오류 발생", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //fail
                    Toast.makeText(SubActivity.this, "잘못된 요청입니다", Toast.LENGTH_SHORT).show();
                }
            }
        }.timeout(20000));


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


}
