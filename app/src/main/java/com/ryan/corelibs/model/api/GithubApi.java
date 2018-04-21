package com.ryan.corelibs.model.api;

import com.ryan.corelibs.constants.Urls;
import com.ryan.corelibs.model.entity.Data;
import com.ryan.corelibs.model.entity.Repository;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubApi {
    @GET(Urls.SEARCH)
    Observable<Data<Repository>> searchRepositories(
            @Query("q") String key, @Query("page") int page, @Query("per_page") int pageSize);
}
