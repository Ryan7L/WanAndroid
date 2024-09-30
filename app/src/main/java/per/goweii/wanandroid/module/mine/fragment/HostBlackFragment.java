package per.goweii.wanandroid.module.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.utils.listener.OnClickListener2;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.FragmentHostInterruptBinding;
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

    RecyclerView rv;
    private HostInterruptAdapter mAdapter = null;

    public static HostBlackFragment create() {
        return new HostBlackFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHostInterruptBinding binding = FragmentHostInterruptBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        rv = binding.rv;
        return mRootView;
    }


    @Override
    protected void initViews() {
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new HostInterruptAdapter();
        RvConfigUtils.init(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> mAdapter.remove(position));
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            mAdapter.getData().get(position).setEnable(!mAdapter.getData().get(position).isEnable());
            mAdapter.notifyItemChanged(position);
        });
        mAdapter.setOnCheckedChangeListener((position, isChecked) -> mAdapter.getData().get(position).setEnable(isChecked));
        View footer = LayoutInflater.from(getViewContext()).inflate(R.layout.rv_item_host_interrupt_footer, null);
        footer.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                AddHostDialog.show(getViewContext(), data -> mAdapter.addData(new HostEntity(data, true)));
            }
        });
        mAdapter.addFooterView(footer);
        rv.setAdapter(mAdapter);
    }

    @Override
    protected void bindData() {
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
