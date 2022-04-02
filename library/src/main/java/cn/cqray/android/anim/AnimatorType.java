package cn.cqray.android.anim;

/**
 * 属性动画类型
 * @author Cqray
 */
public enum AnimatorType {
    /** 透明度 **/ ALPHA("alpha"),
    /** 缩放中心X坐标 **/ PIVOT_X("pivotX"),
    /** 缩放中心Y坐标 **/ PIVOT_Y("pivotY"),
    /** 绕Z轴旋转 **/ ROTATION("rotation"),
    /** 绕Z轴旋转 **/ ROTATION_X("rotationX"),
    /** 绕Z轴旋转 **/ ROTATION_Y("rotationY"),
    /** 在X轴缩放 **/ SCALE_X("scaleX"),
    /** 在Y轴缩放 **/ SCALE_Y("scaleY"),
    /** 在X轴平移 **/ TRANSLATION_X("translationX"),
    /** 在Y轴平移 **/ TRANSLATION_Y("translationY");

    /** 对应的操作字段 **/
    public final String field;

    AnimatorType(String field) {
        this.field = field;
    }
}
