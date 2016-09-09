# FantasySlide

一个 DrawerLayout 的扩展,具有帅气的动画与创新的交互。一次手势完成滑出侧边栏与选择菜单。欢迎下载 demo 体验。

<https://raw.githubusercontent.com/mzule/FantasySlide/master/demo.apk>

## 效果

![](https://raw.githubusercontent.com/mzule/FantasySlide/master/sample.gif)


## 使用方法

### 添加依赖

``` groovy
compile 'com.github.mzule.fantasyslide:library:1.0.4'
```

### 调用

调用方法基本与 DrawerLayout 一致. 本项目支持左右 (start left end right) 侧边栏同时定义。

``` xml
<com.github.mzule.fantasyslide.FantasyDrawerLayout xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/drawerLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="centerCrop"
        android:src="@drawable/fake" />

    <com.github.mzule.fantasyslide.SideBar
        android:id="@+id/leftSideBar"
        android:layout_width="200dp"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:background="@color/colorPrimary"
        app:maxTranslationX="66dp">
        
        <!-- 这里是 SideBar 的子视图-->
        
    </com.github.mzule.fantasyslide.SideBar>
    <!-- 如果需要的话，可以添加右侧边栏-->
</com.github.mzule.fantasyslide.FantasyDrawerLayout>

```

1. 最外层的 FantasyDrawerLayout 的使用与官方的 DrawerLayout 完全一致。
2. SideBar 用来包装每一个菜单项，SideBar 本质上可以当做一个 vertical 的 LinearLayout 来使用。
3. 效果图上的文字变色是表示该菜单处于 hover 状态, hover 状态默认会设定 view 的 pressed 状态为 true。可以通过 Listener 来改写, 下文会有详细说明。
4. 详细参考 <https://github.com/mzule/FantasySlide/blob/master/app/src/main/res/layout/activity_main.xml>



## 进阶

### maxTranslationX

通过设置 maxTranslationX 可以设置菜单项动画的最大位移。仅有在采用默认 Transformer 时才有效。

``` xml
<com.github.mzule.fantasyslide.SideBar
	...
    app:maxTranslationX="88dp">
```
一般情况下，左边的侧边栏 maxTranslationX 为正数，右边的侧边栏 maxTranslationX 为负数。


### Listener

支持设置 Listener 来监听侧边栏菜单的状态。

``` java
SideBar leftSideBar = (SideBar) findViewById(R.id.leftSideBar);
leftSideBar.setFantasyListener(new SimpleFantasyListener() {
    @Override
    public boolean onHover(@Nullable View view) {
    	return false;
    }

    @Override
    public boolean onSelect(View view) {
        return false;
    }

    @Override
    public void onCancel() {
    }
});
```

1. Hover 是指上面效果图中，高亮的状态，此时手指仍在屏幕上 move. 默认的 hover 处理逻辑是设置 view 的 pressed 状态为 true. 重写 onHover(View) 方法返回 true 可以改写默认逻辑。
2. Select 是指 hover 状态时手指离开屏幕，触发 select 状态。默认的处理逻辑是调用 view 的 onClick 事件。重写 onSelect(View) 方法返回 true 可以改写默认逻辑。
3. Cancel 是指手指离开屏幕时，没有任何 view 触发 select 状态，则为 cancel，无默认处理逻辑。

### Transformer

项目设计了一个 Transformer 接口，供调用者自定义菜单项的动画效果。使用方法类似于 ViewPager 的 PageTransformer.

``` java
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
```

## 感谢

动画效果参考自 dribbble. <https://dribbble.com/shots/2269140-Force-Touch-Slide-Menu> 在此感谢。

另外，demo 里面 MainActivity 的右边栏实现了类似原作的菜单动画效果。具体可以参考相关代码。

## 许可

Apache License  2.0

## 联系我

任何相关问题都可以通过以下方式联系我。

1. 提 issue
1. 新浪微博 http://weibo.com/mzule
1. 个人博客 https://mzule.github.io/
1. 邮件 "mzule".concat("4j").concat("@").concat("gmail.com")
