package com.github.mzule.fantasyslide;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 封装 SideBar 以及 SideBarBgView,保证 SideBarBgView 可以展示在 SideBar 背后
 * Created by CaoDongping on 9/6/16.
 */
class SideBarWithBg extends RelativeLayout {

    private SideBarBgView bgView;
    private SideBar sideBar;

    public SideBarWithBg(Context context) {
        super(context);
    }

    public SideBarWithBg(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBarWithBg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static SideBarWithBg wrapper(SideBar sideBar) {
        SideBarWithBg layout = new SideBarWithBg(sideBar.getContext());
        layout.init(sideBar);
        return layout;
    }

    private void init(SideBar sideBar) {
        setLayoutParams(sideBar.getLayoutParams());

        int parentLayoutGravity = ((DrawerLayout.LayoutParams) getLayoutParams()).gravity;
        this.sideBar = sideBar;
        bgView = new SideBarBgView(getContext());
        bgView.setParentLayoutGravity(parentLayoutGravity);
        addView(bgView, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // 将背景色转移给 bgView, 并清除 sideBar 自身的背景色
        bgView.setDrawable(sideBar.getBackground());
        sideBar.setBackgroundColor(Color.TRANSPARENT);
        sideBar.setParentLayoutGravity(parentLayoutGravity);

        addView(sideBar, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    void setTouchY(float y, float percent) {
        bgView.setTouchY(y, percent);
        sideBar.setTouchY(y, percent);
    }

    public void onMotionEventUp() {
        sideBar.onMotionEventUp();
    }
}
