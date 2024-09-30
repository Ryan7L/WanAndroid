package per.goweii.wanandroid.module.mine.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.wanandroid.databinding.ActivityCoinRankBinding;
import per.goweii.wanandroid.module.home.activity.UserPageActivity;
import per.goweii.wanandroid.module.main.model.CoinInfoBean;
import per.goweii.wanandroid.module.mine.adapter.CoinRankAdapter;
import per.goweii.wanandroid.module.mine.model.CoinRankBean;
import per.goweii.wanandroid.module.mine.presenter.CoinRankPresenter;
import per.goweii.wanandroid.module.mine.view.CoinRankView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvConfigUtils;

/**
 * @author CuiZhen
 * @date 2019/8/31
 * GitHub: https://github.com/goweii
 */
public class CoinRankActivity extends BaseActivity<CoinRankPresenter, CoinRankView> implements CoinRankView {

    private static final int PAGE_START = 1;

    ActionBarCommon abc;
    MultiStateView msv;
    RecyclerView rv;
    private int currPage = PAGE_START;
    private CoinRankAdapter mAdapter = null;

    public static void start(Context context) {
        Intent intent = new Intent(context, CoinRankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initContentView() {
        ActivityCoinRankBinding binding = ActivityCoinRankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        msv = binding.msv;
        rv = binding.rv;
    }


    @Nullable
    @Override
    protected void setUpPresenter() {
        presenter = new CoinRankPresenter();
    }

    @Override
    protected void initViews() {
        abc.setOnRightIconClickListener(v -> {

        });
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new CoinRankAdapter();
        RvConfigUtils.init(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> presenter.getCoinRankList(currPage), rv);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            CoinInfoBean item = mAdapter.getItem(position);
            if (item != null) {
                UserPageActivity.start(getViewContext(), item.getUserId());
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            currPage = PAGE_START;
            presenter.getCoinRankList(currPage);
        });
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        currPage = PAGE_START;
        presenter.getCoinRankList(currPage);
    }

    @Override
    public void getCoinRankListSuccess(int code, CoinRankBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            mAdapter.setNewData(data.getDatas());
            mAdapter.setEnableLoadMore(true);
            if (data.getDatas() == null || data.getDatas().isEmpty()) {
                MultiStateUtils.toEmpty(msv);
            } else {
                MultiStateUtils.toContent(msv);
            }
        } else {
            mAdapter.addData(data.getDatas());
            mAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mAdapter.loadMoreEnd();
        }
    }

    @Override
    public void getCoinRankListFail(int code, String msg) {
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            MultiStateUtils.toError(msv);
        }
    }
}
