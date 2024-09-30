//package per.goweii.basic.core.mvp;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//
//import per.goweii.basic.utils.ClickHelper;
//
//
///**
// * @author Cuizhen
// * @version v1.0.0
// * @date 2018/4/4-下午1:23
// */
//public abstract class MvpActivity<P extends IPresenter<V>, V extends IView> extends CacheActivity implements IView, View.OnClickListener {
//
//    public P presenter;
//
//    /**
//     * 初始化presenter
//     */
//    protected void setUpPresenter() {
//
//    }
//
//    /**
//     * 初始化控件
//     */
//    protected abstract void initViews();
//
//    /**
//     * 绑定数据
//     */
//    protected abstract void bindData();
//
//    /**
//     * 点击事件，可连续点击
//     */
//    protected boolean onClickContinuously(final View v) {
//        return false;
//    }
//
//    /**
//     * 点击事件，500毫秒第一次
//     */
//    protected void onClickSpace(final View v) {
//    }
//
//    protected void initWindowConfig() {
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initWindowConfig();
//        initContentView();
//        setUpPresenter();
//        if (presenter != null) {
//            presenter.attach((V) this);
//        }
//        initialize();
//    }
//
//    public void initContentView() {
//
//    }
//
//    protected void initialize() {
//        initViews();
//        bindData();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (presenter != null) {
//            presenter.detach();
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public Context getViewContext() {
//        return getActivity();
//    }
//
//    protected Activity getActivity() {
//        return this;
//    }
//
//    /**
//     * 用注解绑定点击事件时，在该方法绑定
//     */
//    @Override
//    public void onClick(final View v) {
//        if (!onClickContinuously(v)) {
//            ClickHelper.onlyFirstSameView(v, this::onClickSpace);
//        }
//    }
//}
