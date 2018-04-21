package com.ryan.corelibs.adapter;

import android.content.Context;

import com.corelibs.utils.adapter.BaseAdapterHelper;
import com.corelibs.utils.adapter.recycler.RecyclerAdapter;
import com.ryan.corelibs.R;
import com.ryan.corelibs.model.entity.Repository;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GithubAdapter extends RecyclerAdapter<Repository> {

    private SimpleDateFormat format;

    public GithubAdapter(Context context) {
        super(context, R.layout.i_github);
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    }

    @Override
    protected void convert(BaseAdapterHelper helper, Repository item, int position) {
        helper.setText(R.id.tv_title, item.owner.login + "/" + item.name)
                .setText(R.id.tv_desc, item.description)
                .setText(R.id.tv_lang, item.language)
                .setText(R.id.tv_star, String.valueOf(item.stargazers_count))
                .setText(R.id.tv_update_date, "Updated at " + format.format(item.updated_at))
                .setImageUrl(R.id.iv_avatar, item.owner.avatar_url);
    }
}
