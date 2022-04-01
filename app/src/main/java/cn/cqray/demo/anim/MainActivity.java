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

               // new Thread(() -> {

                    ViewAnimator animator = ViewAnimator.playOn(MainActivity.this, view)
                            //.slideBottomIn()
                            .slideTopIn()
                            .duration(1000)
                            .convert();
                            //.playThen(view)
                            //.slideTopIn()
                            animator.start();

                            animator.getDuration(() -> {

                            });
//                            new Thread(()-> {
//                                try {
//                                    Thread.sleep(100);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                                animator.cancel();
//                            });
//                animator.cancel();
               // }).start();
            }
        });
    }
}