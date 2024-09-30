package per.goweii.wanandroid.module.mine.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.core.utils.SmartRefreshUtils;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Config;
import per.goweii.wanandroid.databinding.FragmentMineBinding;
import per.goweii.wanandroid.event.LoginEvent;
import per.goweii.wanandroid.event.MessageCountEvent;
import per.goweii.wanandroid.event.MessageUpdateEvent;
import per.goweii.wanandroid.event.UserInfoUpdateEvent;
import per.goweii.wanandroid.module.login.model.UserEntity;
import per.goweii.wanandroid.module.mine.activity.AboutMeActivity;
import per.goweii.wanandroid.module.mine.activity.CoinActivity;
import per.goweii.wanandroid.module.mine.activity.CollectionActivity;
import per.goweii.wanandroid.module.mine.activity.MessageActivity;
import per.goweii.wanandroid.module.mine.activity.MineShareActivity;
import per.goweii.wanandroid.module.mine.activity.OpenActivity;
import per.goweii.wanandroid.module.mine.activity.ReadLaterActivity;
import per.goweii.wanandroid.module.mine.activity.ReadRecordActivity;
import per.goweii.wanandroid.module.mine.activity.SettingActivity;
import per.goweii.wanandroid.module.mine.activity.UserInfoActivity;
import per.goweii.wanandroid.module.mine.model.UserInfoBean;
import per.goweii.wanandroid.module.mine.presenter.MinePresenter;
import per.goweii.wanandroid.module.mine.view.MineView;
import per.goweii.wanandroid.utils.ImageLoader;
import per.goweii.wanandroid.utils.UserUtils;

/**
 * @author CuiZhen
 * @date 2019/5/12
 * GitHub: https://github.com/goweii
 */
public class MineFragment extends BaseFragment<MinePresenter, MineView> implements MineView {
    ImageView aiv_notification;
    TextView tv_notification;
    SmartRefreshLayout srl;
    NestedScrollView nsv;
    ImageView iv_blur;
    RelativeLayout rl_user_info;
    ImageView civ_user_icon;
    TextView tv_user_name;
    LinearLayout ll_user_level_ranking;
    LinearLayout ll_read_later;
    LinearLayout ll_read_record;
    LinearLayout ll_open;
    LinearLayout ll_about_me;
    TextView tv_user_level;
    TextView tv_user_ranking;
    TextView tv_coin;
    private SmartRefreshUtils mSmartRefreshUtils;

    public static MineFragment create() {
        return new MineFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMineBinding binding = FragmentMineBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        aiv_notification = binding.ab.findViewById(R.id.aiv_notification);
        tv_notification = binding.ab.findViewById(R.id.tv_notification);
        srl = binding.srl;
        nsv = binding.nsv;
        iv_blur = binding.ivBlur;
        rl_user_info = binding.rlUserInfo;
        civ_user_icon = binding.civUserIcon;
        tv_user_name = binding.tvUserName;
        ll_user_level_ranking = binding.llUserLevelRanking;
        ll_read_later = binding.llReadLater;
        ll_read_record = binding.llReadRecord;
        ll_open = binding.llOpen;
        ll_about_me = binding.llAboutMe;
        tv_user_level = binding.tvUserLevel;
        tv_user_ranking = binding.tvUserRanking;
        tv_coin = binding.tvCoin;

        civ_user_icon.setOnClickListener(this);
        tv_user_name.setOnClickListener(this);
        binding.llCollect.setOnClickListener(this);
        ll_read_later.setOnClickListener(this);
        ll_read_record.setOnClickListener(this);
        ll_about_me.setOnClickListener(this);
        ll_open.setOnClickListener(this);
        binding.llSetting.setOnClickListener(this);
        binding.llCoin.setOnClickListener(this);
        binding.llShare.setOnClickListener(this);
        return mRootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (isDetached()) {
            return;
        }
        setRefresh();
        refreshUserInfo();
        loadUserInfo();
        loadNotificationCount();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserInfoUpdateEvent(UserInfoUpdateEvent event) {
        if (isDetached()) {
            return;
        }
        refreshUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageUpdateEvent(MessageUpdateEvent event) {
        if (isDetached()) {
            return;
        }
        presenter.getMessageUnreadCount();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Override
    protected void setUpPresenter() {
        presenter = new MinePresenter();
    }

    @Override
    protected void initViews() {
        aiv_notification.setOnClickListener(v -> {
            if (UserUtils.getInstance().doIfLogin(getViewContext())) {
                MessageActivity.start(requireContext());
                loadNotificationCount();
            }
        });
        nsv.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (nsv == null || rl_user_info == null) return;
            setIvBlurHeight(rl_user_info.getMeasuredHeight() - scrollY);
        });
        srl.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                if (nsv == null || rl_user_info == null) return;
                setIvBlurHeight(rl_user_info.getMeasuredHeight() - nsv.getScrollY() + offset);
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                setIvBlurHeight(rl_user_info.getMeasuredHeight() - nsv.getScrollY() - offset);
            }
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        setRefresh();
    }

    private void setIvBlurHeight(int h) {
        if (iv_blur == null) return;
        if (h >= 0) {
            iv_blur.getLayoutParams().height = h;
        } else {
            iv_blur.getLayoutParams().height = 0;
        }
        iv_blur.requestLayout();
    }

    private void setRefresh() {
        if (UserUtils.getInstance().isLogin()) {
            mSmartRefreshUtils.setRefreshListener(() -> {
                loadUserInfo();
                loadNotificationCount();
            });
        } else {
            mSmartRefreshUtils.setRefreshListener(null);
        }
    }

    @Override
    protected void bindData() {
        refreshUserInfo();
        loadNotificationCount();
    }

    private void loadUserInfo() {
        if (UserUtils.getInstance().isLogin()) {
            presenter.getUserInfo();
        }
    }

    private void loadNotificationCount() {
        presenter.getMessageUnreadCount();
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
        if (isFirstVisible) {
            loadUserInfo();
        }
    }

    @SuppressLint("SetTextI18n")
    private void refreshUserInfo() {
        if (UserUtils.getInstance().isLogin()) {
            UserEntity bean = UserUtils.getInstance().getLoginUser();
            ImageLoader.userIcon(civ_user_icon, bean.getAvatar());
            ImageLoader.userBlur(iv_blur, bean.getCover());
            tv_user_name.setText(bean.getUsername());
            ll_user_level_ranking.setVisibility(View.VISIBLE);
            if (presenter.mUserInfoBean != null) {
                tv_coin.setText(presenter.mUserInfoBean.getCoinCount() + "");
                tv_user_level.setText(presenter.mUserInfoBean.getLevel() + "");
                tv_user_ranking.setText(presenter.mUserInfoBean.getRank() + "");
            } else {
                tv_user_level.setText("--");
                tv_user_ranking.setText("--");
                tv_coin.setText("");
            }
        } else {
            civ_user_icon.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            iv_blur.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            tv_user_name.setText("去登录");
            ll_user_level_ranking.setVisibility(View.INVISIBLE);
            tv_user_level.setText("--");
            tv_user_ranking.setText("--");
            tv_coin.setText("");
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onClickSpace(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_coin:
                if (UserUtils.getInstance().doIfLogin(requireContext())) {
                    CoinActivity.start(requireContext());
                }
                break;
            case R.id.civ_user_icon:
            case R.id.tv_user_name:
                if (UserUtils.getInstance().doIfLogin(requireContext())) {
                    UserInfoActivity.start(requireContext());
                }
                break;
            case R.id.ll_share:
                if (UserUtils.getInstance().doIfLogin(requireContext())) {
                    MineShareActivity.start(requireContext());
                }
                break;
            case R.id.ll_collect:
                if (UserUtils.getInstance().doIfLogin(requireContext())) {
                    CollectionActivity.start(requireContext());
                }
                break;
            case R.id.ll_read_later:
                ReadLaterActivity.start(getViewContext());
                break;
            case R.id.ll_read_record:
                ReadRecordActivity.start(getViewContext());
                break;
            case R.id.ll_about_me:
                AboutMeActivity.start(getViewContext());
                break;
            case R.id.ll_open:
                OpenActivity.start(getViewContext());
                break;
            case R.id.ll_setting:
                SettingActivity.start(getViewContext());
                break;
        }
    }

    @Override
    public void getUserInfoSuccess(int code, UserInfoBean data) {
        mSmartRefreshUtils.success();
        tv_coin.setText(data.getCoinCount() + "");
        ll_user_level_ranking.setVisibility(View.VISIBLE);
        tv_user_level.setText(data.getLevel() + "");
        tv_user_ranking.setText(data.getRank() + "");
    }

    @Override
    public void getUserInfoFail(int code, String msg) {
        mSmartRefreshUtils.fail();
        tv_coin.setText("");
        tv_user_level.setText("--");
        tv_user_ranking.setText("--");
    }

    @Override
    public void getMessageUnreadCountSuccess(int count) {
        MessageCountEvent.post(count);
        if (count > 0) {
            tv_notification.setVisibility(View.VISIBLE);
            if (count > Config.NOTIFICATION_MAX_SHOW_COUNT) {
                tv_notification.setText(Config.NOTIFICATION_MAX_SHOW_COUNT + "+");
            } else {
                tv_notification.setText(count + "");
            }
        } else {
            tv_notification.setVisibility(View.GONE);
        }
    }

}
