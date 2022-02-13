package per.goweii.wanandroid.module.mine.fragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.core.base.BasePresenter;
import per.goweii.basic.utils.listener.OnClickListener2;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.module.mine.adapter.HostInterruptAdapter;
import per.goweii.wanandroid.module.mine.dialog.AddHostDialog;
import per.goweii.wanandroid.module.mine.model.HostEntity;
import per.goweii.wanandroid.utils.RvConfigUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;
import per.goweii.wanandroid.utils.SettingUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class HostBlackFragment extends BaseFragment implements RvScrollTopUtils.ScrollTop {

    @BindView(R.id.rv)
    RecyclerView rv;

    private HostInterruptAdapter mAdapter = null;

    public static HostBlackFragment create() {
        return new HostBlackFragment();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_host_interrupt;
    }

    @Nullable
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HostInterruptAdapter();
        RvConfigUtils.init(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.remove(position);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.getData().get(position).setEnable(!mAdapter.getData().get(position).isEnable());
                mAdapter.notifyItemChanged(position);
            }
        });
        mAdapter.setOnCheckedChangeListener(new HostInterruptAdapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(int position, boolean isChecked) {
                mAdapter.getData().get(position).setEnable(isChecked);
            }
        });
        View footer = LayoutInflater.from(getContext()).inflate(R.layout.rv_item_host_interrupt_footer, null);
        footer.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                AddHostDialog.show(getContext(), new SimpleCallback<String>() {
                    @Override
                    public void onResult(String data) {
                        mAdapter.addData(new HostEntity(data, true));
                    }
                });
            }
        });
        mAdapter.addFooterView(footer);
        rv.setAdapter(mAdapter);
    }

    @Override
    protected void loadData() {
        List<HostEntity> list = SettingUtils.getInstance().getHostBlackIntercept();
        List<HostEntity> newList = new ArrayList<>(list);
        mAdapter.setNewData(newList);
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        SettingUtils.getInstance().setHostBlackIntercept(mAdapter.getData());
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
}
