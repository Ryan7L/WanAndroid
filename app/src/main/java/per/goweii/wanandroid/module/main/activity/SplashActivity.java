package per.goweii.wanandroid.module.main.activity;

import per.goweii.basic.core.base.BaseActivity;
import per.goweii.wanandroid.R;

/**
 * @author CuiZhen
 * @date 2019/5/7
 * GitHub: https://github.com/goweii
 */
public class SplashActivity extends BaseActivity {


    @Override
    protected void initViews() {
        MainActivity.start(getViewContext());
        finish();
        overridePendingTransition(R.anim.zoom_small_in, R.anim.zoom_small_out);
    }

    @Override
    protected void bindData() {
    }
}
