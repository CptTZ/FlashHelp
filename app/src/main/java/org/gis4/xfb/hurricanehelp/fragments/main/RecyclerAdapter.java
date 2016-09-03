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

import com.amap.api.location.AMapLocation;
import com.avos.avoscloud.AVGeoPoint;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.activity.TaskDetailsActivity;
import org.gis4.xfb.hurricanehelp.activity.TaskLandingActivity;
import org.gis4.xfb.hurricanehelp.data.XfbTask;

/**
 * “发现”里的列表适配器
 * Created by chi on 2016-8-23.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private List<XfbTask> mDataSet;
    private AMapLocation userLocation;

    public RecyclerAdapter(Context context, List<XfbTask> dataSet, AMapLocation loc) {
        mContext = context;
        mDataSet = dataSet;
        userLocation = loc;
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
        if(mDataSet.get(position).getTaskState() == 0) {
            Picasso.with(mContext).load(XfbTask.getLogoOfTaskType(mDataSet.get(position).getTaskType())).into(holder.itemTaskType);
        }
        else {
            Picasso.with(mContext).load(XfbTask.getLogoOfTaskType(mDataSet.get(position).getTaskState())).into(holder.itemTaskType);
        }
        holder.itemDesc.setText(mDataSet.get(position).getDesc());
        String rewPnt = "奖励积分：" + mDataSet.get(position).getRewardPoint();
        holder.itemRewardPoint.setText(rewPnt);
        String disTxt;
        if(userLocation==null) { disTxt="距离：未定位，无法计算"; } else
        {
            double dist = mDataSet.get(position).getHappenGeoLocation().
                    distanceInKilometersTo(new AVGeoPoint(
                            userLocation.getLatitude(),userLocation.getLongitude()) );
            if (dist - 1 < 0) {
                disTxt = "距离：" + String.format(Locale.CHINA, "%1$.0f", dist*1000) + "米";
            } else {
                disTxt = "距离：" + String.format(Locale.CHINA, "%1$.1f", dist) + "公里";
            }
        }
        holder.itemDistance.setText(disTxt);

        holder.itemRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class targetActivity;
                if(mDataSet.get((int)v.getTag()).getTaskState() == 0)
                    targetActivity = TaskLandingActivity.class;
                else
                    targetActivity = TaskDetailsActivity.class;

                Intent intent =new Intent(mContext, targetActivity);
                Bundle bundle=new Bundle();
                bundle.putParcelable("xfbTask", mDataSet.get((int)v.getTag()));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * remove功能暂时不实现
     */
    public void remove(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * add功能暂时不实现
     */
    public void add(XfbTask task, int position) {
        mDataSet.add(position, task);
        notifyItemInserted(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout itemRelativeLayout;
        public ImageView itemTaskType;
        public TextView itemDesc;
        public TextView itemRewardPoint;
        public TextView itemDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            itemRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_relativelayout);
            itemTaskType = (ImageView) itemView.findViewById(R.id.item_task_type);
            itemDesc = (TextView) itemView.findViewById(R.id.item_desc);
            itemRewardPoint = (TextView) itemView.findViewById(R.id.item_reward_point);
            itemDistance = (TextView) itemView.findViewById(R.id.item_distance);
        }
    }
}
