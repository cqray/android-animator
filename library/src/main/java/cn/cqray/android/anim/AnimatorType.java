package cn.cqray.android.anim;

/**
 * 属性动画类型
 * <P>不要修改枚举项名，因为属性动画直接使用的枚举项名</P>
 * <P>修改之后，属性动画将失效</P>
 * @author Cqray
 */
enum AnimatorType {
    /** 透明度 **/ alpha(0),
    /** 绕X轴旋转 **/ pivotX(1),
    /** 绕X轴旋转 **/ pivotY(2),
    /** 绕Z轴旋转 **/ rotation(3),
    /** 绕Z轴旋转 **/ rotationX(4),
    /** 绕Z轴旋转 **/ rotationY(5),
    /** 在X轴缩放 **/ scaleX(6),
    /** 在Y轴缩放 **/ scaleY(7),
    /** 在X轴平移 **/ translationX(8),
    /** 在Y轴平移 **/ translationY(9);

    /** 类型 **/
    public final int type;

    AnimatorType(int type) {
        this.type = type;
    }
}
