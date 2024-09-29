package per.goweii.wanandroid.module.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.actionbarex.common.OnActionBarChildClickListener;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.utils.SmartRefreshUtils;
import per.goweii.basic.ui.dialog.TipDialog;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.CopyUtils;
import per.goweii.basic.utils.IntentUtils;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.basic.utils.listener.SimpleListener;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.ActivityReadRecordBinding;
import per.goweii.wanandroid.db.model.ReadRecordModel;
import per.goweii.wanandroid.event.ReadRecordAddedEvent;
import per.goweii.wanandroid.module.mine.adapter.ReadRecordAdapter;
import per.goweii.wanandroid.module.mine.presenter.ReadRecordPresenter;
import per.goweii.wanandroid.module.mine.view.ReadRecordView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvConfigUtils;
import per.goweii.wanandroid.utils.UrlOpenUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class ReadRecordActivity extends BaseActivity<ReadRecordPresenter,ReadRecordView> implements ReadRecordView {

    ActionBarCommon abc;
    MultiStateView msv;
    SmartRefreshLayout srl;
    RecyclerView rv;
    private SmartRefreshUtils mSmartRefreshUtils;
    private ReadRecordAdapter mAdapter;
    private int offset = 0;
    private int perPageCount = 20;
    private boolean loading = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, ReadRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initBinding() {
        ActivityReadRecordBinding binding = ActivityReadRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        msv = binding.msv;
        srl = binding.srl;
        rv = binding.rv;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadRecordEvent(ReadRecordAddedEvent event) {
        if (isDestroyed()) {
            return;
        }
        if (offset == 0 && loading) {
            return;
        }
        offset = 0;
        presenter.getList(offset, perPageCount);
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_record;
    }

    @Nullable
    @Override
    protected ReadRecordPresenter initPresenter() {
        return new ReadRecordPresenter();
    }

    @Override
    protected void initViews() {
        abc.setOnRightTextClickListener(v -> TipDialog.with(getContext())
                .message("确定要全部删除吗？")
                .onYes(new SimpleCallback<Void>() {
                    @Override
                    public void onResult(Void data) {
                        presenter.removeAll();
                    }
                })
                .show());
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(() -> {
            offset = 0;
            getPageList();
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ReadRecordAdapter();
        RvConfigUtils.init(mAdapter);
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> getPageList(), rv);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mAdapter.closeAll(null);
            ReadRecordModel item = mAdapter.getItem(position);
            if (item == null) {
                return;
            }
            switch (view.getId()) {
                default:
                    break;
                case R.id.rl_top:
                    UrlOpenUtils.Companion
                            .with(item.getLink())
                            .title(item.getTitle())
                            .open(getContext());
                    break;
                case R.id.tv_copy:
                    CopyUtils.copyText(item.getLink());
                    ToastMaker.showShort("复制成功");
                    break;
                case R.id.tv_open:
                    if (TextUtils.isEmpty(item.getLink())) {
                        ToastMaker.showShort("链接为空");
                        break;
                    }
                    if (getContext() != null) {
                        IntentUtils.openBrowser(getContext(), item.getLink());
                    }
                    break;
                case R.id.tv_delete:
                    ReadRecordModel model = mAdapter.getItem(position);
                    if (model != null) {
                        presenter.remove(model.getLink());
                    }
                    break;
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            offset = 0;
            getPageList();
        });
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        offset = 0;
        getPageList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getPageList() {
        loading = true;
        presenter.getList(offset, perPageCount);
    }

    @Override
    public void getReadRecordListSuccess(List<ReadRecordModel> list) {
        mSmartRefreshUtils.success();
        if (offset == 0) {
            mAdapter.setNewData(list);
            if (list.isEmpty()) {
                MultiStateUtils.toEmpty(msv, true);
            } else {
                MultiStateUtils.toContent(msv);
            }
        } else {
            mAdapter.addData(list);
            mAdapter.loadMoreComplete();
        }
        offset = mAdapter.getData().size();
        if (list.size() < perPageCount) {
            mAdapter.loadMoreEnd();
        }
        loading = false;
    }

    @Override
    public void getReadRecordListFailed() {
        mSmartRefreshUtils.fail();
        if (offset == 0) {
            MultiStateUtils.toError(msv);
        } else {
            mAdapter.loadMoreFail();
        }
        loading = false;
    }

    @Override
    public void removeReadRecordSuccess(String link) {
        List<ReadRecordModel> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            ReadRecordModel model = list.get(i);
            if (TextUtils.equals(model.getLink(), link)) {
                mAdapter.remove(i);
                break;
            }
        }
        offset = mAdapter.getData().size();
        if (mAdapter.getData().isEmpty()) {
            MultiStateUtils.toEmpty(msv, true);
        }
    }

    @Override
    public void removeReadRecordFailed() {
        ToastMaker.showShort("删除失败");
    }

    @Override
    public void removeAllReadRecordSuccess() {
        mAdapter.setNewData(null);
        offset = mAdapter.getData().size();
        MultiStateUtils.toEmpty(msv, true);
    }

    @Override
    public void removeAllReadRecordFailed() {
        ToastMaker.showShort("删除失败");
    }
}
