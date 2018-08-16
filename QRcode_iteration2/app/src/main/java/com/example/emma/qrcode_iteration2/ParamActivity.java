package com.example.emma.qrcode_iteration2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sy.qrcodelibrary.interfaces.OnScanSucceedListener;
import com.sy.qrcodelibrary.utils.QRCodeUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by touchzy on 2018/3/20.
 */

public class ParamActivity extends AppCompatActivity{
    public static final int MAX = 20;
    private ConstraintLayout layout;
    private LinearLayout linear;
    private LinearLayout linear2;

    private Qrcode qrcode;
    private static final int UPDATE_POINT = 0; //更新点的标志
    private Handler mainHandler= new Handler();

    private int id;
    private String name;
    private String str_T;
    private String[] Tname;
    private Button[] btn;


    private TextView display;
    private ImageView imageView;
    private ImageView imageView2;

    private String info[] = new String[100];
    private boolean isInfo[] = new boolean[10];
    private String info_json[] = new String[100];
    private String json[] = new String[100];

    private FixedEditText fet;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.param);

        final Gson gson = new Gson();

        ImageView map = (ImageView) findViewById(R.id.map);

        layout = (ConstraintLayout)findViewById(R.id.parent);
        linear = (LinearLayout)findViewById(R.id.linearLayout);
        linear2 = (LinearLayout)findViewById(R.id.linearLayout2);

        Button scan = (Button)findViewById(R.id.scan);
        Button list = (Button)findViewById(R.id.list);
        final TextView param = (TextView)findViewById(R.id.param);
        display = (TextView)findViewById(R.id.display);
        Button submit = (Button)findViewById(R.id.submit);

        btn = new Button[MAX];
        ImageView[] imv = new ImageView[MAX];

//        str_T = ListActivity.loadDataFromFile(ParamActivity.this, "table");
//        Tname = str_T.split(" ");
//        Log.d("length", "" + Tname.length);
        UpdateScreen(btn);
//        for(int i = 0; i < Tname.length; i++){
//            btn[i] = new Button(this);
//            btn[i].setText(Tname[i]);
//            linear.addView(btn[i]);
//            btn[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(isInfo[0]) { //#####此处需要修改#####
//                        AlertDialog alertDialog1 = new AlertDialog.Builder(ParamActivity.this)
//                                .setTitle("低压配电房")//标题
//                                .setMessage("输入的数据：\n" + info[0] + "\n")//内容  #####此处需要修改#####
//                                .setIcon(R.mipmap.ic_launcher)//图标
//                                .create();
//                        alertDialog1.show();
//                    }
//                    else{
//                        Toast.makeText(ParamActivity.this, "没有录入数据！", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }

        ImageView iv1 = new ImageView(this);
        iv1.setImageDrawable(getResources().getDrawable(R.drawable.check));
        linear2.addView(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setImageDrawable(getResources().getDrawable(R.drawable.check));
        linear2.addView(iv2);

//        fet = new FixedEditText(this);
//        fet.setFixedText("fet1");
//        linear.addView(fet);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeUtil.scanCode(ParamActivity.this, new OnScanSucceedListener() {
                    @Override
                    public void onScanSucceed(final String resultString) {
                        Log.i("resultString", resultString);
                        qrcode = gson.fromJson(resultString, Qrcode.class);
                        //qrcode = JSON.parseObject(resultString, Qrcode.class);

                        id = qrcode.getId();
                        name = qrcode.getName();

                        Intent intent = new Intent(ParamActivity.this, InputActivity.class);
                        intent.putExtra("name", Tname[id - 1]);
                        startActivityForResult(intent, id);

                        //param.setText(qrcode.toString());
                    }
                });
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestWithHttp();
                Toast.makeText(ParamActivity.this, "提交数据成功！", Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ParamActivity.this, ListActivity.class);
                startActivityForResult(intent,10);
            }
        });

    }

    private boolean isNetworkConnected(Context context) {
        if(context != null){
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode != 10){
            if(resultCode == RESULT_OK){
                info[requestCode - 1] = intent.getStringExtra("data_return");
                isInfo[requestCode - 1] = true;
                //imageView.setImageDrawable(getResources().getDrawable(R.drawable.check));

                info_json[requestCode - 1] = intent.getStringExtra("info");
                json[requestCode - 1] = intent.getStringExtra("json");
                //display.setText(info[0]);
            }
        }
        else{
            linear.removeAllViews();
            UpdateScreen(btn);
        }
//        switch (requestCode){
//            case 1:
//                info[0] = intent.getStringExtra("data_return");
//                isInfo[0] = true;
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.check));
//                //display.setText(info[0]);
//                break;
//            case 2:
//                info[1] = intent.getStringExtra("data_return");
//                isInfo[1] = true;
//                imageView2.setImageDrawable(getResources().getDrawable(R.drawable.check));
//                //display.setText(info[1]);
//                break;
//            case 10:
//                linear.removeAllViews();
//                UpdateScreen(Tname, btn);
//                break;
//            default:
//        }
    }

    private void UpdateScreen(Button[] btn){
        str_T = ListActivity.loadDataFromFile(ParamActivity.this, "table");
        Tname = str_T.split(" ");
        for(int i = 0; i < Tname.length; i++){
            btn[i] = new Button(this);
            btn[i].setText(Tname[i]);
            linear.addView(btn[i]);
            final int finalI = i;
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isInfo[finalI]) { //#####此处需要修改#####
                        AlertDialog alertDialog1 = new AlertDialog.Builder(ParamActivity.this)
                                .setTitle(Tname[finalI])//标题
                                .setMessage("输入的数据：\n" + info[finalI] + "\n")//内容  #####此处需要修改#####
                                .setIcon(R.mipmap.ic_launcher)//图标
                                .create();
                        alertDialog1.show();
                    }
                    else{
                        Toast.makeText(ParamActivity.this, "没有录入数据！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendRequestWithHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL("http://183.60.167.132:8000/audit/upload");
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    String message = "{";
                    for(int i = 0; i < Tname.length; i++){
                        message += json[i];
                        if(i < Tname.length - 1){
                            message += ",";
                        }
                    }
                    message += "}";
                    Log.d("message", message);
                    String test_m = "{\"机房\":{\"温度\":\"526\",\"是否\":\"公交卡\"},\"测试\":{\"项目1\":\"865\",\"项目2\":\"得分\"}}";
                    out.write(message.getBytes());
                    //out.writeUTF(test_m);
                    //out.writeBytes(test_m);

                    InputStream in = connection.getInputStream();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
//                try{
//                    OkHttpClient client = new OkHttpClient();
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("name", "admin")
//                            .add("type", "1")
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://183.60.167.132:8000/audit/upload")
//                            .post(requestBody)
//                            .build();
//                    Response response = client.newCall(request).execute();
//                    String responseData = response.body().string();
//                    Log.d("showResponse", request.toString());
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
            }
        }).start();
    }

    //坐标点请求  APPEND_SLASH=False
//    public void httpRequest_P(final String resultString, final HttpRequest.HttpCallback callback) {
//        new Thread(){
//            @Override
//            public void run() {
//                    //你的处理逻辑,这里简单睡眠一秒
//                    String url = "http://10.209.4.196:8080";
//                    HttpRequest.HttpLocation(url, resultString, ParamActivity.this, new HttpRequest.HttpCallback() {
//                        @Override
//                        public void onSuccess(final Location location) {
//                            mainHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    callback.onSuccess(location);
//                                }
//                            });
//                        }
//                    });
//                try {
//                    this.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                /*
//
//                 */
//            }
//        }.start();
//
//    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}