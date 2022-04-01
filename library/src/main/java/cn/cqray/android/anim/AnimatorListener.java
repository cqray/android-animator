package cn.cqray.android.anim;

import android.animation.Animator;

/**
 * 动画监听
 * @author Cqray
 */
public interface AnimatorListener extends Animator.AnimatorListener {

    @Override
    default void onAnimationStart(Animator animator) {}

    @Override
    default void onAnimationEnd(Animator animator) {}

    @Override
    default void onAnimationCancel(Animator animator) {}

    @Override
    default void onAnimationRepeat(Animator animator) {}
}
