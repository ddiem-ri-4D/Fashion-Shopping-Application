package team9.clover.Fragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import team9.clover.LogInActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Module.Reuse;
import team9.clover.R;


public class ForgetPasswordFragment extends Fragment {

    FrameLayout mContainer;
    MaterialTextView mSignInFragment, mState;
    TextInputLayout mEmail;
    MaterialButton mResetPassword;
    ProgressBar mCircle;

    String email = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInActivity.currentFragment = ForgetPasswordFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        refer(view);
        setEvents();

        return view;
    }

    /*
     * Tham chiếu đến các các component trên activity
     * */
    private void refer(View view) {
        mContainer = view.findViewById(R.id.flContainer);
        mSignInFragment = view.findViewById(R.id.mtvSignIn);
        mState = view.findViewById(R.id.mtvState);
        mEmail = view.findViewById(R.id.tilEmail);
        mResetPassword = view.findViewById(R.id.mbResetPassword);
        mCircle = view.findViewById(R.id.pbCircle);
    }

    /*
     * Thiết lập các sự kiện
     * */
    private void setEvents() {
        // Xử lí sự kiện user nhấn vào nút quay về fragment đăng kí
        mSignInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignInFragment.setTypeface(null, Typeface.BOLD); // bold text
                Reuse.setFragment(getActivity().getSupportFragmentManager(), new SignInFragment(), mContainer, -1);
            }
        });

        /*
         * Xử lí sự kiện user nhập email để đăng kí
         * */
        mEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = Reuse.emailValid(mEmail, getActivity());
            }
        });

        // Xử lí sự kiện user nhấn vào nút gửi mail khôi phục password
        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Reuse.emailValid(mEmail, getActivity());
                if (email.isEmpty()) return;
                resetPassword();
            }
        });
    }

    /*
     * Dùng khi user nhấn vào button reset password
     * */
    private void resetPassword() {
        mResetPassword.setClickable(false);
        mCircle.setVisibility(View.VISIBLE);
        mState.setText("Đang khôi phục mật khẩu...");
        mState.setVisibility(View.VISIBLE);

        DatabaseModel.resetPassword(email).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    mResetPassword.setClickable(true);
                    mCircle.setVisibility(View.GONE);
                    mState.setText("Mail khôi phục đã được gửi.\nBạn kiểm tra email của bạn nha.");
                    mState.setVisibility(View.VISIBLE);
                } else {
                    errorDialog();
                }
            }
        });
    }

    /*
     * Hiên thị dialog khi gửi mail khôi phục password không thành công
     * */
    private void errorDialog() {
        mResetPassword.setClickable(true);
        mCircle.setVisibility(View.GONE);
        mState.setVisibility(View.GONE);

        new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Khôi phục thất bại")
                .setMessage("Không thể gửi mail khôi phục mật khẩu. Xin lỗi bạn nha.")
                .setCancelable(false)
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
}