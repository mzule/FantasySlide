package com.github.mzule.fantasyslide;

import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

/**
 * Created by CaoDongping on 9/7/16.
 */
class GravityUtil {

    static boolean isLeft(int gravity) {
        return gravity == Gravity.START || gravity == Gravity.LEFT;
    }

    static boolean isLeft(View view) {
        return isLeft(getGravity(view));
    }

    static boolean isRight(int gravity) {
        return gravity == Gravity.END || gravity == Gravity.RIGHT;
    }

    static boolean isRight(View view) {
        return isRight(getGravity(view));
    }

    static int getGravity(View view) {
        if (view.getLayoutParams() instanceof DrawerLayout.LayoutParams) {
            return ((DrawerLayout.LayoutParams) view.getLayoutParams()).gravity;
        }
        throw new IllegalArgumentException("Not child of DrawerLayout");
    }
}
