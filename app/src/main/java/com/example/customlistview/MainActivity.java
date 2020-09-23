package com.example.customlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView1= (ImageView) findViewById(R.id.new_seoul);
        ImageView imageView2 = (ImageView)findViewById(R.id.jeju);
        ImageView imageView3 = (ImageView)findViewById(R.id.busan);
        ImageView imageView4 = (ImageView)findViewById(R.id.gangju);

        imageView1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                intent.putExtra("localNum","1");
                startActivity(intent);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                intent.putExtra("localNum","39");
                startActivity(intent);
            }
        });


        imageView3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                intent.putExtra("localNum","6");
                startActivity(intent);
            }
        });


        imageView4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SubActivity.class);
                intent.putExtra("localNum","5");
                startActivity(intent);
            }
        });
    }


}
