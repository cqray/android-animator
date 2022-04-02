# android-animator

### 介绍
简单的动画动画封装

![](/img/animator1.gif)
![](/img/animator2.gif)

### 引入

#### 第一步
在根build.gradle文件中添加如下代码
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
#### 第二步
添加依赖
```
implementation 'com.github.cqray:android-animator:0.5.8'
```

### 如何使用
```        
// 可同时在多个控件上播放相同动画。
// LifecyclerOwner不为空，则退出会自动cancel()
// playThen，上个动画播放完在播发
// playWith，与上个动画同时播放
ViewAnimator.playOn(lifecyclerOwner, view)
        .slideBottomIn()
        .duration(1000)
        .playThen(view)
        .slideTopIn()
        .playWith(view)
        .slideLeftIn()
        .start();
```
ViewAnimator可在子线程使用，更多使用请阅读源码。
