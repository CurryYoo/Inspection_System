package com.example.emma.qrcode_iteration2;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ListActivity extends AppCompatActivity {

    private Handler mainHandler= new Handler();

    private String filename_T = "table";
    private String filename_I = "item";

    private String[] Tname;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Button download = (Button)findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest_T(new HttpRequest.HttpCallback() {
                    @Override
                    public void onSuccess(String response) {
                        saveDataToFile(ListActivity.this, response, filename_T);
                        Log.d("test", loadDataFromFile(ListActivity.this, filename_T));
                        Tname = response.split(" ");

                        for(int i = 0; i < Tname.length; i++){
                            final int finalI = i;
                            httpRequest_I(Tname[i], new HttpRequest.HttpCallback2() {
                                @Override
                                public void onSuccess(Item item) {
                                    saveDataToFile(ListActivity.this, item.toString(), Tname[finalI]);
                                    Log.d("III_" + finalI, loadDataFromFile(ListActivity.this, Tname[finalI]));
                                }
                            });
                        }

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        });
    }

    /**
     * 将数据存到文件中
     *
     * @param context context
     * @param data 需要保存的数据
     * @param fileName 文件名
     */
    public static void saveDataToFile(Context context, String data, String fileName) {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            /**
             * "data"为文件名,MODE_PRIVATE表示如果存在同名文件则覆盖，
             * 还有一个MODE_APPEND表示如果存在同名文件则会往里面追加内容
             */
            fileOutputStream = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件中读取数据
     * @param context context
     * @param fileName 文件名
     * @return 从文件中读取的数据
     */
    public static String loadDataFromFile(Context context, String fileName) {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            /**
             * 注意这里的fileName不要用绝对路径，只需要文件名就可以了，系统会自动到data目录下去加载这个文件
             */
            fileInputStream = context.openFileInput(fileName);
            bufferedReader = new BufferedReader(
                    new InputStreamReader(fileInputStream));
            String result = "";
            int line = 0;
            while ((result = bufferedReader.readLine()) != null) {
                if(line > 0){
                    stringBuilder.append("\n");  //为了使查看历史纪录界面更加直观，加入换行符。
                }
                stringBuilder.append(result);
                line++;

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public void httpRequest_T(final HttpRequest.HttpCallback callback) {
        new Thread(){
            @Override
            public void run() {
                //你的处理逻辑,这里简单睡眠一秒
                String url = "http://183.60.167.132:8000/inspection/dblist";
                HttpRequest.httpRequest_1(url, ListActivity.this, new HttpRequest.HttpCallback() {
                    @Override
                    public void onSuccess(final String response) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(response);
                            }
                        });
                    }
                });
                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public void httpRequest_I(final String name, final HttpRequest.HttpCallback2 callback) {
        new Thread(){
            @Override
            public void run() {
                //你的处理逻辑,这里简单睡眠一秒
                String url = "http://183.60.167.132:8000/inspection/" + name;
                HttpRequest.httpRequest_2(url, ListActivity.this, new HttpRequest.HttpCallback2() {
                    @Override
                    public void onSuccess(final Item item) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(item);
                            }
                        });
                    }
                });
                try {
                    this.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }


}
