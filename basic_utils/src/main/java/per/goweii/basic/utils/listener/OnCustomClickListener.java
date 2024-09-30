package per.goweii.basic.utils.listener;

import android.view.View;

import per.goweii.basic.utils.ClickHelper;

/**
 * @author Cuizhen
 * @date 2018/5/7-下午4:40
 */
public abstract class OnCustomClickListener implements View.OnClickListener {

    @Override
    public final void onClick(final View v) {
        ClickHelper.onlyFirstSameView(v, this::onCustomClick);
    }

    public abstract void onCustomClick(View v);
}
