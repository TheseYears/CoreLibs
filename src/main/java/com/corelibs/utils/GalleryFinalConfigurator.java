package com.corelibs.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.corelibs.R;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.widget.GFImageView;

public class GalleryFinalConfigurator {

    public static void config(Context context) {
        ThemeConfig theme = ThemeConfig.CYAN;

        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();

        CoreConfig coreConfig = new CoreConfig.Builder(context, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .setEditPhotoCacheFolder(new File(context.getCacheDir().getPath()
                        + "GalleryFinal/temp"))
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();

        GalleryFinal.init(coreConfig);
    }

    static class GlideImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, final GFImageView imageView,
                                 Drawable defaultDrawable, int width, int height) {
            Glide.with(activity)
                    .load("file://" + path)
                    .placeholder(defaultDrawable)
                    .error(defaultDrawable)
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                    .skipMemoryCache(true)
                    .into(new ImageViewTarget<GlideDrawable>(imageView) {
                        @Override
                        protected void setResource(GlideDrawable resource) {
                            imageView.setImageDrawable(resource);
                        }

                        @Override
                        public void setRequest(Request request) {
                            imageView.setTag(R.id.adapter_item_tag_key, request);
                        }

                        @Override
                        public Request getRequest() {
                            return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                        }
                    });
        }

        @Override
        public void clearMemoryCache() {}
    }

    static class GlidePauseOnScrollListener extends PauseOnScrollListener {

        public GlidePauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
            super(pauseOnScroll, pauseOnFling);
        }

        @Override
        public void resume() {
            Glide.with(getActivity()).resumeRequests();
        }

        @Override
        public void pause() {
            Glide.with(getActivity()).pauseRequests();
        }
    }
}
