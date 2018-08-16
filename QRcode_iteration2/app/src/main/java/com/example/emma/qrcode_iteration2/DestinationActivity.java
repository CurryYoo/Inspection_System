package com.example.emma.qrcode_iteration2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by touchzy on 2018/4/15.
 */

public class DestinationActivity extends AppCompatActivity {
    private List<String> destinationList = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.destination);

        String[] location = {"招商银行","北门","中国移动","小米手机","H&M北门","Balabala","UNIQ南门",
                            "海底捞","西门","东门","吉野家","星巴克北门","扶梯","视客VR","原麦山丘",
                            "Apple体验店北门","鹿港小镇","麦颂KTV","南门","儿童游乐场北门","JDING",
                            "H&M南门","UNIQ东门","星巴克南门","Apple体验店南门","儿童游乐场南门"};

        for(int i = 0; i<location.length; i++)
            destinationList.add(location[i]);
        DestinationAdapter adapter = new DestinationAdapter(DestinationActivity.this,
                R.layout.destination_item,destinationList);

        ListView listView = (ListView) findViewById(R.id.destinations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("position",position+"");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
