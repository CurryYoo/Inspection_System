package com.example.emma.qrcode_iteration2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.alibaba.fastjson.JSON;

public class InputActivity extends AppCompatActivity {
    private String[] isSwitch = new String[20];
    private LinearLayout linear3;

    private String name;
    private int len;
    private Item item;
    private String[] str_I;  //存储所有名称的数组
    private String[] str_T;  //存储所有类型的数组
    private FixedEditText[] fet = new FixedEditText[10];
    private Switch[] sth = new Switch[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        final Switch switch1 = (Switch)findViewById(R.id.switch1);

        Switch switch2 = (Switch)findViewById(R.id.switch2);
        Button button1 = (Button)findViewById(R.id.button1);

        linear3 = (LinearLayout)findViewById(R.id.linearLayout3);

        Intent intent = getIntent();  //ParamActivity
        name = intent.getStringExtra("name");
        Log.d("InputActivity", name);
        String json_item = ListActivity.loadDataFromFile(InputActivity.this, name);
        item = JSON.parseObject(json_item, Item.class);
        str_I = item.getItems().split(",");
        str_T = item.getType().split(",");
        len = str_I.length;
        for(int i = 0; i < len; i++){
            if(str_T[i].equals("text")){
                fet[i] = new FixedEditText(this);
                fet[i].setFixedText(str_I[i]);
                linear3.addView(fet[i]);
            }
            else{  //"bool"
                sth[i] = new Switch(this);
                sth[i].setText(str_I[i]);
                final int finalI = i;
                isSwitch[finalI] = "0";
                sth[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            isSwitch[finalI] = "1";  //正常
                        }else {
                            isSwitch[finalI] = "0";  //异常
                        }
                    }
                });
                linear3.addView(sth[i]);
            }

        }

//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    isSwitch1 = "是";
//                }else {
//                    isSwitch1 = "否";
//                }
//            }
//        });
//        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    isSwitch2 = "是";
//                }else {
//                    isSwitch2 = "否";
//                }
//            }
//        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
//                intent.putExtra("data_return",
//                        "指示灯是否正常："+ isSwitch1 + "，有无异响（是否正常）：" + isSwitch2 +
//                                "，温度：" + Float.parseFloat(editText.getText().toString()));
                String message = "";
                String info = "\"" + name + "\":\"";
                String str_json = "\"" + name + "\":{";
                for(int i = 0; i < len; i++){
                    if(str_T[i].equals("text")){
                        message += str_I[i] + ": " + fet[i].getText().toString() + "\n";
                        info += str_I[i];
                        str_json += "\"" + str_I[i] + "\":\"" + fet[i].getText().toString() +"\"";

                    }
                    else{  //"bool"
                        message += str_I[i] + ": " + isSwitch[i] + "\n";
                        info += str_I[i];
                        str_json += "\"" + str_I[i] + "\":\"" + isSwitch[i] +"\"";

                    }
                    if(i < len - 1){
                        str_json += ",";
                        info += ",";
                    }
                }
                info += "\"";
                str_json += "}";

                intent.putExtra("data_return", message);
                intent.putExtra("info", info);
                intent.putExtra("json", str_json);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
