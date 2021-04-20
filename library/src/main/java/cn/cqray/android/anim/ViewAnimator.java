package cn.cqray.android.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 控件动画
 * @author Cqray
 * @date 2021/4/7 11:55
 */
public class ViewAnimator implements LifecycleEventObserver, LifecycleObserver {

    /** 插值器 **/
    private Interpolator mInterpolator;
    /** 动画集合 **/
    private AnimatorSet mAnimatorSet;
    /** 动画构建器集合 **/
    private List<AnimatorBuilder> mBuilderList;
    /** 整个动画的监听事件 **/
    private List<Animator.AnimatorListener> mAnimatorListeners;
    /** 需要执行的事件 **/
    private LinkedList<Runnable> mActionList;

    /**
     * 初始化动画对象（需手动管理动画生命周期）
     * @param targets 目标View
     * @return 动画对象
     */
    @NonNull
    public static AnimatorBuilder playOn(@NonNull View... targets) {
        return playOn(null, targets);
    }

    /**
     * 初始化动画对象（自动管理动画生命周期）
     * @param owner LifecycleOwner不为空，将自行管理动画生命周期
     * @param targets 目标View
     * @return 动画对象
     */
    @NonNull
    @MainThread
    public static AnimatorBuilder playOn(@Nullable LifecycleOwner owner, @NonNull View... targets) {
        ViewAnimator va = new ViewAnimator(owner);
        AnimatorBuilder ab = new AnimatorBuilder(va, false, targets);
        va.mBuilderList.add(ab);
        return ab;
    }

    private ViewAnimator(LifecycleOwner owner) {
        mBuilderList = new ArrayList<>();
        mAnimatorListeners = new ArrayList<>();
        mActionList = new LinkedList<>();
        if (owner != null) {
            owner.getLifecycle().addObserver(this);
        }
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            cancel();
        }
    }

    @NonNull
    public AnimatorBuilder playWith(@NonNull View... targets) {
        AnimatorBuilder ab = new AnimatorBuilder(this, false, targets);
        mBuilderList.add(ab);
        return ab;
    }

    @NonNull
    public AnimatorBuilder playThen(@NonNull View... targets) {
        AnimatorBuilder ab = new AnimatorBuilder(this, true, targets);
        mBuilderList.add(ab);
        return ab;
    }

    public ViewAnimator interpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    public List<AnimatorBuilder> getAnimationBuilders() {
        return mBuilderList;
    }

    public ViewAnimator addAnimatorListener(Animator.AnimatorListener listener) {
        if (listener != null) {
            mAnimatorListeners.add(listener);
        }
        return this;
    }

    public void start() {
        post(new Runnable() {
            @Override
            public void run() {
                startAnimator();
            }
        });
        doAfterViewReady();
    }

    public void cancel() {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    public boolean isRunning() {
        if (mAnimatorSet != null) {
            return mAnimatorSet.isRunning();
        }
        return false;
    }

    /**
     * 获取动画总时长，需要在start()后调用才有值
     * @return 动画总时长
     */
    public int getDuration() {
        int [] duration = new int[2];
        // 正常动画
        for (AnimatorBuilder ab : mBuilderList) {
            if (ab.isPlayThen()) {
                // PlayThen动动画已用时从[1]取，获取之前PlayOn、PlayWith中最大已用时
                duration[0] = duration[1];
                duration[0] += ab.getDuration() + ab.getDelay();
                duration[1] = duration[0];
            } else {
                // 新动画将用时临时值
                duration[1] = Math.max(duration[0] + ab.getDuration() + ab.getDelay(), duration[1]);
            }
        }
        return duration[1];
    }

    void post(Runnable r) {
        mActionList.add(r);
    }

    private void startAnimator() {
        cancel();
        mAnimatorSet = new AnimatorSet();
        // 动画用时,[0]已用时，[1]新动画将用时临时值
        int [] duration = new int[2];
        List<ObjectAnimator> animators = new ArrayList<>();
        // 正常动画
        for (AnimatorBuilder ab : mBuilderList) {
            if (ab.isPlayThen()) {
                // PlayThen动动画已用时从[1]取，获取之前PlayOn、PlayWith中最大已用时
                duration[0] = duration[1];
                animators.addAll(ab.getAnimators(duration[0]));
                duration[0] += ab.getDuration() + ab.getDelay();
                duration[1] = duration[0];
            } else {
                // PlayOn、PlayWith，动画前延时为之前动画用时
                animators.addAll(ab.getAnimators(duration[0]));
                // 新动画将用时临时值
                duration[1] = Math.max(duration[0] + ab.getDuration() + ab.getDelay(), duration[1]);
            }
        }
        ObjectAnimator[] animatorArray = animators.toArray(new ObjectAnimator[]{});
        if (animatorArray.length > 0) {
            mAnimatorSet.playTogether(animatorArray);
        }
        mAnimatorSet.setInterpolator(mInterpolator);
        for (Animator.AnimatorListener listener : mAnimatorListeners) {
            mAnimatorSet.addListener(listener);
        }
        mAnimatorSet.start();
    }

    private void doAfterViewReady() {
        final View view = mBuilderList.get(0).getViews().get(0);
        if (view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    while (!mActionList.isEmpty()) {
                        mActionList.removeFirst().run();
                    }
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        } else {
            while (!mActionList.isEmpty()) {
                mActionList.removeFirst().run();
            }
        }
    }
}
