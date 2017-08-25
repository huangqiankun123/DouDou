package com.example.administrator.doudou.modules.news;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.doudou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/4.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.HeadLineViewHolder> implements View.OnClickListener {


    private Context context;
    private ArrayList<NewsBean> newsBeanList;
    private int image_width;
    private int image_height;

    public NewsListAdapter(Context context, ArrayList<NewsBean> newsBeanList) {
        this.context = context;
        this.newsBeanList = newsBeanList;
        float value = context.getResources().getDimension(R.dimen.video_type_card_height);
        image_height = (int) value;
        image_width = (int) value * 4 / 3;
    }

    @Override
    public HeadLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_headline, parent, false);
        //3
        view.setOnClickListener(this);
        return new HeadLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeadLineViewHolder holder, int position) {
        holder.newsTitle.setText(newsBeanList.get(position).getTitle());
        String imgsrc = newsBeanList.get(position).getImgsrc();
        if (!TextUtils.isEmpty(imgsrc)){
            Picasso.with(context).load(newsBeanList.get(position).getImgsrc()).resize(image_width,image_height).into(holder.newsImage);
        }
        holder.source.setText(newsBeanList.get(position).getSource());
        //5
        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return newsBeanList.size();
    }



    static class HeadLineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_image)
        ImageView newsImage;
        @BindView(R.id.news_title)
        TextView newsTitle;
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.item_card)
        CardView itemCard;

        public HeadLineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }



    //1
    public static interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    //2
    private OnItemClickListener mOnItemClickListener=null;

    //6
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener =listener;
    }

    //4
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }


    public void setData(ArrayList<NewsBean> newsBeanLists){
        newsBeanList = newsBeanLists;
        notifyDataSetChanged();
    }
}
