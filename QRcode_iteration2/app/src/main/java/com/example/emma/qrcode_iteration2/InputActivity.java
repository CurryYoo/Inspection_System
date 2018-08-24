package com.example.emma.qrcode_iteration2;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InputActivity extends AppCompatActivity {
    private String[] isSwitch = new String[20];
    private LinearLayout linear3;

    private String name;
    private int len;
    private Item item;
    private String[] str_I;  //存储所有名称的数组
    private String[] str_T;  //存储所有类型的数组
    private FixedEditText[] fet = new FixedEditText[100];
    private Switch[] sth = new Switch[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Button submit1 = (Button)findViewById(R.id.submit1);
        Button review = (Button)findViewById(R.id.review);
        TextView inputTitle = (TextView)findViewById(R.id.inputTitle);
        linear3 = (LinearLayout)findViewById(R.id.linearLayout3);

        Intent intent = getIntent();    //ParamActivity
        name = intent.getStringExtra("name");
        inputTitle.setText(name);   //设置页面的标题
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

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = InputActivity.this.openFileInput(name + "_data");
                    String review_data = ListActivity.loadDataFromFile(InputActivity.this, name + "_data");
                    Log.d("tttttt1", review_data);
                    AlertDialog alertDialog1 = new AlertDialog.Builder(InputActivity.this)
                            .setTitle(name)//标题
                            .setMessage("历史输入的数据：\n" + review_data + "\n")  //内容
                            .setIcon(R.mipmap.ic_launcher)  //
                            .create();
                    alertDialog1.show();
                } catch (FileNotFoundException e) {
                    Toast.makeText(InputActivity.this, "暂未输入过数据！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        submit1.setOnClickListener(new View.OnClickListener() {
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
                    else {
                        Time time = new Time();
                        time.setToNow();
                        int year = time.year;
                        int month = time.month+1;
                        int day = time.monthDay;
                        int hour = time.hour; // 0-23
                        int minute = time.minute;
                        int second = time.second;
                        str_json +=",\"日期\":\"" + year + "年" + month + "月" + day + "日\",\"时间\":\"" + hour +":" + minute +"\"";
                    }
                }
                info += "\"";
                str_json += "}";

                intent.putExtra("data_return", message);
                intent.putExtra("info", info);
                intent.putExtra("json", str_json);
                setResult(RESULT_OK, intent);

                Log.d("tttttt", message);
                ListActivity.saveDataToFile(InputActivity.this, message, name + "_data");  //存储历史数据

                finish();
            }
        });
    }
}
