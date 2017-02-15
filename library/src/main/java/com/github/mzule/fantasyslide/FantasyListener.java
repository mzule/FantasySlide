package com.github.mzule.fantasyslide;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by CaoDongping on 9/7/16.
 */

public interface FantasyListener {
    /**
     * View 处于 hover 状态
     *
     * @param view 处于 hover 状态的 view, 可能为空
     * @param index view 的 index，view 为空时 index 为 -1
     * @return 返回 true 表示消费了事件
     */
    boolean onHover(@Nullable View view, int index);

    /**
     * 用户选中的 view
     *
     * @param view 被选中的 view
     * @param index 被选中的 view 的 index
     * @return 返回 true 表示消费了事件
     */
    boolean onSelect(View view, int index);

    /**
     * 用户未选中 view,被取消
     */
    void onCancel();
}
