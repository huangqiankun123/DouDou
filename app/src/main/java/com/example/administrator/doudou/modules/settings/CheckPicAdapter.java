package com.example.administrator.doudou.modules.settings;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.administrator.doudou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PandaQ on 2017/3/30.
 * email:767807368@qq.com
 */

public class CheckPicAdapter extends BaseAdapter {
    private ArrayList<String> mPicPaths;
    private Context mContext;

    public CheckPicAdapter(Context context, ArrayList<String> picPaths) {
        mPicPaths = picPaths;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPicPaths == null ? 0 : mPicPaths.size();
    }

    @Override
    public String getItem(int position) {
        return mPicPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.check_pic_item, null, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String path = mPicPaths.get(position);
        if (path.equals("ic_action_camera")){
            Picasso.with(mContext)
                    .load(R.mipmap.ic_action_camera)
                    .fit()
                    .centerInside()
                    .into(holder.mIvPic);
        }else {
            Picasso.with(mContext)
                    .load("file://" + mPicPaths.get(position))
                    .fit()
                    .centerCrop()
                    .into(holder.mIvPic);
        }
        System.out.println(position);
        System.out.println(mPicPaths.get(position));

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView mIvPic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setPicPaths(ArrayList<String> picPaths) {
        mPicPaths = picPaths;
        notifyDataSetChanged();
    }
}
