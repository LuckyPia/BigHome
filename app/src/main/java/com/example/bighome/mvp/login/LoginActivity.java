package com.example.bighome.mvp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.data.source.LoginDataRepository;
import com.example.bighome.data.source.local.LoginDataLocalSource;
import com.example.bighome.data.source.remote.LoginDataRemoteSource;
import com.example.bighome.util.StatuUtil;

public class LoginActivity extends AppCompatActivity {
    private LoginFragment loginFragment;
    private SignUpFragment signUpFragment;

    public Intent intent;

    private int fragmentFlag=0;

    private long startTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MyEditTextStyle);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        StatuUtil.setDarkStatusIcon(true,getWindow());


        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            loginFragment = (LoginFragment) manager.getFragment(savedInstanceState, LoginFragment.class.getSimpleName());
            signUpFragment = (SignUpFragment) manager.getFragment(savedInstanceState, LoginFragment.class.getSimpleName());
        }else {
            loginFragment = LoginFragment.newInstance();
            signUpFragment = SignUpFragment.newInstance();
        }

        //1
        if (!loginFragment.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, loginFragment, LoginFragment.class.getSimpleName())
                    .commit();
        }
        if (!signUpFragment.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.view_pager, signUpFragment, SignUpFragment.class.getSimpleName())
                    .commit();
        }

        new LoginPresenter(loginFragment, LoginDataRepository.getInstance(
                LoginDataLocalSource.getInstance(),
                LoginDataRemoteSource.getInstance()
        ));

        new LoginPresenter(signUpFragment, LoginDataRepository.getInstance(
                LoginDataLocalSource.getInstance(),
                LoginDataRemoteSource.getInstance()
        ));
        showLoginFragment();

    }

    //2.
    public void showLoginFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in,R.anim.right_out)
                .show(loginFragment)
                .hide(signUpFragment)
                .commit();
        fragmentFlag=0;
    }

    //3.
    public void showSignUpFragment(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_in,R.anim.left_out)
                .show(signUpFragment)
                .hide(loginFragment)
                .commit();
        fragmentFlag=1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getSupportFragmentManager();
        if (loginFragment.isAdded()) {
            manager.putFragment(outState, LoginFragment.class.getSimpleName(), loginFragment);
        }
        if (signUpFragment.isAdded()) {
            manager.putFragment(outState, SignUpFragment.class.getSimpleName(), signUpFragment);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(fragmentFlag==1){
            showLoginFragment();
        }else if ((currentTime - startTime) >= 2000) {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            finish();
        }
    }

}
