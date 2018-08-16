package com.example.emma.qrcode_iteration2;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class FixedEditText extends android.support.v7.widget.AppCompatEditText {
    private String fixedText;
    private View.OnClickListener mListener;

    public FixedEditText(Context context) {
        super(context);
    }

    public void setFixedText(String text) {
        fixedText = text;
        int left = (int) getPaint().measureText(fixedText)+ getPaddingLeft();
        setPadding(left, getPaddingTop(), getPaddingBottom(), getPaddingRight());
        invalidate();
    }

    public void setDrawableClk(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(fixedText)) {
            canvas.drawText(fixedText, 0, (getMeasuredHeight() - getTextSize()) / 2 + getTextSize(), getPaint());
//            通过下面的代码，可以查看出文字的基线，以及view的中线
//            Paint p = new Paint();
//            p.setStrokeWidth(1);
//            p.setColor(Color.parseColor("#ff0000"));
//            canvas.drawLine(0, getBaseline(), getMeasuredWidth(), getBaseline(), p);
//            canvas.drawLine(0, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, p);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mListener != null && getCompoundDrawables()[2] != null) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    int i = getMeasuredWidth() - getCompoundDrawables()[2].getIntrinsicWidth();
                    if (event.getX() > i) {
                        mListener.onClick(this);
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
                default:
                    break;
            }

        }

        return super.onTouchEvent(event);
    }
}
