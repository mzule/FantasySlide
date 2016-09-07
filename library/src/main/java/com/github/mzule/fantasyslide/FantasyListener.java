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
     * @return 返回 true 表示消费了事件
     */
    boolean onHover(@Nullable View view);

    /**
     * 用户选中的 view
     *
     * @param view 被选中的 view
     * @return 返回 true 表示消费了事件
     */
    boolean onSelect(View view);

    /**
     * 用户未选中 view,被取消
     */
    void onCancel();
}
