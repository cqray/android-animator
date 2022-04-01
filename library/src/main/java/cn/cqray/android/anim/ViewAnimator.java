package cn.cqray.android.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 控件动画
 * @author Cqray
 */
@Accessors(prefix = "m", chain = true, fluent = true)
public class ViewAnimator {

    /** 插值器 **/
    @Setter
    private Interpolator mInterpolator;
    /** 动画集合 **/
    private AnimatorSet mAnimatorSet;
    /** 需要执行的事件 **/
    private final List<Runnable> mActionList;
    /** 动画构建器集合 **/
    @Getter
    private final List<AnimatorBuilder> mBuilderList;
    /** 整个动画的监听事件 **/
    @Getter
    private final List<AnimatorListener> mAnimatorListeners;

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

    private ViewAnimator(@Nullable LifecycleOwner owner) {
        mActionList = Collections.synchronizedList(new ArrayList<>());
        mBuilderList = Collections.synchronizedList(new ArrayList<>());
        mAnimatorListeners = Collections.synchronizedList(new ArrayList<>());
        if (owner != null) {
            owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    cancel();
                }
            });
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

    public ViewAnimator addAnimatorListener(AnimatorListener listener) {
        if (listener != null) {
            mAnimatorListeners.add(listener);
        }
        return this;
    }

    public void start() {
        post(this::startAnimator);
        doWhenViewReady();
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
        int[] duration = new int[2];
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

    @SuppressLint("NewApi")
    public void getDuration(Runnable consumer) {
        post(() -> {
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //consumer.accept(getDuration());
            //}
        });
    }

    void post(Runnable r) {
        mActionList.add(r);
    }

    /**
     * 开始动画
     * <p>整合各个控件的属性动画</p>
     */
    void startAnimator() {
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
                animators.addAll(ab.generateAnimators(duration[0]));
                duration[0] += ab.getDuration() + ab.getDelay();
                duration[1] = duration[0];
            } else {
                // PlayOn、PlayWith，动画前延时为之前动画用时
                animators.addAll(ab.generateAnimators(duration[0]));
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

    /**
     * 控件准备完毕才执行任务
     */
    void doWhenViewReady() {
        if (!mBuilderList.isEmpty()) {
            List<View> views = mBuilderList.get(0).views();
            if (views.isEmpty()) {
                return;
            }
            Runnable runnable = () -> {
                while (!mActionList.isEmpty()) {
                    mActionList.remove(0).run();
                }
            };
            View view = views.get(0);
            boolean notMeasured = view.getMeasuredWidth() == 0 && view.getMeasuredHeight() == 0;
            if (notMeasured) {
                view.post(runnable);
            } else {
                runnable.run();
            }
        }
    }
}
