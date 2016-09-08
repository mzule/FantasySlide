package com.github.mzule.fantasyslide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * 背景视图,根据设定的 color 以及 y 位移绘制曲线背景。
 * Created by CaoDongping on 9/6/16.
 */
class SideBarBgView extends View {
    private Paint paint;
    private Path path;
    private int parentLayoutGravity;
    private boolean drawableProcessed;
    private Drawable drawable;

    public SideBarBgView(Context context) {
        super(context);
        init();
    }

    public SideBarBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBarBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
    }

    void setParentLayoutGravity(int parentLayoutGravity) {
        this.parentLayoutGravity = parentLayoutGravity;
    }

    public void setTouchY(float y, float percent) {
        path.reset();
        float width = getWidth() * percent;
        float height = getHeight();
        float arcWidth = width / 2; // 宽度的一般作为贝瑟尔曲线宽度
        float yOffset = height / 8; // 贝塞尔曲线高度的 offset,以达到背景的上下宽度变化
        if (GravityUtil.isLeft(parentLayoutGravity)) {
            path.lineTo(width - arcWidth, -yOffset);
            path.quadTo(width + arcWidth, y, width - arcWidth, height + yOffset);
            path.lineTo(0, height);
            path.close();
            path.offset(getWidth() - width, 0);
        } else {
            path.moveTo(arcWidth, -yOffset);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(arcWidth, height + yOffset);
            path.quadTo(-arcWidth, y, arcWidth, -yOffset);
            path.close();
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable != null && !drawableProcessed) {
            drawableProcessed = true;
            paint.setShader(createShader());
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }

    @NonNull
    private Shader createShader() {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(c);
        }
        return new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    public void setDrawable(Drawable drawable) {
        if (drawable instanceof ColorDrawable) {
            paint.setColor(((ColorDrawable) drawable).getColor());
        } else {
            this.drawable = drawable;
            this.drawableProcessed = false;
        }
    }
}
