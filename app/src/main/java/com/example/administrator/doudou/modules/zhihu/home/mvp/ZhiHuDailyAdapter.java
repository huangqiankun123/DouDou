package com.example.administrator.doudou.modules.zhihu.home.mvp;



import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.doudou.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Administrator on 2017/5/2.
 */

public class ZhiHuDailyAdapter extends BaseQuickAdapter<ZhiHuDaily.StoriesBean,BaseViewHolder> {
    public ZhiHuDailyAdapter(@LayoutRes int layoutResId, @Nullable List<ZhiHuDaily.StoriesBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZhiHuDaily.StoriesBean item) {
        helper.setText(R.id.news_title,item.getTitle());
        String image = item.getImages().get(0);
        if (!TextUtils.isEmpty(image)){
           Picasso.with(mContext).load(image).into((ImageView) helper.getView(R.id.news_image));
//            helper.setImageResource(R.id.news_image, Integer.parseInt(image));
        }

    }
//    private static final int TYPE_DATE = 0;
//    private static final int TYPE_CONTENT = 1;
//
//    private Context context;
//    private List<ZhiHuDaily.StoriesBean> zhiHuDailyList;
//
//    public ZhiHuDailyAdapter(List<ZhiHuDaily.StoriesBean> zhiHuDailyList, Context context) {
//        this.zhiHuDailyList = zhiHuDailyList;
//        this.context = context;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.zhihu_story_item, parent, false);
//        //第三步  在onCreateViewHolder()中为每个item添加点击事件
//        view.setOnClickListener(this);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.newsTitle.setText(zhiHuDailyList.get(position).getTitle());
//        String image = zhiHuDailyList.get(position).getImages().get(0);
//        if (!TextUtils.isEmpty(image)){
//            Picasso.with(context).load(image).into(holder.newsImage);
//        }
//        //第五步  ：注意上面调用接口的onItemClick()中的v.getTag()方法，这需要在onBindViewHolder()方法中设置和item的position
//        holder.itemView.setTag(position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return zhiHuDailyList.size();
//    }
//
//
////    @Override
////    public int getItemViewType(int position) {
////
////    }
//
//
//
//    class MyViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.news_image)
//        ImageView newsImage;
//        @BindView(R.id.news_title)
//        TextView newsTitle;
//        @BindView(R.id.item_cardview)
//        CardView itemCardview;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//
//            ButterKnife.bind(this,itemView);
//
//        }
//    }
//
//    class DateViewHolder extends RecyclerView.ViewHolder {
//
//
//        public DateViewHolder(View itemView) {
//            super(itemView);
//        }
//    }
//    // 第二步 声明接口
//    private OnItemClickListener mOnItemClickListener = null;
//    //define interface 第一步 定义接口
//    public static interface OnItemClickListener {
//        void onItemClick(View view , int position);
//    }
//    //第六步  ：最后暴露给外面的调用者，定义一个设置Listener的方法（）：
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//    //第四步  ：将点击事件转移给外面的调用者：
//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener!=null){
//            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
//        }
//    }



}
