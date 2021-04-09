package cn.cqray.demo.anim;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import cn.cqray.android.anim.ViewAnimator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View view = findViewById(R.id.tv);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewAnimator.playOn(MainActivity.this, view)
                        .slideBottomIn()
                        .duration(1000)
                        .playThen(view)
                        .slideTopIn()
                        .start();
            }
        });
    }
}