package per.goweii.wanandroid.module.login.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import per.goweii.wanandroid.databinding.FragmentRegisterBinding;
import per.goweii.wanandroid.event.LoginEvent;
import per.goweii.wanandroid.module.login.activity.AuthActivity;
import per.goweii.wanandroid.module.login.model.UserEntity;
import per.goweii.wanandroid.module.login.presenter.RegisterPresenter;
import per.goweii.wanandroid.module.login.view.RegisterView;
import per.goweii.wanandroid.widget.InputView;
import per.goweii.wanandroid.widget.PasswordInputView;
import per.goweii.wanandroid.widget.SubmitView;

/**
 * @author CuiZhen
 * @date 2019/5/16
 * GitHub: https://github.com/goweii
 */
public class RegisterFragment extends BaseFragment<RegisterPresenter> implements RegisterView {

    //@BindView(R.id.ll_go_login)
    LinearLayout ll_go_login;
    //@BindView(R.id.piv_register_account)
    InputView piv_account;
    //@BindView(R.id.piv_register_password)
    PasswordInputView piv_password;
    //@BindView(R.id.piv_register_password_again)
    PasswordInputView piv_password_again;
    //@BindView(R.id.sv_register)
    SubmitView sv_register;

    private AuthActivity mActivity;

    public static RegisterFragment create() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRegisterBinding binding = FragmentRegisterBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        mViewCreated = true;
        ll_go_login = binding.llGoLogin;
        piv_account = binding.pivRegisterAccount;
        piv_password = binding.pivRegisterPassword;
        piv_password_again = binding.pivRegisterPasswordAgain;
        sv_register = binding.svRegister;
        ll_go_login.setOnClickListener(this);
        sv_register.setOnClickListener(this);
        return mRootView;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_register;
    }

    @Nullable
    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AuthActivity) context;
    }

    @Override
    protected void initView() {
        piv_password.setOnPwdFocusChangedListener(new PasswordInputView.OnPwdFocusChangedListener() {
            @Override
            public void onFocusChanged(boolean focus) {
                mActivity.doEyeAnim(focus);
            }
        });
        piv_password_again.setOnPwdFocusChangedListener(new PasswordInputView.OnPwdFocusChangedListener() {
            @Override
            public void onFocusChanged(boolean focus) {
                mActivity.doEyeAnim(focus);
            }
        });
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.getSoftInputHelper().moveWith(
                sv_register,
                piv_account.getEditText(),
                piv_password.getEditText(),
                piv_password_again.getEditText());
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected boolean onClick1(View v) {
        switch (v.getId()) {
            default:
                return false;
            case R.id.ll_go_login:
                mActivity.changeToLogin();
                break;
            case R.id.sv_register:
                InputMethodUtils.hide(sv_register);
                register();
                break;
        }
        return true;
    }

    private void register() {
        String password = piv_password.getText();
        String repassword = piv_password_again.getText();
        if (!TextUtils.equals(password, repassword)) {
            ToastMaker.showShort("请确认2次密码一致");
            return;
        }
        String username = piv_account.getText();
        presenter.register(username, password, repassword);
    }

    @Override
    public void registerSuccess(int code, UserEntity data, String username, String password) {
        new LoginEvent(true).post();
        mActivity.tryOpenLoginByBiometric(username, password);
        //finish();
    }

    @Override
    public void registerFailed(int code, String msg) {
        ToastMaker.showShort(msg);
    }
}
