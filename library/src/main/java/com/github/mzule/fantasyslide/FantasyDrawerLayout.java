package com.github.mzule.fantasyslide;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoDongping on 9/6/16.
 */
public class FantasyDrawerLayout extends DrawerLayout implements DrawerLayout.DrawerListener {
    private SideBarWithBg leftSideBar;
    private SideBarWithBg rightSideBar;
    private SideBarWithBg currentSideBar;
    private View contentLayout;
    private List<SideBar> sideBarChildren = new ArrayList<>();
    private float slideOffset;
    private float y;

    public FantasyDrawerLayout(Context context) {
        super(context);
    }

    public FantasyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FantasyDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        classifyChildren();
        wrapperSideBars();

        addDrawerListener(this);
    }

    public void openDrawer(View drawerView) {
        super.openDrawer(drawerView);
        currentSideBar = (SideBarWithBg) drawerView;
    }

    public void openDrawer(int gravity) {
        super.openDrawer(gravity);
        currentSideBar = GravityUtil.isLeft(gravity) ? leftSideBar : rightSideBar;
    }

    /**
     * 对 child view 进行分裂,得到一个 contentLayout 和 两个 SideBar
     */
    private void classifyChildren() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof SideBar) {
                sideBarChildren.add((SideBar) child);
            } else {
                if (contentLayout != null) {
                    throw new IllegalStateException("Multiple Content layout found.");
                }
                contentLayout = child;
            }
        }
        if (contentLayout == null) {
            throw new IllegalStateException("Content layout not found.");
        }
    }

    /**
     * 包装  SideBar 成 SideBarWithBg,这样可以显示背景
     */
    private void wrapperSideBars() {
        for (SideBar sideBar : sideBarChildren) {
            removeView(sideBar);
            SideBarWithBg sideBarWithBg = SideBarWithBg.wrapper(sideBar);
            addView(sideBarWithBg);
            if (GravityUtil.isLeft(sideBarWithBg)) {
                leftSideBar = sideBarWithBg;
            } else if (GravityUtil.isRight(sideBarWithBg)) {
                rightSideBar = sideBarWithBg;
            } else {
                throw new IllegalStateException("unsupported gravity");
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (leftSideBar != null && rightSideBar != null) {
                if (isDrawerOpen(leftSideBar)) {
                    currentSideBar = leftSideBar;
                } else if (isDrawerOpen(rightSideBar)) {
                    currentSideBar = rightSideBar;
                } else if (ev.getX() < getWidth() / 2) {
                    currentSideBar = leftSideBar;
                } else {
                    currentSideBar = rightSideBar;
                }
            } else {
                currentSideBar = leftSideBar != null ? leftSideBar : rightSideBar;
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            closeDrawers();
            currentSideBar.onMotionEventUp();
            return super.dispatchTouchEvent(ev);
        }
        y = ev.getY();
        if (slideOffset < 1) {
            super.dispatchTouchEvent(ev);
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            currentSideBar.setTouchY(y, slideOffset);
        }
        return true;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        this.slideOffset = slideOffset;
        currentSideBar.setTouchY(y, slideOffset);
        float translationX = drawerView.getWidth() * slideOffset / 2; // translation 为 sideBar 宽度的一半
        if (GravityUtil.isLeft(GravityUtil.getGravity(drawerView))) {
            contentLayout.setTranslationX(translationX);
        } else {
            contentLayout.setTranslationX(-translationX);
        }
        y = slideOffset == 0 ? 0 : y; // onDrawerClosed 并不是每次都会被触发,所以需要在这里判断。
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }
}
