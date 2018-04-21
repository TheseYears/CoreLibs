package com.ryan.corelibs.presenter;

import com.corelibs.api.ResponseTransformer;
import com.corelibs.pagination.presenter.ListPagePresenter;
import com.corelibs.subscriber.PaginationSubscriber;
import com.ryan.corelibs.model.api.GithubApi;
import com.ryan.corelibs.model.entity.Data;
import com.ryan.corelibs.model.entity.Repository;
import com.ryan.corelibs.view.interfaces.MainView;

import java.util.List;

public class MainPresenter extends ListPagePresenter<MainView> {

    private GithubApi api;

    @Override
    protected void onViewAttach() {
        api = getApi(GithubApi.class);
    }

    @Override
    public void onStart() {
        search(true);
    }

    public void search(final boolean reload) {
        if (!doPagination(reload)) return;
        if (reload) view.showLoading();

        api.searchRepositories("CoreLibs", getPageNo(), getPageSize())
                .compose(new ResponseTransformer<Data<Repository>>())
                .compose(this.<Data<Repository>>bindToLifeCycle())
                .subscribe(new PaginationSubscriber<Data<Repository>>(view, this, reload) {
                    @Override
                    protected void onDataNotNull(Data<Repository> data) {
                        view.renderResult(data.items, reload);
                    }

                    @Override
                    protected Object getCondition(Data<Repository> data, boolean dataNotNull) {
                        return getListResult(data, dataNotNull);
                    }

                    @Override
                    protected List getListResult(Data<Repository> data, boolean dataNotNull) {
                        return data.items;
                    }
                });
    }
}
