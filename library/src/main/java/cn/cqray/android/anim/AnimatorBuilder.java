package cn.cqray.android.anim;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Interpolator;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动画构建器
 * @author Cqray
 * @date 2021/4/7 11:31
 */
public final class AnimatorBuilder {
    /** 动画对象 **/
    private ViewAnimator mAnimator;
    /** 动画开始前延时时间 **/
    private int mDelay = 0;
    /** 动画时间 **/
    private int mDuration = 500;
    /** 重复次数 **/
    private int mRepeatCount = 0;
    /** 重复模式 **/
    private RepeatMode mRepeatMode = RepeatMode.RESTART;
    /** 插值器 **/
    private Interpolator mInterpolator;
    /** 是否是随后动画 **/
    private boolean mPlayThen;
    /** 控件列表 **/
    private List<View> mViews;

    private Map<View, SparseArray<PropertyValuesHolder>> mHolderMap;

    AnimatorBuilder(ViewAnimator animator, boolean playThen, @NonNull View... views) {
        mAnimator = animator;
        mPlayThen = playThen;
        mViews = new ArrayList<>();
        mHolderMap = new HashMap<>();
        for (View view : views) {
            if (view != null) {
                mViews.add(view);
                mHolderMap.put(view, new SparseArray<PropertyValuesHolder>());
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

    /**
     * 动画时长
     * @param duration 默认500ms
     */
    public AnimatorBuilder duration(int duration) {
        mDuration = duration;
        return this;
    }

    /**
     * 动画播放前延时
     * @param delay 默认0ms
     */
    public AnimatorBuilder delay(int delay) {
        mDelay = delay;
        return this;
    }

    public AnimatorBuilder repeatCount(int value) {
        mRepeatCount = value;
        return this;
    }

    public AnimatorBuilder repeatMode(RepeatMode mode) {
        mRepeatMode = mode;
        return this;
    }

    public AnimatorBuilder interpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    @NonNull
    public AnimatorBuilder alpha(@NonNull float... values) {
        return ofFloat(null, AnimatorType.alpha, values);
    }

    @NonNull
    public AnimatorBuilder pivot(@NonNull float... values) {
        return pivotX(values).pivotY(values);
    }

    @NonNull
    public AnimatorBuilder pivotX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.pivotX, values);
    }

    @NonNull
    public AnimatorBuilder pivotY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.pivotY, values);
    }

    @NonNull
    public AnimatorBuilder rotation(@NonNull float... values) {
        return ofFloat(null, AnimatorType.rotation, values);
    }

    @NonNull
    public AnimatorBuilder rotationX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.rotationX, values);
    }

    @NonNull
    public AnimatorBuilder rotationY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.rotationY, values);
    }

    @NonNull
    public AnimatorBuilder scale(float... values) {
        return scaleX(values).scaleY(values);
    }

    @NonNull
    public AnimatorBuilder scaleX(@NonNull float... values) {
        return ofFloat(null, AnimatorType.scaleX, values);
    }

    @NonNull
    public AnimatorBuilder scaleY(@NonNull float... values) {
        return ofFloat(null, AnimatorType.scaleY, values);
    }

    @NonNull
    public AnimatorBuilder translationX(@NonNull float... values) {
        return translationX(true, values);
    }

    @NonNull
    public AnimatorBuilder translationX(boolean dp, @NonNull float... values) {
        float[] temp = values;
        if (dp) {
            temp = dp2px(values);
        }
        return ofFloat(null, AnimatorType.translationX, temp);
    }

    @NonNull
    public AnimatorBuilder translationY(@NonNull float... values) {
        return translationY(true, values);
    }

    @NonNull
    public AnimatorBuilder translationY(boolean dp, @NonNull float... values) {
        float[] temp = values;
        if (dp) {
            temp = dp2px(values);
        }
        return ofFloat(null, AnimatorType.translationY, temp);
    }

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

    public AnimatorBuilder rollLeftIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1).rotation(-120, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, -view.getWidth(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder rollRightIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1).rotation(120, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, view.getWidth(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder rollLeftOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0).rotation(0, -120);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, 0, -view.getWidth());
                }
            }
        });
        return this;
    }

    public AnimatorBuilder rollRightOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0).rotation(0, 120);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, 0, view.getWidth());
                }
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

    public AnimatorBuilder standUp() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                for (View view : mViews) {
                    float x = view.getWidth() / 2f;
                    float y = view.getHeight();
                    ofFloat(view, AnimatorType.rotationX, 55, -30, 15, -15, 0);
                    ofFloat(view, AnimatorType.pivotX, x, x, x, x, x);
                    ofFloat(view, AnimatorType.pivotY, y, y, y, y, y);
                }
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

    public AnimatorBuilder wave() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                for (View view : mViews) {
                    float x = view.getWidth() / 2f;
                    float y = view.getHeight();
                    ofFloat(view, AnimatorType.rotation, 12, -12, 3, -3, 0);
                    ofFloat(view, AnimatorType.pivotX, x, x, x, x, x);
                    ofFloat(view, AnimatorType.pivotY, y, y, y, y, y);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder wobble() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                for (View view : mViews) {
                    float width = view.getWidth();
                    float one = width / 100.0f;
                    ofFloat(view, AnimatorType.translationX, 0 * one, -25 * one, 20 * one,
                            -15 * one, 10 * one, -5 * one, 0 * one, 0);
                    ofFloat(view, AnimatorType.rotation, 0, -5, 3, -3, 2, -1, 0);
                }
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

    public AnimatorBuilder slideLeftIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, -view.getRight(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideLeftOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, 0, -view.getRight());
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideRightIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, view.getRight(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideRightOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationX, 0, view.getRight());
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideTopIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationY, -view.getBottom(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideTopOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationY, 0, -view.getBottom());
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideBottomIn() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(0, 1);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationY, view.getBottom(), 0);
                }
            }
        });
        return this;
    }

    public AnimatorBuilder slideBottomOut() {
        mAnimator.post(new Runnable() {
            @Override
            public void run() {
                alpha(1, 0);
                for (View view : mViews) {
                    ofFloat(view, AnimatorType.translationY, 0, view.getBottom());
                }
            }
        });
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

    @NonNull
    protected List<ObjectAnimator> getAnimators(int delay) {
        List<ObjectAnimator> animators = new ArrayList<>();
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
                animator.setRepeatMode(mRepeatMode == RepeatMode.RESTART ?
                        ValueAnimator.RESTART : ValueAnimator.REVERSE);
                if (mInterpolator != null) {
                    animator.setInterpolator(mInterpolator);
                }
                animators.add(animator);
            }
        }
        return animators;
    }

    private AnimatorBuilder ofFloat(View view, AnimatorType type, float... values) {
        if (view == null) {
            for (View v : mViews) {
                SparseArray<PropertyValuesHolder> array = mHolderMap.get(v);
                assert array != null;
                array.put(type.type, PropertyValuesHolder.ofFloat(type.name(), values));
            }
        } else {
            SparseArray<PropertyValuesHolder> array = mHolderMap.get(view);
            assert array != null;
            array.put(type.type, PropertyValuesHolder.ofFloat(type.name(), values));
        }
        return this;
    }

    @NonNull
    private float[] dp2px(@NonNull float... values) {
        float[] result = new float[values.length];
        float density = Resources.getSystem().getDisplayMetrics().density;
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i] * density;
        }
        return result;
    }
}
