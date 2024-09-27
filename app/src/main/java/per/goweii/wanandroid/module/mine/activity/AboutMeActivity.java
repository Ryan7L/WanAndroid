package per.goweii.wanandroid.module.mine.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.actionbarex.common.OnActionBarChildClickListener;
import per.goweii.anypermission.RequestListener;
import per.goweii.anypermission.RuntimeRequester;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.glide.GlideHelper;
import per.goweii.basic.core.permission.PermissionUtils;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.ui.widget.PercentImageView;
import per.goweii.basic.utils.CopyUtils;
import per.goweii.basic.utils.LogUtils;
import per.goweii.basic.utils.RandomUtils;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.wanandroid.BuildConfig;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.ActivityAboutMeBinding;
import per.goweii.wanandroid.module.mine.model.AboutMeBean;
import per.goweii.wanandroid.module.mine.presenter.AboutMePresenter;
import per.goweii.wanandroid.module.mine.view.AboutMeView;
import per.goweii.wanandroid.utils.ImageLoader;
import per.goweii.wanandroid.utils.UrlOpenUtils;
import per.goweii.wanandroid.utils.router.Router;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class AboutMeActivity extends BaseActivity<AboutMePresenter> implements AboutMeView {

    private static final int REQUEST_CODE_PERMISSION = 1;

    //@BindView(R.id.abc)
    ActionBarCommon abc;
    //@BindView(R.id.sl)
    SwipeLayout sl;
    //@BindView(R.id.iv_blur)
    ImageView iv_blur;
    //@BindView(R.id.civ_icon)
    ImageView civ_icon;
    //@BindView(R.id.tv_name)
    TextView tv_name;
    //@BindView(R.id.tv_sign)
    TextView tv_sign;
    //@BindView(R.id.ll_github)
    LinearLayout ll_github;
    //@BindView(R.id.ll_jianshu)
    LinearLayout ll_jianshu;
    //@BindView(R.id.ll_qq)
    LinearLayout ll_qq;
    //@BindView(R.id.ll_qq_group)
    LinearLayout ll_qq_group;
    //@BindView(R.id.tv_github)
    TextView tv_github;
    //@BindView(R.id.tv_jianshu)
    TextView tv_jianshu;
    //@BindView(R.id.tv_qq)
    TextView tv_qq;
    //@BindView(R.id.tv_qq_group)
    TextView tv_qq_group;
    //@BindView(R.id.rl_info)
    RelativeLayout rl_info;
    //@BindView(R.id.rl_reward)
    RelativeLayout rl_reward;
    //@BindView(R.id.piv_zfb_qrcode)
    PercentImageView piv_zfb_qrcode;
    //@BindView(R.id.piv_wx_qrcode)
    PercentImageView piv_wx_qrcode;
    private RuntimeRequester mRuntimeRequester;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutMeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initBinding() {
        ActivityAboutMeBinding binding = ActivityAboutMeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        sl = binding.sl;
        iv_blur = binding.ivBlur;
        civ_icon = binding.infoLayout.civIcon;
        tv_name = binding.infoLayout.tvName;
        tv_sign = binding.infoLayout.tvSign;
        ll_github = binding.infoLayout.llGithub;
        ll_jianshu = binding.infoLayout.llJianshu;
        ll_qq = binding.infoLayout.llQq;
        ll_qq_group = binding.infoLayout.llQqGroup;
        tv_github = binding.infoLayout.tvGithub;
        tv_jianshu = binding.infoLayout.tvJianshu;
        tv_qq = binding.infoLayout.tvQq;
        tv_qq_group = binding.infoLayout.tvQqGroup;
        rl_info = binding.infoLayout.rlInfo;
        rl_reward = binding.sponsorLayout.rlReward;
        piv_zfb_qrcode = binding.sponsorLayout.pivZfbQrcode;
        piv_wx_qrcode = binding.sponsorLayout.pivWxQrcode;

        ll_github.setOnClickListener(this);
        ll_jianshu.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_qq_group.setOnClickListener(this);
        piv_zfb_qrcode.setOnClickListener(this);
        piv_wx_qrcode.setOnClickListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_me;
    }

    @Nullable
    @Override
    protected AboutMePresenter initPresenter() {
        return new AboutMePresenter();
    }

    @Override
    protected void initViews() {
        abc.setOnRightTextClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder s = new StringBuilder();
                s.append("【玩口令】你的好友给你订了一份双人咖啡，户制泽条消息");
                s.append(String.format(BuildConfig.WANPWD_FORMAT, BuildConfig.WANPWD_TYPE_ABOUTME, RandomUtils.randomLetter(10)));
                s.append("打開最美玩安卓客户端即可领取品尝");
                LogUtils.d("UserPageActivity", s);
                CopyUtils.copyText(s.toString());
                ToastMaker.showShort("口令已复制");
            }
        });
        changeVisible(View.INVISIBLE, civ_icon, tv_name, tv_sign);
        changeVisible(View.GONE, ll_github, ll_jianshu, ll_qq, ll_qq_group);
    }

    @Override
    protected void bindData() {
        presenter.getAboutMe();

        Uri uri = Router.getUriFrom(getIntent());
        if (uri != null) {
            boolean secondFloor = uri.getBooleanQueryParameter("second_floor", false);
            if (secondFloor) {
                sl.post(new Runnable() {
                    @Override
                    public void run() {
                        sl.open(true, true, SwipeLayout.DragEdge.Bottom);
                    }
                });
            }
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
            case R.id.ll_github:
                UrlOpenUtils.Companion
                        .with(tv_github.getText().toString())
                        .title(tv_name.getText().toString())
                        .open(getContext());
                break;
            case R.id.ll_jianshu:
                UrlOpenUtils.Companion
                        .with(tv_jianshu.getText().toString())
                        .title(tv_name.getText().toString())
                        .open(getContext());
                break;
            case R.id.ll_qq:
                presenter.openQQChat();
                break;
            case R.id.ll_qq_group:
                presenter.openQQGroup();
                break;
            case R.id.piv_zfb_qrcode:
                mRuntimeRequester = PermissionUtils.request(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        presenter.saveZFBQrcode();
                    }

                    @Override
                    public void onFailed() {
                    }
                }, getContext(), REQUEST_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.piv_wx_qrcode:
                mRuntimeRequester = PermissionUtils.request(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        presenter.saveWXQrcode();
                    }

                    @Override
                    public void onFailed() {
                    }
                }, getContext(), REQUEST_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
    }

    private void changeVisible(int visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    private void changeViewSize(final View target, float from, float to, long dur) {
        final ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(dur);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (target == null) {
                    animator.cancel();
                    return;
                }
                float f = (float) animator.getAnimatedValue();
                target.setScaleX(f);
                target.setScaleY(f);
            }
        });
        animator.start();
    }

    private void changeViewAlpha(final View target, float from, float to, long dur) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "alpha", from, to);
        animator.setDuration(dur);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void doDelayShowAnim(long dur, long delay, View... targets) {
        for (int i = 0; i < targets.length; i++) {
            final View target = targets[i];
            target.setAlpha(0);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, "translationY", 100, 0);
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
            animatorY.setDuration(dur);
            animatorA.setDuration((long) (dur * 0.618F));
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(animatorA, animatorY);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setStartDelay(delay * i);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    changeVisible(View.VISIBLE, target);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    @Override
    public void getAboutMeSuccess(int code, AboutMeBean data) {
        ImageLoader.image(piv_zfb_qrcode, data.getZfb_qrcode());
        ImageLoader.image(piv_wx_qrcode, data.getWx_qrcode());
        GlideHelper.with(getContext())
                .asBitmap()
                .load(data.getIcon())
                .getBitmap(new SimpleCallback<Bitmap>() {
                    @Override
                    public void onResult(Bitmap bitmap) {
                        ImageLoader.userIcon(civ_icon, data.getIcon());
                        ImageLoader.userBlur(iv_blur, data.getIcon());
                        iv_blur.setAlpha(0F);
                        iv_blur.post(new Runnable() {
                            @Override
                            public void run() {
                                changeViewAlpha(iv_blur, 0, 1, 600);
                                changeViewSize(iv_blur, 2, 1, 1000);
                            }
                        });
                    }
                });

        List<View> targets = new ArrayList<>();
        targets.add(civ_icon);
        if (!TextUtils.isEmpty(data.getName())) {
            tv_name.setText(data.getName());
            targets.add(tv_name);
        }
        if (!TextUtils.isEmpty(data.getDesc())) {
            tv_sign.setText(data.getDesc());
            targets.add(tv_sign);
        }
        if (!TextUtils.isEmpty(data.getGithub())) {
            tv_github.setText(data.getGithub());
            targets.add(ll_github);
        }
        if (!TextUtils.isEmpty(data.getJianshu())) {
            tv_jianshu.setText(data.getJianshu());
            targets.add(ll_jianshu);
        }
        if (!TextUtils.isEmpty(data.getQq())) {
            tv_qq.setText(data.getQq());
            targets.add(ll_qq);
        }
        if (!TextUtils.isEmpty(data.getQq_group())) {
            tv_qq_group.setText(data.getQq_group());
            targets.add(ll_qq_group);
        }
        civ_icon.post(new Runnable() {
            @Override
            public void run() {
                changeViewSize(civ_icon, 0, 1, 300);
                doDelayShowAnim(800, 60, targets.toArray(new View[0]));
            }
        });
    }

    @Override
    public void getAboutMeFailed(int code, String msg) {
        ToastMaker.showShort(msg);
    }
}
