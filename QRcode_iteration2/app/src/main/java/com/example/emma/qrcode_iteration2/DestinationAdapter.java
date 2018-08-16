package com.example.emma.qrcode_iteration2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by touchzy on 2018/4/15.
 */

public class DestinationAdapter extends ArrayAdapter<String>{

    private int resourceId;

    public DestinationAdapter(Context context, int textViewResourceId,
                        List<String> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String destination = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view=LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.desImage = (ImageView) view.findViewById (R.id.des_image);
            viewHolder.desName = (TextView) view.findViewById (R.id.des_name);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.desImage.setImageResource(R.drawable.placeholder);
        viewHolder.desName.setText(destination);
        return view;
    }

    class ViewHolder {
        ImageView desImage;
        TextView desName;
    }
}
