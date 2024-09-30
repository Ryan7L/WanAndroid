package per.goweii.wanandroid.module.mine.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import per.goweii.anypermission.RequestListener;
import per.goweii.anypermission.RuntimeRequester;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.permission.PermissionUtils;
import per.goweii.basic.ui.dialog.ListDialog;
import per.goweii.basic.ui.dialog.TipDialog;
import per.goweii.basic.ui.dialog.UpdateDialog;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.AppInfoUtils;
import per.goweii.basic.utils.ResUtils;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.rxhttp.request.base.BaseBean;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Constant;
import per.goweii.wanandroid.common.WanApp;
import per.goweii.wanandroid.databinding.ActivitySettingBinding;
import per.goweii.wanandroid.event.LoginEvent;
import per.goweii.wanandroid.event.SettingChangeEvent;
import per.goweii.wanandroid.module.main.dialog.DownloadDialog;
import per.goweii.wanandroid.module.main.model.UpdateBean;
import per.goweii.wanandroid.module.mine.presenter.SettingPresenter;
import per.goweii.wanandroid.module.mine.view.SettingView;
import per.goweii.wanandroid.utils.DarkModeUtils;
import per.goweii.wanandroid.utils.SettingUtils;
import per.goweii.wanandroid.utils.UpdateUtils;
import per.goweii.wanandroid.utils.UrlOpenUtils;
import per.goweii.wanandroid.utils.UserUtils;
import per.goweii.wanandroid.utils.web.HostInterceptUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class SettingActivity extends BaseActivity<SettingPresenter,SettingView> implements SettingView {

    private static final int REQ_CODE_PERMISSION = 1;

    SwitchCompat sc_system_theme;
    TextView tv_dark_theme_title;
    TextView tv_show_read_later_notification_title;
    TextView tv_show_read_later_notification_desc;
    SwitchCompat sc_dark_theme;
    SwitchCompat sc_show_read_later_notification;
    SwitchCompat sc_show_top;
    SwitchCompat sc_show_banner;
    TextView tv_intercept_host;
    TextView tv_cache;
    TextView tv_has_update;
    TextView tv_curr_version;
    LinearLayout ll_logout;
    private RuntimeRequester mRuntimeRequester;
    private UpdateUtils mUpdateUtils;
    private boolean mSystemTheme;
    private boolean mDarkTheme;
    private boolean mShowBanner;
    private boolean mShowTop;
    private boolean mShowReadLaterNotification;
    private int mUrlIntercept;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initContentView() {
        ActivitySettingBinding binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sc_system_theme = binding.scSystemTheme;
        tv_dark_theme_title = binding.tvDarkThemeTitle;
        tv_show_read_later_notification_title = binding.tvShowReadLaterNotificationTitle;
        tv_show_read_later_notification_desc = binding.tvShowReadLaterNotificationDesc;
        sc_dark_theme = binding.scDarkTheme;
        sc_show_read_later_notification = binding.scShowReadLaterNotification;
        sc_show_top = binding.scShowTop;
        sc_show_banner = binding.scShowBanner;
        tv_intercept_host = binding.tvInterceptHost;
        tv_cache = binding.tvCache;
        tv_has_update = binding.tvHasUpdate;
        tv_curr_version = binding.tvCurrVersion;
        ll_logout = binding.llLogout;
        binding.rlInterceptHost.setOnClickListener(this);
        binding.llUpdate.setOnClickListener(this);
        binding.llCache.setOnClickListener(this);
        binding.llAbout.setOnClickListener(this);
        binding.llPrivacyPolicy.setOnClickListener(this);
        binding.llLogout.setOnClickListener(this);
        binding.rlInterceptHost.setOnLongClickListener(this::onLongClick);
    }


    @Nullable
    @Override
    protected void setUpPresenter() {
        presenter =  new SettingPresenter();
    }

    @Override
    protected void initViews() {
        tv_has_update.setText("");
        tv_curr_version.setText("当前版本" + AppInfoUtils.getVersionName());
        mSystemTheme = SettingUtils.getInstance().isSystemTheme();
        sc_system_theme.setChecked(mSystemTheme);
        changeEnable(!sc_system_theme.isChecked(), tv_dark_theme_title, sc_dark_theme);
        mDarkTheme = SettingUtils.getInstance().isDarkTheme();
        sc_dark_theme.setChecked(mDarkTheme);
        mShowTop = SettingUtils.getInstance().isShowTop();
        sc_show_top.setChecked(mShowTop);
        mShowBanner = SettingUtils.getInstance().isShowBanner();
        sc_show_banner.setChecked(mShowBanner);
        mShowReadLaterNotification = SettingUtils.getInstance().isShowReadLaterNotification();
        sc_show_read_later_notification.setChecked(mShowReadLaterNotification);
        mUrlIntercept = SettingUtils.getInstance().getUrlInterceptType();
        tv_intercept_host.setText(HostInterceptUtils.getName(mUrlIntercept));
        sc_system_theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                changeEnable(!isChecked, tv_dark_theme_title, sc_dark_theme);
                SettingUtils.getInstance().setSystemTheme(isChecked);
                DarkModeUtils.initDarkMode();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WanApp.restartApp();
                    }
                }, 300);
            }
        });
        sc_dark_theme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                SettingUtils.getInstance().setDarkTheme(isChecked);
                DarkModeUtils.initDarkMode();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WanApp.recreate();
                    }
                }, 300);
            }
        });
        sc_show_top.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                SettingUtils.getInstance().setShowTop(isChecked);
            }
        });
        sc_show_banner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                SettingUtils.getInstance().setShowBanner(isChecked);
            }
        });
        sc_show_read_later_notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton v, boolean isChecked) {
                SettingUtils.getInstance().setShowReadLaterNotification(isChecked);
            }
        });
        if (UserUtils.getInstance().isLogin()) {
            ll_logout.setVisibility(View.VISIBLE);
        } else {
            ll_logout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void bindData() {
        presenter.update(false);
        presenter.getCacheSize();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        postSettingChangedEvent();
    }

    private void changeEnable(boolean enable, View... views) {
        for (View view : views) {
            if (enable) {
                view.setEnabled(true);
                view.setAlpha(1F);
            } else {
                view.setEnabled(false);
                view.setAlpha(0.5F);
            }
        }
    }

    private void postSettingChangedEvent() {
        boolean showTopChanged = mShowTop != SettingUtils.getInstance().isShowTop();
        boolean showBannerChanged = mShowBanner != SettingUtils.getInstance().isShowBanner();
        boolean urlInterceptChanged = mUrlIntercept != SettingUtils.getInstance().getUrlInterceptType();
        if (showTopChanged || showBannerChanged || urlInterceptChanged) {
            SettingChangeEvent event = new SettingChangeEvent();
            event.setShowTopChanged(showTopChanged);
            event.setShowBannerChanged(showBannerChanged);
            event.post();
        }
    }

    public boolean onLongClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.rl_intercept_host:
                HostInterruptActivity.start(getViewContext());
                break;
        }
        return true;
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
            case R.id.rl_intercept_host:
                ListDialog.with(getViewContext())
                        .cancelable(true)
//                        .title("网页拦截")
                        .datas(HostInterceptUtils.getName(HostInterceptUtils.TYPE_NOTHING),
                                HostInterceptUtils.getName(HostInterceptUtils.TYPE_ONLY_WHITE),
                                HostInterceptUtils.getName(HostInterceptUtils.TYPE_INTERCEPT_BLACK))
                        .currSelectPos(SettingUtils.getInstance().getUrlInterceptType())
                        .listener(new ListDialog.OnItemSelectedListener() {
                            @Override
                            public void onSelect(String data, int pos) {
                                tv_intercept_host.setText(HostInterceptUtils.getName(pos));
                                SettingUtils.getInstance().setUrlInterceptType(pos);
                            }
                        })
                        .show();
                break;
            case R.id.ll_update:
                presenter.update(true);
                break;
            case R.id.ll_cache:
                TipDialog.with(getViewContext())
                        .message("确定要清除缓存吗？")
                        .onYes(new SimpleCallback<Void>() {
                            @Override
                            public void onResult(Void data) {
                                presenter.clearCache();
                            }
                        })
                        .show();
                break;
            case R.id.ll_about:
                AboutActivity.start(getViewContext());
                break;
            case R.id.ll_privacy_policy:
                UrlOpenUtils.Companion
                        .with(Constant.PRIVACY_POLICY_URL)
                        .open(getViewContext());
                break;
            case R.id.ll_logout:
                TipDialog.with(getViewContext())
                        .message("确定要退出登录吗？")
                        .onYes(new SimpleCallback<Void>() {
                            @Override
                            public void onResult(Void data) {
                                presenter.logout();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public void updateSuccess(int code, UpdateBean data, boolean click) {
        if (mUpdateUtils == null) {
            mUpdateUtils = UpdateUtils.newInstance();
        }
        if (mUpdateUtils.isNewest(data)) {
            tv_has_update.setTextColor(ResUtils.getThemeColor(getViewContext(), R.attr.colorTextMain));
            tv_has_update.setText("发现新版本");
            if (click) {
                UpdateDialog.with(getViewContext())
                        .setUrl(data.getUrl())
                        .setUrlBackup(data.getUrl_backup())
                        .setVersionCode(data.getVersion_code())
                        .setVersionName(data.getVersion_name())
                        .setForce(mUpdateUtils.shouldForceUpdate(data))
                        .setDescription(data.getDesc())
                        .setTime(data.getTime())
                        .setOnUpdateListener(new UpdateDialog.OnUpdateListener() {
                            @Override
                            public void onDownload(String url, String urlBackup, boolean isForce) {
                                download(url, urlBackup, isForce);
                            }

                            @Override
                            public void onIgnore(String versionName, int versionCode) {
                                mUpdateUtils.ignore(versionCode);
                            }
                        })
                        .show();
            }
        } else {
            presenter.betaUpdate(click);
        }
    }

    @Override
    public void updateFailed(int code, String msg, boolean click) {
        tv_has_update.setTextColor(ResUtils.getThemeColor(getViewContext(), R.attr.colorTextThird));
        tv_has_update.setText("已是最新版");
    }

    @Override
    public void betaUpdateSuccess(int code, UpdateBean data, boolean click) {
        if (mUpdateUtils.isNewest(data)) {
            tv_has_update.setTextColor(ResUtils.getThemeColor(getViewContext(), R.attr.colorTextAccent));
            tv_has_update.setText("发现内测版本");
            if (click) {
                UpdateDialog.with(getViewContext())
                        .setTest(true)
                        .setUrl(data.getUrl())
                        .setUrlBackup(data.getUrl_backup())
                        .setVersionCode(data.getVersion_code())
                        .setVersionName(data.getVersion_name())
                        .setForce(mUpdateUtils.shouldForceUpdate(data))
                        .setDescription(data.getDesc())
                        .setTime(data.getTime())
                        .setOnUpdateListener(new UpdateDialog.OnUpdateListener() {
                            @Override
                            public void onDownload(String url, String urlBackup, boolean isForce) {
                                download(url, urlBackup, isForce);
                            }

                            @Override
                            public void onIgnore(String versionName, int versionCode) {
                                mUpdateUtils.ignoreBeta(versionName, versionCode);
                            }
                        })
                        .show();
            }
        } else {
            tv_has_update.setTextColor(ResUtils.getThemeColor(getViewContext(), R.attr.colorTextThird));
            tv_has_update.setText("已是最新版");
            if (click) {
                ToastMaker.showShort("已是最新版");
            }
        }
    }

    @Override
    public void betaUpdateFailed(int code, String msg, boolean click) {
        tv_has_update.setTextColor(ResUtils.getThemeColor(getViewContext(), R.attr.colorTextThird));
        tv_has_update.setText("已是最新版");
        if (click) {
            ToastMaker.showShort("已是最新版");
        }
    }

    @Override
    public void logoutSuccess(int code, BaseBean data) {
        UserUtils.getInstance().logout();
        new LoginEvent(false).post();
        finish();
    }

    @Override
    public void logoutFailed(int code, String msg) {
        ToastMaker.showShort(msg);
    }

    @Override
    public void getCacheSizeSuccess(String size) {
        tv_cache.setText(size);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
    }

    private void download(final String url, final String urlBackup, final boolean isForce) {
        mRuntimeRequester = PermissionUtils.request(new RequestListener() {
            @Override
            public void onSuccess() {
                DownloadDialog.with(getActivity(), isForce, url, urlBackup, null);
            }

            @Override
            public void onFailed() {
            }
        }, getViewContext(), REQ_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}
