package com.example.bighome.mvp.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;
import com.example.bighome.mvp.main.MainActivity;
import com.example.bighome.util.SettingsUtil;
import com.example.bighome.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpFragment extends Fragment implements LoginContract.View,View.OnClickListener,View.OnTouchListener {

    private LoginActivity loginActivity;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.edit_username)
    TextInputEditText editUserName;
    @BindView(R.id.edit_password)
    TextInputEditText editPassword;
    @BindView(R.id.edit_re_password)
    TextInputEditText editRePassword;
    @BindView(R.id.btn_sign_up)
    AppCompatButton btnSignUp;
    @BindView(R.id.text_link_login)
    TextView linkLogin;
    private LoginContract.Presenter presenter;

    protected float mFirstX;//触摸下去的位置
    protected float mCurrentX;//滑动时Y的位置
    //protected int direction;//判断是否上滑或者下滑的标志
    protected int mTouchShop;//最小滑动距离


    public SignUpFragment() {

    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this,view);

        linearLayout.setOnClickListener(this);
        linearLayout.setOnTouchListener(this);
        linkLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    private boolean checkValid(String username, String password, String rePassword){
        boolean isValid = false;
        if (StringUtil.isInvalid(username)|| StringUtil.isInvalid(password)|| StringUtil.isInvalid(rePassword)){
            Snackbar.make(linkLogin,getString(R.string.input_error),Snackbar.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)){
            Snackbar.make(linkLogin,getString(R.string.repassword_not_match),Snackbar.LENGTH_SHORT).show();
        }else {
            isValid = true;
        }
        return isValid;

    }

    @Override
    public void showLoginError(String errorMsg) {
        Snackbar.make(linkLogin,errorMsg,Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    @Override
    public void saveUserPreference(LoginDetailData loginDetailData) {
        int userId = loginDetailData.getId();
        String username = loginDetailData.getUsername();
        String password = loginDetailData.getPassword();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        presenter.clearReadLaterData();
        sp.edit().putInt(SettingsUtil.USERID, userId).apply();
        sp.edit().putString(SettingsUtil.USERNAME, username).apply();
        sp.edit().putString(SettingsUtil.PASSWORD, password).apply();
        sp.edit().putBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE,true).apply();
        navigateToMain();
    }

    private void navigateToMain() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(getContext(),getString(R.string.network_error),Toast.LENGTH_LONG).show();
    }

    @Override
    public void initViews(View view) {
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private  void hideSoftInput(Context ctx, View v){
        //隐藏输入法
        InputMethodManager inputMethodManager = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_layout:
                hideSoftInput(loginActivity,v);
                break;
            case R.id.text_link_login:
                loginActivity.showLoginFragment();
                break;
            case R.id.btn_sign_up:
                hideSoftInput(loginActivity,v);

                String username = editUserName.getText().toString();
                String password = editPassword.getText().toString();
                String rePassword = editRePassword.getText().toString();
                if (checkValid(username,password,rePassword)){
                    //register
                    presenter.login(username,password, LoginType.TYPE_REGISTER);
                }
                break;
                default:break;
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mFirstX = event.getX();//按下时获取位置
                break;

            case MotionEvent.ACTION_MOVE:

                mCurrentX = event.getX();//得到滑动的位置
                if(mCurrentX - mFirstX > mTouchShop){
                    //滑动的位置减去按下的位置大于最小滑动距离  则表示向右滑动
                    loginActivity.showLoginFragment();
                }else if(mFirstX - mCurrentX > mTouchShop){
                    //反之向左滑动
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return false;
    }
}
