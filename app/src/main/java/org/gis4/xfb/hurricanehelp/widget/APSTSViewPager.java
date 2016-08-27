package org.gis4.xfb.hurricanehelp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * APSTS首页滑动显示类
 * Created by Tony on 2016/7/26.
 */
public class APSTSViewPager extends ViewPager
{
    private boolean mNoFocus = false; //if true, keep View don't move

    public APSTSViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public APSTSViewPager(Context context)
    {
        this(context, null);
    }

    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mNoFocus)
        {
            return false;
        }
        try {
            return super.onInterceptTouchEvent(event);
        }
        catch (Exception e){
            return false;
        }
    }

    public void setNoFocus(boolean b)
    {
        mNoFocus = b;
    }
}
