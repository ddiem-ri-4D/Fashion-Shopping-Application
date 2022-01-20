package team9.clover.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import team9.clover.MainActivity;
import team9.clover.Model.DatabaseModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class SignInFragment extends Fragment {

    FrameLayout mContainer;
    MaterialTextView mSignUpFragment, mForgetPassword;
    ProgressBar mCircle;
    MaterialButton mSignIn;
    TextInputLayout mEmail, mPassword;

    String email = "", password = "";

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInActivity.currentFragment = SignInFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        refer(view);
        setEvent();

        return view;
    }

    /*
     * Tham chiếu đến các component của activity
     * */
    private void refer(View view) {
        mContainer = view.findViewById(R.id.flContainer);
        mForgetPassword = view.findViewById(R.id.mtvForgetPassword);
        mSignUpFragment = view.findViewById(R.id.mtvSignUp);
        mCircle = view.findViewById(R.id.pbCircle);
        mSignIn = view.findViewById(R.id.mbSignIn);
        mEmail = view.findViewById(R.id.tilEmail);
        mPassword = view.findViewById(R.id.tilPassword);
    }

    private void setEvent() {
        /*
         * Xử lí sự kiện user nhấn nút đăng ký tài khoản
         * */
        mSignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignUpFragment.setTypeface(null, Typeface.BOLD); // bold text
                Reuse.setFragment(getActivity().getSupportFragmentManager(), new SignUpFragment(), mContainer, 1);
            }
        });

        /*
         * Xử lí sự kiện user nhấn nút forget password
         * */
        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mForgetPassword.setTypeface(null, Typeface.BOLD); // bold text
                Reuse.setFragment(getActivity().getSupportFragmentManager(), new ForgetPasswordFragment(), mContainer, 1);
            }
        });

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

        /*
         * Xử lí sự kiện user nhập password để đăng kí
         * */
        mPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password = Reuse.passwordValid(mPassword, getActivity());
            }
        });

        /*
         * Xử lí sự kiện user nhấn nút đăng nhập
         * */
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Reuse.emailValid(mEmail, getActivity()); // kiểm tra email
                password = Reuse.passwordValid(mPassword, getActivity()); // kiểm tra password
                if (email.isEmpty() || password.isEmpty()) return;
                signIn();
            }
        });
    }

    /*
     * Dùng cho sự kiện khi user nhấn nút đăng nhập
     * */
    private void signIn() {
        mSignIn.setClickable(false);
        mCircle.setVisibility(View.VISIBLE);

        DatabaseModel.signIn(email, password).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DatabaseModel.getCurrentUser();
                    getActivity().finishAffinity();
                    getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                    Reuse.startActivity(getActivity());
                } else {
                    errorDialog();
                }
            }
        });
    }

    /*
     * Hiển thị dialog cảnh bảo cho user là đăng nhập không thành công
     * */
    private void errorDialog() {
        mSignIn.setClickable(true);
        mCircle.setVisibility(View.INVISIBLE);

        new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Đăng nhập thất bại")
                .setMessage("Bạn vui lòng kiểm tra lại giúp mình nha.")
                .setCancelable(false)
                .setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
}