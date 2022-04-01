package cn.cqray.android.anim;

import android.animation.ValueAnimator;

/**
 * 动画重复模式
 * @author Cqray
 */
public enum RepeatMode {

    /** 重头开始 **/ RESTART(ValueAnimator.RESTART),
    /** 倒置 **/ REVERSE(ValueAnimator.REVERSE);

    /** 值 **/
    protected final int mCode;

    RepeatMode(int code) {
        mCode = code;
    }
}
