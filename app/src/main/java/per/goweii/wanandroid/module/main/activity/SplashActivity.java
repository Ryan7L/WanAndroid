package per.goweii.wanandroid.module.main.activity;

import androidx.annotation.Nullable;

import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.mvp.IPresenter;
import per.goweii.wanandroid.R;

/**
 * @author CuiZhen
 * @date 2019/5/7
 * GitHub: https://github.com/goweii
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        // return R.layout.activity_splash;
        return 0;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initViews() {
        MainActivity.start(getContext());
        finish();
        overridePendingTransition(R.anim.zoom_small_in, R.anim.zoom_small_out);
    }

    @Override
    protected void bindData() {
    }
}
