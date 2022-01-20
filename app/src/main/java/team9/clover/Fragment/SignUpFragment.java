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
import team9.clover.Model.UserModel;
import team9.clover.Module.Reuse;
import team9.clover.R;

public class SignUpFragment extends Fragment {

    FrameLayout mContainer;
    MaterialTextView mSignInFragment;
    TextInputLayout mEmail, mFullName, mPassword;
    ProgressBar mCircle;
    MaterialButton mSignUp;

    String email = "", fullName = "", password = "";

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogInActivity.currentFragment = SignUpFragment.class.getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        refer(view);
        setEvents();

        return view;
    }

    private void refer(View view) {
        mContainer = view.findViewById(R.id.flContainer);
        mSignInFragment = view.findViewById(R.id.mtvSignIn);
        mEmail = view.findViewById(R.id.tilEmail);
        mFullName = view.findViewById(R.id.tilFullName);
        mPassword = view.findViewById(R.id.tilPassword);
        mCircle = view.findViewById(R.id.pbCircle);
        mSignUp = view.findViewById(R.id.mbResetPassword);
    }

    private void setEvents() {
        /*
         * Xử lí sự kiện user nhấn nút quay về trang fragment đăng nhập
         * */
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

        /*
         * Xử lí sự kiện user nhập tên mình vào để đăng kí
         * */
        mFullName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                fullName = Reuse.fullNameValid(mFullName, getActivity());
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
         * Xử lí sự kiện user nhấn nut đăng kí người dùng mới
         * */
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Reuse.emailValid(mEmail, getActivity());
                fullName = Reuse.fullNameValid(mFullName, getActivity());
                password = Reuse.passwordValid(mPassword, getActivity());
                if (email.isEmpty() || fullName.isEmpty() || password.isEmpty()) return;
                addNewUser();
            }
        });
    }

    /*
     * Đăng kí new user và thêm thông tin user vào FireStore
     * */
    private void addNewUser() {
        mSignUp.setClickable(false);
        mCircle.setVisibility(View.VISIBLE);

        UserModel newUser = new UserModel(fullName, email);
        DatabaseModel.signUp(email, password).addOnCompleteListener(new OnCompleteListener() { // đắng kí tài khoản
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    DatabaseModel.addNewUser(newUser).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                getActivity().finish();
                                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
                                Reuse.startActivity(getActivity());

                            } else {
                                errorDialog();
                            }
                        }
                    });
                } else {
                    errorDialog();
                }
            }
        });
    }

    /*
     * Hiển thị dialog cảnh bảo cho user là đăng kí không thành công
     * */
    private void errorDialog() {
        mSignUp.setClickable(true);
        mCircle.setVisibility(View.INVISIBLE);

        new MaterialAlertDialogBuilder(getContext(), R.style.ThemeOverlay_App_MaterialAlertDialog).setTitle("Đăng ký thất bại")
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