package per.goweii.wanandroid.module.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;

import per.goweii.actionbarex.common.ActionBarSuper;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.AnimatorUtils;
import per.goweii.basic.utils.listener.OnClickListener2;
import per.goweii.wanandroid.databinding.ActivityCoinBinding;
import per.goweii.wanandroid.module.main.dialog.WebDialog;
import per.goweii.wanandroid.module.mine.adapter.CoinRecordAdapter;
import per.goweii.wanandroid.module.mine.model.CoinRecordBean;
import per.goweii.wanandroid.module.mine.presenter.CoinPresenter;
import per.goweii.wanandroid.module.mine.view.CoinView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvConfigUtils;

/**
 * @author CuiZhen
 * @date 2019/8/31
 * GitHub: https://github.com/goweii
 */
public class CoinActivity extends BaseActivity<CoinPresenter, CoinView> implements CoinView {

    private static final int PAGE_START = 1;

    ActionBarSuper abc;
    TextView tv_coin;
    MultiStateView msv;
    RecyclerView rv;
    private int currPage = PAGE_START;
    private CoinRecordAdapter mCoinRecordAdapter = null;

    public static void start(Context context) {
        Intent intent = new Intent(context, CoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initContentView() {
        ActivityCoinBinding binding = ActivityCoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        tv_coin = binding.tvCoin;
        msv = binding.msv;
        rv = binding.rv;
    }


    @Override
    protected void setUpPresenter() {
        presenter = new CoinPresenter();
    }

    @Override
    protected void initViews() {
        abc.getRightActionView(0).setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                WebDialog.create(getViewContext(), "https://www.wanandroid.com/blog/show/2653").show();
            }
        });
        abc.getRightActionView(1).setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                CoinRankActivity.start(getViewContext());
            }
        });
        tv_coin.setText("0");
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mCoinRecordAdapter = new CoinRecordAdapter();
        RvConfigUtils.init(mCoinRecordAdapter);
        mCoinRecordAdapter.setEnableLoadMore(false);
        mCoinRecordAdapter.setOnLoadMoreListener(() -> presenter.getCoinRecordList(currPage), rv);
        rv.setAdapter(mCoinRecordAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            currPage = PAGE_START;
            presenter.getCoinRecordList(currPage);
        });
    }

    @Override
    protected void bindData() {
        presenter.getCoin();
        MultiStateUtils.toLoading(msv);
        currPage = PAGE_START;
        presenter.getCoinRecordList(currPage);
    }

    @Override
    public void getCoinSuccess(int code, int coin) {
        AnimatorUtils.doIntAnim(tv_coin, coin, 1000);
    }

    @Override
    public void getCoinFail(int code, String msg) {
        ToastMaker.showShort(msg);
    }

    @Override
    public void getCoinRecordListSuccess(int code, CoinRecordBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            mCoinRecordAdapter.setNewData(data.getDatas());
            mCoinRecordAdapter.setEnableLoadMore(true);
            if (data.getDatas() == null || data.getDatas().isEmpty()) {
                MultiStateUtils.toEmpty(msv);
            } else {
                MultiStateUtils.toContent(msv);
            }
        } else {
            mCoinRecordAdapter.addData(data.getDatas());
            mCoinRecordAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mCoinRecordAdapter.loadMoreEnd();
        }
    }

    @Override
    public void getCoinRecordListFail(int code, String msg) {
        mCoinRecordAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            MultiStateUtils.toError(msv);
        }
    }
}
