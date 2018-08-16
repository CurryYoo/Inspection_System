package com.example.emma.qrcode_iteration2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sy.qrcodelibrary.decoding.Intents;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by Administrator on 2018/4/20.
 */

public class HttpRequest {
    private static GraphInfor graphInfor=null;
    private static Location location=null;
    private static Item item;

    public static void httpRequest_1(String url, Context context, final HttpCallback callback){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG_", response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }

    public static void httpRequest_2(String url, Context context, final HttpCallback2 callback){
        RequestQueue mQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TAG_", response);
                item = JSON.parseObject(response, Item.class);
                callback.onSuccess(item);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
        mQueue.add(stringRequest);
    }

    //获取一个楼层信息的请求
//    public static void HttpQuestGraph(String url, String ScanJson, Context context, final HttpCallback2 callback) throws UnsupportedEncodingException {
//        Message message= JSON.parseObject(ScanJson,Message.class);
//        RequestQueue mQueue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,url+"/getGraphInfor/"+  URLEncoder.encode(message.getBuildName(),"utf-8")+"/"+message.getFloor(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("TAG_", response);
//                        graphInfor=JSON.parseObject(response,GraphInfor.class);
//                        callback.onSuccess(graphInfor);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//        mQueue.add(stringRequest);
//    }
//    //获取一个二维码信息的请求
//    public static void HttpLocation(String url, String ScanJson, Context context, final HttpCallback callback){
//        Message message=JSON.parseObject(ScanJson,Message.class);
//        RequestQueue mQueue = Volley.newRequestQueue(context);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,url+"/findQRcode/"+message.getId(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("TAG_", response);
//                       location=JSON.parseObject(response,Location.class);
//                       callback.onSuccess(location);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//
//        mQueue.add(stringRequest);
//    }

    public interface HttpCallback {
        public void onSuccess(String response);
    }

    public interface HttpCallback2{
        public void onSuccess(Item item);
    }
}
