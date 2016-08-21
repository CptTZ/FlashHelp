package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends BaseFragment
{
    private ListView mListview;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private XfbTask[] taskData;

    public static ActivitiesFragment instance() {
        ActivitiesFragment view = new ActivitiesFragment();
        return view;
    }

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();

        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) getActivity().findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                new Task().execute();
            }
        });

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveRGBColor(63, 81, 181);
        mListview = (ListView) getActivity().findViewById(R.id.main_list);

        mListview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), taskData[0].getDesc(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //// TODO: 2016-8-21 没有考虑到请求超时的情况，可以在请求的代码中解决。
    private class Task extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            taskData = XfbTask.taskSample();
            return String.valueOf(taskData.length);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "共刷新" + result + "条记录", Toast.LENGTH_SHORT).show();
            setSampleData();
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    private void setSampleData() {
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();

        for(int n = 0; n < taskData.length; n++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_task_type", taskData[n].getTaskType());
            map.put("item_desc", taskData[n].getDesc());
            map.put("item_reward_point", taskData[n].getRewardPoint());
            map.put("item_shadow",(n == 0 ? false : true));
            mylist.add(map);
        }
        mListview.setAdapter(new ListViewAdapter(mylist));

    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(final ArrayList<HashMap<String, Object>> mList) {
            itemViews = new View[mList.size()];

            for (int i = 0; i < itemViews.length; i++) {
                itemViews[i] = makeItemView(
                        (String) mList.get(i).get("item_task_type"),
                        (String) mList.get(i).get("item_desc"),
                        (int) mList.get(i).get("item_reward_point"),
                        (boolean)mList.get(i).get("item_shadow"));
            }
        }

        @Override
        public int getCount() {
            return itemViews.length;
        }

        @Override
        public Object getItem(int position) {
            return itemViews[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }

        private View makeItemView(String item_task_type, String item_desc,
                                  int item_reward_point, boolean item_shadow) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.fragment_activities_list_item, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            ImageView image = (ImageView) itemView.findViewById(R.id.item_task_type);
            HashMap<String, Integer> imageList = XfbTask.imageList();
            image.setImageDrawable(getResources().getDrawable(imageList.get(item_task_type)));

            TextView itemDesc = (TextView) itemView.findViewById(R.id.item_desc);
            itemDesc.setText(item_desc);
            TextView itemRewardPoint = (TextView) itemView.findViewById(R.id.item_reward_point);
            itemRewardPoint.setText("奖励积分：" + item_reward_point);
            //// TODO: 2016-8-21 这里还少一个根据经纬度计算当前定位地点到任务地点的距离

            ImageView itemshadow = (ImageView) itemView.findViewById(R.id.item_shadow);
            //还有点问题。
            //itemhadow.setVisibility(item_shadow ? View.VISIBLE : View.GONE);

            return itemView;
        }

    }

}
