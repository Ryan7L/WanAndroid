package per.goweii.wanandroid.event;

import per.goweii.wanandroid.module.mine.model.MessageBean;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class MessageDeleteEvent extends BaseEvent {

    private MessageBean bean;

    private MessageDeleteEvent(MessageBean bean) {
        this.bean = bean;
    }

    public static void post(MessageBean bean) {
        new MessageDeleteEvent(bean).post();
    }

    public MessageBean getMessageBean() {
        return bean;
    }
}
