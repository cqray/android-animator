package cn.cqray.demo.anim;

import android.view.View;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.lang.reflect.Method;

import cn.cqray.android.anim.AnimatorBuilder;
import cn.cqray.android.anim.ViewAnimator;

public class AnimatorAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    boolean startAnimator;

    public AnimatorAdapter() {
        super(R.layout.item_animator);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, String s) {
        holder.setText(R.id.item_text, s);

        View view = holder.getView(R.id.item_view);

        if (startAnimator) {
            AnimatorBuilder ab = ViewAnimator.playOn(view);
            try {
                Class<?> cls = ab.getClass();
                Method method = cls.getMethod(s);
                method.invoke(ab);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
            ab.duration(3000).start();
        }
    }

    public void startAnimator() {
        startAnimator = true;
        notifyDataSetChanged();
    }
}
