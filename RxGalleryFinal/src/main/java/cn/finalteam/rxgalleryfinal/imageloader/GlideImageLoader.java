package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import cn.finalteam.rxgalleryfinal.imageloader.rotate.RotateTransformation;
import cn.finalteam.rxgalleryfinal.ui.widget.FixImageView;

/**
 * Created by pengjianbo  Dujinyang on 2016/8/13 0013.
 */
public class GlideImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Context context, String path, FixImageView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, boolean isGif, int width, int height, int rotate) {
        RequestOptions options = new RequestOptions()
                .placeholder(defaultDrawable).error(defaultDrawable).override(width, height)
                .apply(RequestOptions.bitmapTransform(new RotateTransformation(rotate)));


        if (isGif) {
            Glide
                    .with(context)
                    .load(path)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        } else {
            options = options.diskCacheStrategy(DiskCacheStrategy.NONE);

            Glide
                    .with(context)
                    .load(path)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        }
    }
}
