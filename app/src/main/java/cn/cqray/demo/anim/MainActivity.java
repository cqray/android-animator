package cn.cqray.demo.anim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.Arrays;

import cn.cqray.android.anim.AnimatorBuilder;
import cn.cqray.android.anim.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    RecyclerView rv;

    String[] list = new String[] {
            "bounce", "bounceIn", "bounceOut",
            "fadeIn", "fadeOut", "flash", "pulse",
            "rollLeftIn", "rollRightIn", "rollLeftOut", "rollRightOut",
            "rubber", "shakeX", "shakeY", "standUp", "swing", "tada", "wave", "wobble",

    };
    String[] list2 = new String[] {
            "zoomIn", "zoomOut",
            "fall", "fallRotate",
            "flipX", "flipX2", "flipY", "flipY2", "newsPaper",
            "slitX", "slitY", "jelly",
            "slideLeftIn", "slideLeftOut", "slideRightIn", "slideRightOut",
            "slideTopIn", "slideTopOut", "slideBottomIn", "slideBottomOut"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        AnimatorAdapter adapter = new AnimatorAdapter();
        adapter.setList(Arrays.asList(list2));
        rv.setAdapter(adapter);


        findViewById(R.id.tv).setOnClickListener(v -> adapter.startAnimator());
    }
}