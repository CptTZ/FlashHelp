package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.activity.TaskLandingActivity;
import org.gis4.xfb.hurricanehelp.data.XfbTask;

/**
 * “发现”里的列表适配器
 * Created by chi on 2016-8-23.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<XfbTask> mDataSet;

    public RecyclerAdapter(Context context, List<XfbTask> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_activities_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //把当前item的index存在RelativeLayout的Tag中，方便点击事件的实现
        holder.itemRelativeLayout.setTag(position);
        HashMap<String, Integer> imageList = XfbTask.imageList();
        Picasso.with(mContext).load(imageList.get(mDataSet.get(position).getTaskType())).into(holder.itemTaskType);
        holder.itemDesc.setText(mDataSet.get(position).getDesc());
        holder.itemRewardPoint.setText("奖励积分：" + mDataSet.get(position).getRewardPoint());

        holder.itemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(mContext,TaskLandingActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("xfbTask", mDataSet.get((int)v.getTag()));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void remove(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    //add功能暂时不实现，没有这个需求。
    public void add(String text, int position) {
        //mDataSet.add(position, text);
        //notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout itemRelativeLayout;
        public ImageView itemTaskType;
        public TextView itemDesc;
        public TextView itemRewardPoint;

        // TODO: 2016-8-21 这里还少一个根据经纬度计算当前定位地点到任务地点的距离
        // ZYC COMMENT FOR ABOVE: 未来使用LeanCloud中的CQL直接计算

        public ViewHolder(View itemView) {
            super(itemView);
            itemRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_relativelayout);
            itemTaskType = (ImageView) itemView.findViewById(R.id.item_task_type);
            itemDesc = (TextView) itemView.findViewById(R.id.item_desc);
            itemRewardPoint = (TextView) itemView.findViewById(R.id.item_reward_point);
        }
    }
}
