package com.github.mzule.fantasyslide;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by CaoDongping on 9/6/16.
 */
public class SideBar extends LinearLayout {
    private int parentLayoutGravity;
    private boolean opened;
    private Transformer transformer;
    private FantasyListener fantasyListener;


    public SideBar(Context context) {
        super(context);
        init(null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(VERTICAL);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SideBar);
            float maxTranslationX = a.getDimension(R.styleable.SideBar_maxTranslationX, 0);
            transformer = new DefaultTransformer(maxTranslationX);
            a.recycle();
        }
    }

    void setTouchY(float y, float percent) {
        opened = percent == 1;
        int childCount = getChildCount();
        boolean found = false;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setPressed(false);
            boolean inHover = opened && child.getTop() < y && child.getBottom() > y;
            if (inHover) {
                found = true;
                if (fantasyListener == null || !fantasyListener.onHover(child, i)) {
                    child.setPressed(true);
                }
            }
            transformer.apply((ViewGroup) getParent(), child, y, percent, GravityUtil.isLeft(parentLayoutGravity));
        }
        if (opened && !found && fantasyListener != null) {
            fantasyListener.onHover(null, -1);
        }
    }

    void onMotionEventUp() {
        for (int i = 0; opened && i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.isPressed()) {
                if (fantasyListener == null || !fantasyListener.onSelect(child, i)) {
                    child.performClick();
                }
                return;
            }
        }
        if (fantasyListener != null) {
            fantasyListener.onCancel();
        }
    }

    void setParentLayoutGravity(int parentLayoutGravity) {
        this.parentLayoutGravity = parentLayoutGravity;
    }

    public void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    public void setFantasyListener(FantasyListener fantasyListener) {
        this.fantasyListener = fantasyListener;
    }
}
