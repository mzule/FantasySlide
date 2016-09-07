package com.github.mzule.fantasyslide;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CaoDongping on 9/7/16.
 */

class DefaultTransformer implements Transformer {
    private float maxTranslationX;

    DefaultTransformer(float maxTranslationX) {
        this.maxTranslationX = maxTranslationX;
    }

    @Override
    public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
        float translationX;
        int centerY = itemView.getTop() + itemView.getHeight() / 2;
        float distance = Math.abs(touchY - centerY);
        float scale = distance / sideBar.getHeight() * 3; // 距离中心点距离与 sideBar 的 1/3 对比
        if (isLeft) {
            translationX = Math.max(0, maxTranslationX - scale * maxTranslationX);
        } else {
            translationX = Math.min(0, maxTranslationX - scale * maxTranslationX);
        }
        itemView.setTranslationX(translationX * slideOffset);
    }
}
