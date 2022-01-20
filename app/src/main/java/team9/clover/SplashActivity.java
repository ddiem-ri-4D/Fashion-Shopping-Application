package team9.clover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;

import team9.clover.Model.DatabaseModel;
import team9.clover.Module.Reuse;

public class SplashActivity extends AppCompatActivity {

    final int COPYRIGHT_DELAY = 800,
            DELAY = 3000;

    ImageView mLogoFull;
    MaterialTextView mCopyright;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DatabaseModel.loadHomePageData(); // trong thời gian 3s, tranh thủ load hình ảnh, dữ liệu ở home fragment
        refer(); // tham chiếu đến các component ở view
        setAnimation(); // thiếp lập animation

        /*
         * Chờ animation hoàn tất, trong lúc chờ thực hiện một vài công việc trước
         * */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkKeepInLogIn();
                finish();
                startActivity(intent); // tùy vào phương thức checkKeepInLogIn mà đi đến activity tương ứng
                Reuse.startActivity(SplashActivity.this); // thiết lập animation
            }
        }, DELAY);
    }

    /*
     * Tham chiếu đến mọi component trên activity
     * */
    private void refer() {
        mCopyright = findViewById(R.id.mtvCopyright);
        mLogoFull = findViewById(R.id.ivLogoFull);
    }

    /*
     * Animation cho logo + tên nhóm
     * */
    private void setAnimation() {
        final Animation splashScreen = AnimationUtils.loadAnimation(this, R.anim.splash_screen);
        splashScreen.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCopyright.setText(String.format(getString(R.string.copyright), Calendar.getInstance().get(Calendar.YEAR)));
                mCopyright.animate().alpha(1f).setDuration((COPYRIGHT_DELAY));
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        mCopyright.animate().alpha(0f).setDuration(0);
        mLogoFull.startAnimation(splashScreen);
    }

    /*
     * Kiểm tra user vẫn đang duy trì đăng nhập hay đã sign-out
     * */
    private void checkKeepInLogIn() {
//        FirebaseAuth.getInstance().signOut();
        DatabaseModel.getCurrentUser();
        if (DatabaseModel.firebaseUser == null) { // nếu là new user, sign-out user thì đi đến login activity
            intent = new Intent(SplashActivity.this, LogInActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
    }
}