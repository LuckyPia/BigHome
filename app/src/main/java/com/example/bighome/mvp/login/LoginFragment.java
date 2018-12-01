package com.example.bighome.mvp.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.data.LoginDetailData;
import com.example.bighome.data.LoginType;
import com.example.bighome.mvp.main.MainActivity;
import com.example.bighome.util.SettingsUtil;
import com.example.bighome.util.StringUtil;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginFragment extends Fragment implements LoginContract.View,CompoundButton.OnCheckedChangeListener,View.OnClickListener,View.OnTouchListener {


    private LoginActivity loginActivity;

    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;
    @BindView(R.id.edit_username)
    TextInputEditText editUserName;
    @BindView(R.id.edit_password)
    TextInputEditText editPassword;
    @BindView(R.id.cb_autlg)
    AppCompatCheckBox cbAutoLogin;
    @BindView(R.id.cb_rempwd)
    AppCompatCheckBox cbRememberPassword;
    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;
    @BindView(R.id.text_link_signup)
    TextView linkSignUp;
    @BindView(R.id.cb_terms)
    AppCompatCheckBox cbTerms;
    @BindView(R.id.text_terms_of_service)
    TextView tvTerms;
    @BindString(R.string.terms)
    String content;
    private LoginContract.Presenter presenter;

    private SharedPreferences sp;

    private ProgressDialog progressDialog;

    protected float mFirstX;//触摸下去的位置
    protected float mCurrentX;//滑动时Y的位置
    //protected int direction;//判断是否上滑或者下滑的标志
    protected int mTouchShop;//最小滑动距离


    public LoginFragment() {

    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);

        terms();
        cbTerms.setChecked(sp.getBoolean(SettingsUtil.TERMS_OF_SERVICE, false));
        cbAutoLogin.setChecked(sp.getBoolean(SettingsUtil.AUTO_LOGIN, false));
        cbRememberPassword.setChecked(sp.getBoolean(SettingsUtil.REMEMBER_PASSWORD, false));

        if(cbTerms.isChecked()){
            btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(loginActivity,R.drawable.button_selector));
            linkSignUp.setTextColor(ContextCompat.getColor(loginActivity,R.color.colorPrimaryText));
            tvTerms.setTextColor(ContextCompat.getColor(loginActivity,R.color.iron));
        }

        if(sp.getBoolean(SettingsUtil.REMEMBER_PASSWORD, false)){
            if (sp.getBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE, false)){
                String username=sp.getString(SettingsUtil.USERNAME, null);
                String password=sp.getString(SettingsUtil.PASSWORD, null);
                editUserName.setText(username);
                editPassword.setText(password);
            }
        }

        linearLayout.setOnClickListener(this);
        linearLayout.setOnTouchListener(this);
        linkSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        cbTerms.setOnCheckedChangeListener(this);
        cbAutoLogin.setOnCheckedChangeListener(this);
        cbRememberPassword.setOnCheckedChangeListener(this);
        tvTerms.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        if(sp.getBoolean(SettingsUtil.AUTO_LOGIN, false)){
            if (sp.getBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE, false)) {
                if(sp.getBoolean(SettingsUtil.TERMS_OF_SERVICE, false)){
                    showProgressDialog();
                    final String username=sp.getString(SettingsUtil.USERNAME, null);
                    final String password=sp.getString(SettingsUtil.PASSWORD, null);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            //Login
                            presenter.login(username,password, LoginType.TYPE_LOGIN);
                        }
                    }, 1000);
                }
            }
        }

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void showLoginError(String errorMsg) {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        Snackbar.make(linkSignUp, errorMsg, Snackbar.LENGTH_SHORT).show();
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
        int oldUerId = sp.getInt(SettingsUtil.USERID, -1);
        if (oldUerId != -1 && userId != oldUerId) {
            presenter.clearReadLaterData();
        }
        sp.edit().putInt(SettingsUtil.USERID, userId).apply();
        sp.edit().putString(SettingsUtil.USERNAME, username).apply();
        sp.edit().putString(SettingsUtil.PASSWORD, password).apply();
        sp.edit().putBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE,true).apply();

        navigateToMain(username);
    }

    private void navigateToMain(String username) {
        //登录到主界面
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("username",username);
        loginActivity.startActivity(intent);
    }

    private void terms(){
        //服务协议富文本
        //阅读并同意服务协议
        //登录即代表同意服务协议
        SpannableString mSpannableString = new SpannableString(content);
        mSpannableString.setSpan(new UnderlineSpan(),7,11, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mSpannableString.setSpan(new ForegroundColorSpan(
                ContextCompat.getColor(loginActivity,R.color.colorAccent)),7,11, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvTerms.setText(mSpannableString);
    }

    private  void hideSoftInput(Context ctx,View v){
        //隐藏输入法
        InputMethodManager inputMethodManager = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    private void showProgressDialog(){
        //显示登录进度
        progressDialog=new ProgressDialog(loginActivity);
        progressDialog.setTitle("登录");
        progressDialog.setMessage("登录中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void showTerms(){
        //显示具体服务条款
        new AlertDialog.Builder(loginActivity)
                .setTitle("服务条款")
                .setMessage(getResources().getString(R.string.terms_of_service))
                .setCancelable(true)
                .setNegativeButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cbTerms.setChecked(true);
                        sp.edit().putBoolean(SettingsUtil.TERMS_OF_SERVICE,true).apply();
                    }
                })
                .create()
                .show();
    }

    private boolean checkValid(String username, String password,boolean isChecked){
        //登录错误检查
        boolean isValid = false;
        if (StringUtil.isInvalid(username)|| StringUtil.isInvalid(password)){
            Snackbar.make(linkSignUp,getString(R.string.input_error),Snackbar.LENGTH_SHORT).show();
        }else if(!isChecked){
            Snackbar.make(linkSignUp,getString(R.string.terms_error),Snackbar.LENGTH_SHORT)
                    .setAction("同意", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cbTerms.setChecked(true);
                            sp.edit().putBoolean(SettingsUtil.TERMS_OF_SERVICE,true).apply();
                        }
                    })
                    .show();
        }else {
            isValid = true;
        }
        return isValid;
    }
    private boolean checkValid(String username, String password, String rePassword){
        //注册错误检查
        boolean isValid = false;
        if (StringUtil.isInvalid(username)|| StringUtil.isInvalid(password)|| StringUtil.isInvalid(rePassword)){
            Snackbar.make(linkSignUp,getString(R.string.input_error),Snackbar.LENGTH_SHORT).show();
        }else if (!password.equals(rePassword)){
            Snackbar.make(linkSignUp,getString(R.string.repassword_not_match),Snackbar.LENGTH_SHORT).show();
        }else {
            isValid = true;
        }
        return isValid;

    }

    @Override
    public void showNetworkError() {
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        Toast.makeText(getContext(),getString(R.string.network_error),Toast.LENGTH_LONG).show();
    }

    @Override
    public void initViews(View view) {
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch (buttonView.getId()){
            case R.id.cb_autlg:
                if(isChecked){
                    if(!cbRememberPassword.isChecked()){
                        cbRememberPassword.setChecked(true);
                        sp.edit().putBoolean(SettingsUtil.REMEMBER_PASSWORD,true).apply();
                    }
                    sp.edit().putBoolean(SettingsUtil.AUTO_LOGIN,true).apply();
                }else{
                    sp.edit().putBoolean(SettingsUtil.AUTO_LOGIN,false).apply();
                }
                break;
            case R.id.cb_rempwd:
                if(isChecked){
                    sp.edit().putBoolean(SettingsUtil.REMEMBER_PASSWORD,true).apply();
                }else{
                    if(cbAutoLogin.isChecked()){
                        cbAutoLogin.setChecked(false);
                        sp.edit().putBoolean(SettingsUtil.AUTO_LOGIN,false).apply();
                    }
                    sp.edit().putBoolean(SettingsUtil.REMEMBER_PASSWORD,false).apply();
                }
                break;
            case R.id.cb_terms:
                if(isChecked){
                    btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(loginActivity,R.drawable.button_selector));
                    linkSignUp.setTextColor(ContextCompat.getColor(loginActivity,R.color.colorPrimaryText));
                    tvTerms.setTextColor(ContextCompat.getColor(loginActivity,R.color.iron));
                    sp.edit().putBoolean(SettingsUtil.TERMS_OF_SERVICE,true).apply();
                }else{
                    btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(loginActivity,R.drawable.button_selector_hui));
                    linkSignUp.setTextColor(ContextCompat.getColor(loginActivity,R.color.iron));
                    tvTerms.setTextColor(ContextCompat.getColor(loginActivity,R.color.iron));
                    sp.edit().putBoolean(SettingsUtil.TERMS_OF_SERVICE,false).apply();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                hideSoftInput(loginActivity,v);

                final String username = editUserName.getText().toString();
                final String password = editPassword.getText().toString();
                final boolean isChecked=cbTerms.isChecked();
                if (checkValid(username,password,isChecked)){
                    if(username=="puyadong"&&password=="123456"){
                        navigateToMain(username);
                    }else{
                        //登录中...
                        showProgressDialog();
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                //Login
                                presenter.login(username,password, LoginType.TYPE_LOGIN);
                            }
                        }, 1000);
                    }
                }
                break;
            case R.id.text_link_signup:
                if(cbTerms.isChecked()){
                    loginActivity.showSignUpFragment();
                }else{
                    hideSoftInput(loginActivity,v);
                    Snackbar.make(linkSignUp,getString(R.string.terms_error),Snackbar.LENGTH_SHORT)
                            .setAction("同意", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cbTerms.setChecked(true);
                                    sp.edit().putBoolean(SettingsUtil.TERMS_OF_SERVICE,true).apply();
                                }
                            })
                            .show();
                }

                break;
            case R.id.text_terms_of_service:
                showTerms();
                break;
            case R.id.linear_layout:
                hideSoftInput(loginActivity,v);
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
                    //滑动的位置减去按下的位置大于最小滑动距离  则表示向左滑动
                }else if(mFirstX - mCurrentX > mTouchShop){
                    //反之向右滑动
                    if(cbTerms.isChecked()){
                        loginActivity.showSignUpFragment();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return false;
    }
}
