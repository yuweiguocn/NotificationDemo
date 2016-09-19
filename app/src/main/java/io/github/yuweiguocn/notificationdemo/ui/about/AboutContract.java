package io.github.yuweiguocn.notificationdemo.ui.about;


import io.github.yuweiguocn.notificationdemo.base.BasePresenter;
import io.github.yuweiguocn.notificationdemo.base.BaseView;
import io.github.yuweiguocn.notificationdemo.bean.About;

/**
 * Created by growth on 7/9/16.
 */
public interface AboutContract {
    interface Presenter extends BasePresenter {
        void loadData();
    }

    interface View extends BaseView<Presenter> {
        void onSucess(About about);
    }
}
