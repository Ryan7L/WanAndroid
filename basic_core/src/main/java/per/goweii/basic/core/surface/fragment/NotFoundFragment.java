package per.goweii.basic.core.surface.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import per.goweii.basic.core.R;
import per.goweii.basic.core.base.BaseFragment;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/29
 */
public class NotFoundFragment extends BaseFragment {

    public static NotFoundFragment create(String msg) {
        NotFoundFragment fragment = new NotFoundFragment();
        Bundle args = new Bundle(1);
        args.putString("msg", msg);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.basic_core_fragment_not_found, container, false);
    }

    @Override
    protected void initViews() {
        TextView tvMsg = findViewById(R.id.basic_core_tv_msg);
        Bundle args = getArguments();
        if (args != null) {
            String msg = args.getString("msg");
            if (!TextUtils.isEmpty(msg)) {
                tvMsg.setText(msg);
            }
        }
    }

    @Override
    protected void bindData() {
    }
}
