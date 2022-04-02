package cn.cqray.android.anim;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 动画构建器
 * @author Cqray
 */
@Accessors(prefix = "m", chain = true, fluent = true)
public final class AnimatorBuilder {
    /** 动画对象 **/
    private ViewAnimator mAnimator;
    /** 动画开始前延时时间 **/
    @Setter
    private int mDelay = 0;
    /** 动画时间 **/
    @Setter
    private int mDuration = 500;
    /** 重复次数 **/
    @Setter
    private int mRepeatCount = 0;
    /** 重复模式 **/
    @Setter
    private RepeatMode mRepeatMode = RepeatMode.RESTART;
    /** 插值器 **/
    @Setter
    private Interpolator mInterpolator;
    /** 是否是随后动画 **/
    private boolean mPlayThen;
    /** 控件列表 **/
    @Getter
    private List<View> mViews;
    /** 控件属性值集合 **/
    private Map<View, SparseArray<PropertyValuesHolder>> mHolderMap;

    AnimatorBuilder(ViewAnimator animator, boolean playThen, @NonNull View... views) {
        mAnimator = animator;
        mPlayThen = playThen;
        mViews = Collections.synchronizedList(new ArrayList<>());
        mHolderMap = new ConcurrentHashMap<>();
        for (View view : views) {
            if (view != null) {
                mViews.add(view);
                mHolderMap.put(view, new SparseArray<>());
            }
        }
    }

    public void start() {
        mAnimator.start();
    }

    @NonNull
    public ViewAnimator convert() {
        return mAnimator;
    }

    @NonNull
    public AnimatorBuilder playWith(@NonNull View... targets) {
        return mAnimator.playWith(targets);
    }

    @NonNull
    public AnimatorBuilder playThen(@NonNull View... targets) {
        return mAnimator.playThen(targets);
    }

    ////////////////////////////////////////////////////////////
    ///////////////////   基础属性动画START   ///////////////////
    ///////////////////////////////////////////////////////////

    @NonNull
    public AnimatorBuilder alpha(@NonNull float... values) {
        return ofFloat(null, AnimatorType.ALPHA, values);
    }

    @NonNull
    public AnimatorBuilder pivot(@NonNull float... values) {
        return pivotX(values).pivotY(values);
    }

    @NonNull
    public AnimatorBuilder pivotX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.PIVOT_X, values);
    }

    @NonNull
    public AnimatorBuilder pivotY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.PIVOT_Y, values);
    }

    @NonNull
    public AnimatorBuilder rotation(@NonNull float... values) {
        return ofFloat(null, AnimatorType.ROTATION, values);
    }

    @NonNull
    public AnimatorBuilder rotationX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.ROTATION_X, values);
    }

    @NonNull
    public AnimatorBuilder rotationY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.ROTATION_Y, values);
    }

    @NonNull
    public AnimatorBuilder scale(float... values) {
        return scaleX(values).scaleY(values);
    }

    @NonNull
    public AnimatorBuilder scaleX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.SCALE_X, values);
    }

    @NonNull
    public AnimatorBuilder scaleY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.SCALE_Y, values);
    }

    @NonNull
    public AnimatorBuilder translationX(@NonNull float... values) {
        return translationX(true, values);
    }

    @NonNull
    public AnimatorBuilder translationX(boolean useDip, @NonNull float... values) {
        float[] temp = values;
        if (useDip) {
            temp = dp2px(values);
        }
        return ofFloat(null, AnimatorType.TRANSLATION_X, temp);
    }

    @NonNull
    public AnimatorBuilder translationY(@NonNull float... values) {
        return translationY(true, values);
    }

    @NonNull
    public AnimatorBuilder translationY(boolean useDip, @NonNull float... values) {
        float[] temp = values;
        if (useDip) {
            temp = dp2px(values);
        }
        return ofFloat(null, AnimatorType.TRANSLATION_Y, temp);
    }

    ////////////////////////////////////////////////////////////
    ////////////////////   基础属性动画END   ////////////////////
    ///////////////////////////////////////////////////////////
    ////////////////////    组合动画START   ////////////////////
    ///////////////////////////////////////////////////////////

    @NonNull
    public AnimatorBuilder bounce() {
        return translationY(0, 0, -10, 0, -5, 0, 0);
    }

    @NonNull
    public AnimatorBuilder bounceIn() {
        return scaleX(0.3f, 1.05f, 0.9f, 1).scaleY(0.3f, 1.05f, 0.9f, 1).alpha(0, 1, 1, 1);
    }

    @NonNull
    public AnimatorBuilder bounceOut() {
        scaleX(1, 0.9f, 1.05f, 0.3f);
        scaleY(1, 0.9f, 1.05f, 0.3f);
        return alpha(1, 1, 1, 0);
    }

    @NonNull
    public AnimatorBuilder fadeIn() {
        return alpha(0, 0.25f, 0.5f, 0.75f, 1);
    }

    @NonNull
    public AnimatorBuilder fadeOut() {
        return alpha(1, 0.75f, 0.5f, 0.25f, 0);
    }

    @NonNull
    public AnimatorBuilder flash() {
        return alpha(1, 0, 1, 0, 1);
    }

    @NonNull
    public AnimatorBuilder pulse() {
        return scaleX(1, 1.1f, 1).scaleY(1, 1.1f, 1);
    }

    @NonNull
    public AnimatorBuilder rollLeftIn() {
        mAnimator.post(() -> {
            alpha(0, 1).rotation(-120, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, -view.getMeasuredWidth(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder rollRightIn() {
        mAnimator.post(() -> {
            alpha(0, 1).rotation(120, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, view.getMeasuredWidth(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder rollLeftOut() {
        mAnimator.post(() -> {
            alpha(1, 0).rotation(0, -120);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, 0, -view.getMeasuredWidth());
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder rollRightOut() {
        mAnimator.post(() -> {
            alpha(1, 0).rotation(0, 120);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, 0, view.getMeasuredWidth());
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder rubber() {
        return scaleX(1, 1.25f, 0.75f, 1.15f, 1).scaleY(1, 0.75f, 1.25f, 0.85f, 1);
    }

    @NonNull
    public AnimatorBuilder shakeX() {
        return translationX(0, 8, -8, 8, -8, 5, -5, 2, -2, 0);
    }

    @NonNull
    public AnimatorBuilder shakeY() {
        return translationY(0, 8, -8, 8, -8, 5, -5, 2, -2, 0);
    }

    @NonNull
    public AnimatorBuilder standUp() {
        mAnimator.post(() -> {
            for (View view : mViews) {
                float x = view.getMeasuredWidth() / 2f;
                float y = view.getMeasuredHeight();
                ofFloat(view, AnimatorType.ROTATION_X, 55, -30, 15, -15, 0);
                ofFloat(view, AnimatorType.PIVOT_X, x, x, x, x, x);
                ofFloat(view, AnimatorType.PIVOT_Y, y, y, y, y, y);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder swing() {
        return rotation(0, 10, -10, 6, -6, 3, -3, 0);
    }

    @NonNull
    public AnimatorBuilder tada() {
        scaleX(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        scaleY(1, 0.9f, 0.9f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1.1f, 1);
        rotation(0, -3, -3, 3, -3, 3, -3, 3, -3, 0);
        return this;
    }

    @NonNull
    public AnimatorBuilder wave() {
        mAnimator.post(() -> {
            for (View view : mViews) {
                float x = view.getMeasuredWidth() / 2f;
                float y = view.getMeasuredHeight();
                ofFloat(view, AnimatorType.ROTATION, 12, -12, 3, -3, 0);
                ofFloat(view, AnimatorType.PIVOT_X, x, x, x, x, x);
                ofFloat(view, AnimatorType.PIVOT_Y, y, y, y, y, y);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder wobble() {
        mAnimator.post(() -> {
            for (View view : mViews) {
                float width = view.getMeasuredWidth();
                float one = width / 100.0f;
                ofFloat(view, AnimatorType.TRANSLATION_X, 0 * one, -25 * one, 20 * one,
                        -15 * one, 10 * one, -5 * one, 0 * one, 0);
                ofFloat(view, AnimatorType.ROTATION, 0, -5, 3, -3, 2, -1, 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder zoomIn() {
        return scaleX(0.45f, 1).scaleY(0.45f, 1).alpha(0, 1);
    }

    @NonNull
    public AnimatorBuilder zoomOut() {
        return scaleX(1, 0.3f, 0).scaleY(1, 0.3f, 0).alpha(1, 0, 0);
    }

    @NonNull
    public AnimatorBuilder fall() {
        return scale(2f, 1.5f, 1f).alpha(0, 1);
    }

    @NonNull
    public AnimatorBuilder fallRotate() {
        return scale(2f, 1.5f, 1f).alpha(0, 1).rotation(45, 0);
    }

    @NonNull
    public AnimatorBuilder flipX() {
        return rotationX(-90, 0);
    }

    @NonNull
    public AnimatorBuilder flipX2() {
        return rotationX(90, 0);
    }

    @NonNull
    public AnimatorBuilder flipY() {
        return rotationY(-90, 0);
    }

    @NonNull
    public AnimatorBuilder flipY2() {
        return rotationY(90, 0);
    }

    @NonNull
    public AnimatorBuilder newsPaper() {
        return scaleX(0.1f, 0.5f, 1).scaleY(0.1f, 0.5f, 1).alpha(0, 1);
    }

    @NonNull
    public AnimatorBuilder slitX() {
        alpha(0, 0.4f, 0.8f, 1);
        scale(0, 0.5f, 0.9f, 0.9f, 1);
        return rotationX(90, 88, 88, 45, 0);
    }

    @NonNull
    public AnimatorBuilder slitY() {
        alpha(0, 0.4f, 0.8f, 1);
        scale(0, 0.5f, 0.9f, 0.9f, 1);
        return rotationY(90, 88, 88, 45, 0);
    }

    @NonNull
    public AnimatorBuilder jelly() {
        return scale(0.3f, 0.5f, 0.9f, 0.8f, 0.9f, 1).alpha(0.2f, 1);
    }

    @NonNull
    public AnimatorBuilder slideLeftIn() {
        mAnimator.post(() -> {
            alpha(0, 1);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, -view.getRight(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideLeftOut() {
        mAnimator.post(() -> {
            alpha(1, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, 0, -view.getRight());
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideRightIn() {
        mAnimator.post(() -> {
            alpha(0, 1);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, view.getRight(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideRightOut() {
        mAnimator.post(() -> {
            alpha(1, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_X, 0, view.getRight());
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideTopIn() {
        mAnimator.post(() -> {
            alpha(0, 1);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_Y, -view.getBottom(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideTopOut() {
        mAnimator.post(() -> {
            alpha(1, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_Y, 0, -view.getBottom());
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideBottomIn() {
        mAnimator.post(() -> {
            alpha(0, 1);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_Y, view.getBottom(), 0);
            }
        });
        return this;
    }

    @NonNull
    public AnimatorBuilder slideBottomOut() {
        mAnimator.post(() -> {
            alpha(1, 0);
            for (View view : mViews) {
                ofFloat(view, AnimatorType.TRANSLATION_Y, 0, view.getBottom());
            }
        });
        return this;
    }

    /**
     * 自定义动画
     * @param run 动画实现
     */
    public AnimatorBuilder customize(Runnable run) {
        mAnimator.post(run);
        return this;
    }

    /**
     * 生成动画过程中需要操作的属性和对应的值
     * @param view 对应控件, View为null则对当前Builder所有View生效
     * @param type 动画类型
     * @param values 对应的值
     */
    public AnimatorBuilder ofFloat(View view, AnimatorType type, float... values) {
        if (view == null) {
            for (View v : mViews) {
                SparseArray<PropertyValuesHolder> array = mHolderMap.get(v);
                assert array != null;
                array.put(type.ordinal(), PropertyValuesHolder.ofFloat(type.field, values));
            }
        } else {
            SparseArray<PropertyValuesHolder> array = mHolderMap.get(view);
            assert array != null;
            array.put(type.ordinal(), PropertyValuesHolder.ofFloat(type.field, values));
        }
        return this;
    }

    public List<View> getViews() {
        return mViews;
    }

    public int getDuration() {
        return mDuration;
    }

    public int getDelay() {
        return mDelay;
    }

    protected boolean isPlayThen() {
        return mPlayThen;
    }

    /**
     * 生成属性动画集合
     * @param delay 动画延时
     */
    @NonNull
    List<ObjectAnimator> generateAnimators(int delay) {
        List<ObjectAnimator> animators = Collections.synchronizedList(new ArrayList<>());
        for (View view : mViews) {
            SparseArray<PropertyValuesHolder> array = mHolderMap.get(view);
            assert array != null;
            if (array.size() > 0) {
                PropertyValuesHolder[] holders = new PropertyValuesHolder[array.size()];
                for (int j = 0; j < array.size(); j++) {
                    holders[j] = array.valueAt(j);
                }
                // 生成属性动画
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, holders);
                animator.setDuration(mDuration);
                animator.setStartDelay(mDelay + delay);
                animator.setRepeatCount(mRepeatCount);
                animator.setRepeatMode(mRepeatMode.mCode);
                if (mInterpolator != null) {
                    animator.setInterpolator(mInterpolator);
                }
                animators.add(animator);
            }
        }
        return animators;
    }

    @NonNull
    float[] dp2px(@NonNull float... values) {
        float[] result = new float[values.length];
        float density = Resources.getSystem().getDisplayMetrics().density;
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] * density;
        }
        return result;
    }
}
