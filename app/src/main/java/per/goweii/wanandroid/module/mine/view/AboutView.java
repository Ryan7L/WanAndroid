package per.goweii.wanandroid.module.mine.view;

import per.goweii.basic.core.base.BaseView;
import per.goweii.wanandroid.module.main.model.UpdateBean;

/**
 * @author CuiZhen
 * @date 2019/5/19
 * GitHub: https://github.com/goweii
 */
public interface AboutView extends BaseView {
    void updateSuccess(int code, UpdateBean data);

    void updateFailed(int code, String msg);
}
