package com.example.administrator.doudou.modules.video.videohome.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.doudou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/4.
 */
public class VideoHomeAdapter extends RecyclerView.Adapter<VideoHomeAdapter.VideoHomeViewHolder> {

    private Context context;
    private ArrayList<RetDataBean.ListBean> listBeen;

    public VideoHomeAdapter(Context context, ArrayList<RetDataBean.ListBean> listBeen) {
        this.context = context;
        this.listBeen = listBeen;
    }

    @Override
    public VideoHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_videohoem, parent, false);
        return new VideoHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHomeViewHolder holder, int position) {

        String pic = listBeen.get(position).getChildList().get(0).getPic();
        if (!TextUtils.isEmpty(pic)){
            Picasso.with(context).load(pic).into(holder.ivVideoType);
        }
        holder.tvVideoType.setText(listBeen.get(position).getChildList().get(0).getTitle());
    }

    @Override
    public int getItemCount() {
        return listBeen.size();
    }

    static class VideoHomeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_video_type)
        ImageView ivVideoType;
        @BindView(R.id.tv_video_type)
        TextView tvVideoType;
        @BindView(R.id.rl_parent)
        RelativeLayout rlParent;
        public VideoHomeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
