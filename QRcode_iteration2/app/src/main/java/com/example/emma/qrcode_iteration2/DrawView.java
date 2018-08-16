package com.example.emma.qrcode_iteration2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by touchzy on 2018/4/13.
 */

public class DrawView extends View {
    private float start_x,start_y,stop_x,stop_y;
    private int path = 0;

    public DrawView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        Paint paint = new Paint();

        if(path == 0) {
            paint.reset();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5);
            canvas.drawLine(start_x, start_y, stop_x, stop_y, paint);
        }
        else if(path == 1){
            DashPathEffect pathEffect = new DashPathEffect(new float[] { 1,2 }, 1);
            paint.reset();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setPathEffect(pathEffect);
            Path path = new Path();
            path.moveTo(start_x,start_y);
            path.lineTo(stop_x,stop_y);
            canvas.drawPath(path, paint);
        }
        else if(path == 2){
            DashPathEffect pathEffect = new DashPathEffect(new float[] { 1,2 }, 1);
            paint.reset();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.RED);
            paint.setAntiAlias(true);
            paint.setPathEffect(pathEffect);
            Path path = new Path();
            path.moveTo(start_x,start_y);
            path.lineTo(stop_x,stop_y);
            canvas.drawPath(path, paint);
        }
    }
    public void setPath(int path) { this.path = path; }

    public void setStart_x(float start_x) {
        this.start_x = start_x;
    }

    public void setStart_y(float start_y) {
        this.start_y = start_y;
    }

    public void setStop_x(float stop_x) {
        this.stop_x = stop_x;
    }

    public void setStop_y(float stop_y) {
        this.stop_y = stop_y;
    }
}