package cn.finalteam.rxgalleryfinal.imageloader.rotate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 * Created by pengjianbo  Dujinyang on 2016/8/16 0016.
 */
public class RotateTransformation extends BitmapTransformation {

    private String id = "rotate";
    private float rotateRotationAngle = 0f;

    public RotateTransformation(float rotateRotationAngle) {
        super();

        this.rotateRotationAngle = rotateRotationAngle;
        id += rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateRotationAngle);
        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(id.getBytes(Charset.forName("UTF-8")));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RotateTransformation;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
