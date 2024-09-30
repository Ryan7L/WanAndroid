package per.goweii.wanandroid.module.login.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.InputMethodUtils;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.FragmentLoginBinding;
import per.goweii.wanandroid.event.LoginEvent;
import per.goweii.wanandroid.module.login.activity.AuthActivity;
import per.goweii.wanandroid.module.login.model.UserEntity;
import per.goweii.wanandroid.module.login.presenter.LoginPresenter;
import per.goweii.wanandroid.module.login.view.LoginView;
import per.goweii.wanandroid.widget.InputView;
import per.goweii.wanandroid.widget.PasswordInputView;
import per.goweii.wanandroid.widget.SubmitView;

/**
 * @author CuiZhen
 * @date 2019/5/16
 * GitHub: https://github.com/goweii
 */
public class LoginFragment extends BaseFragment<LoginPresenter,LoginView> implements LoginView {

    //@BindView(R.id.ll_go_register)
    LinearLayout ll_go_register;
    //@BindView(R.id.piv_login_account)
    InputView piv_account;
    //@BindView(R.id.piv_login_password)
    PasswordInputView piv_password;
    //@BindView(R.id.sv_login)
    SubmitView sv_login;

    private AuthActivity mActivity;

    public static LoginFragment create() {
        return new LoginFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLoginBinding binding = FragmentLoginBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        ll_go_register = binding.llGoRegister;
        piv_account = binding.pivLoginAccount;
        piv_password = binding.pivLoginPassword;
        sv_login = binding.svLogin;
        ll_go_register.setOnClickListener(this);
        sv_login.setOnClickListener(this);
        return mRootView;
    }

    @Override
    protected void setUpPresenter() {
        presenter =  new LoginPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AuthActivity) context;
    }

    @Override
    protected void initViews() {
        piv_password.setOnPwdFocusChangedListener(new PasswordInputView.OnPwdFocusChangedListener() {
            @Override
            public void onFocusChanged(boolean focus) {
                mActivity.doEyeAnim(focus);
            }
        });
    }

    @Override
    protected void bindData() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.getSoftInputHelper().moveWith(sv_login,
                piv_account.getEditText(), piv_password.getEditText());
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
    }

    @Override
    public void onInvisible() {
        super.onInvisible();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //    @OnClick({R.id.ll_go_register, R.id.sv_login})
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected boolean onClickContinuously(View v) {
        switch (v.getId()) {
            default:
                return false;
            case R.id.ll_go_register:
                mActivity.changeToRegister();
                break;
            case R.id.sv_login:
                InputMethodUtils.hide(sv_login);
                String userName = piv_account.getText();
                String password = piv_password.getText();
                presenter.login(userName, password, false);
                break;
        }
        return true;
    }

    public void loginByBiometric(String username, String password) {
        presenter.login(username, password, true);
    }

    @Override
    public void loginSuccess(int code, UserEntity data, String username, String password, boolean isBiometric) {
        new LoginEvent(true).post();
        if (isBiometric) {
            finish();
        } else {
            mActivity.tryOpenLoginByBiometric(username, password);
        }
    }

    @Override
    public void loginFailed(int code, String msg) {
        ToastMaker.showShort(msg);
    }
}
