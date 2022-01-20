package team9.clover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import team9.clover.Fragment.ForgetPasswordFragment;
import team9.clover.Fragment.SignInFragment;
import team9.clover.Fragment.SignUpFragment;
import team9.clover.Model.DatabaseModel;
import team9.clover.Module.Reuse;

public class LogInActivity extends AppCompatActivity {

    ImageButton mClose;
    FrameLayout mContainer;
    FragmentManager mFragmentManager;
    public static String currentFragment = SignInFragment.class.getSimpleName(); // giữ fragment hiện đang thực thi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        if (DatabaseModel.categoryModelList == null || DatabaseModel.categoryModelList.size() == 0) {
            DatabaseModel.loadHomePageData();
        }

        refer();
        setEvent();
        Reuse.setFragment(mFragmentManager, new SignInFragment(), mContainer, 1);
    }

    /*
     * Xử lí sự kiện user nhấn nút back trên màn hình
     * */
    @Override
    public void onBackPressed() {
        if (currentFragment.equals(SignUpFragment.class.getSimpleName())
                || currentFragment.equals(ForgetPasswordFragment.class.getSimpleName())) {
            // nếu user đang ở fragment sign-up hoặc fragment forget password thì cho về fragmen sign-in
            Reuse.setFragment(mFragmentManager, new SignInFragment(), mContainer, -1);
        } else if (currentFragment.equals(SignInFragment.class.getSimpleName())){
            // nếu user đang ở fragment sign-in thì hỏi user có "thực sự muốn thoát ứng dụng không"
            currentFragment = null;
            Toast.makeText(this, getString(R.string.on_back_press), Toast.LENGTH_SHORT).show();
        } else {
            // nếu ở fragment sign-in mà user nhấn nút back 2 lần kề nhau thì thoát ứng dụng
            super.onBackPressed();
        }
    }

    /*
     * Tham chiếu đến các component của activity
     * */
    private void refer() {
        mClose = findViewById(R.id.ibClose);
        mContainer = findViewById(R.id.flContainer);
        mFragmentManager = getSupportFragmentManager();
    }

    private void setEvent() {
        mClose.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(LogInActivity.this, MainActivity.class));
            Reuse.startActivity(LogInActivity.this); // thiết lập animation
        });
    }
}