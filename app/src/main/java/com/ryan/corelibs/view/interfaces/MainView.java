package com.ryan.corelibs.view.interfaces;

import com.corelibs.base.BasePaginationView;
import com.ryan.corelibs.model.entity.Repository;

import java.util.List;

public interface MainView extends BasePaginationView {
    void renderResult(List<Repository> repositories, boolean reload);
}
