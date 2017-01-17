package com.corelibs.utils.uploader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.corelibs.utils.ParameterizedTypeImpl;
import com.corelibs.utils.ToastMgr;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * 通过{@link ImageUploader} 与 {@link ImageUploadRequest} 来上传图片.
 * Created by Ryan on 2016/1/20.
 */
public class ImageUploadHelper<T> {

    public static final String IMAGE_CACHE_FOLDER = "ImageUploaderCache";
    public static final String IMAGE_BASE_NAME = "tmp";
    public static final String IMAGE_BASE_TYPE = ".jpeg";
    public static final int IMAGE_MAX_SIZE = 720;

    private ImageUploader uploader;
    private Gson gson;

    public ImageUploadHelper() {
        uploader = new ImageUploader();
        gson = new Gson();
    }

    private void doPost(String url, Map<String, String> params, Map<String, File> files,
                        String fileKey, ImageUploader.OnResponseListener listener) {
        uploader.post(url, params, files, fileKey, listener);
    }

    public void doPost(ImageUploadRequest request) {
        doPost(request.getUrl(), request.getParams(), request.getFiles(),
                request.getFileKey(), request.getListener());
    }

    public Observable<T> doPostWithObservable(final ImageUploadRequest request) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                uploader.post(request.getUrl(), request.getParams(), request.getFiles(),
                        request.getFileKey(), new ImageUploader.OnResponseListener() {
                    @Override @SuppressWarnings("unchecked")
                    public void onResponse(String data) {
                        if (request.getInnerClasses() == null || request.getInnerClasses().length <= 0)
                            subscriber.onNext((T) gson.fromJson(data, request.getOutputClass()));
                        else
                            subscriber.onNext((T) gson.fromJson(data, ParameterizedTypeImpl
                                    .get(request.getOutputClass(), request.getInnerClasses())));

                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Exception e) {
                        subscriber.onError(e);
                    }
                });
            }
        });
    }

    public static File bitmapToFile(Context context, Bitmap bmp) throws IOException {
        String imageCachePath = context.getCacheDir() + IMAGE_CACHE_FOLDER;
        String filename = IMAGE_BASE_NAME + System.currentTimeMillis() + IMAGE_BASE_TYPE;
        File file = new File(imageCachePath + filename);
        if (!file.exists()) {
            File dir = new File(imageCachePath);
            if (!dir.exists()) {
                if (!dir.mkdirs()) ToastMgr.show("创建图片缓存文件夹失败");
            }
            if (!file.createNewFile()) ToastMgr.show("创建图片文件失败");
        }
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(imageCachePath + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boolean result = bmp.compress(format, quality, stream);

        if (result) {
            bmp.recycle();
            return file;
        }

        return null;
    }

    public static Bitmap compressBitmap(Bitmap bitmap, int maxSize) {
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleW;
        float scaleH;
        if (mWidth <= mHeight) {
            scaleH = maxSize / (float) mHeight;
            scaleW = scaleH;
        } else {
            scaleW = maxSize / (float) mWidth;
            scaleH = scaleW;
        }
        matrix.postScale(scaleW, scaleH);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);

        if(newBitmap != bitmap)
            bitmap.recycle();
        return newBitmap;
    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        return compressBitmap(bitmap, IMAGE_MAX_SIZE);
    }

    public static File compressFile(Context context, String filePath) {
        try {
            return bitmapToFile(context,
                    compressBitmap(BitmapFactory.decodeFile(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File compressFile(Context context, String filePath, int maxSize) {
        try {
            return bitmapToFile(context,
                    compressBitmap(BitmapFactory.decodeFile(filePath), maxSize));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void clearCache(final Context context) {
        new Thread(new Runnable() {
            @Override public void run() {
                String imageCachePath = context.getCacheDir() + IMAGE_CACHE_FOLDER;
                File dir = new File(imageCachePath);
                String[] tmp = dir.list();
                for (String path : tmp) {
                    File file;
                    if (path.endsWith(File.separator)) file = new File(imageCachePath + path);
                    else  file = new File(imageCachePath + File.separator + path);

                    if (!file.delete()) ToastMgr.show("删除图片缓存失败");
                }
            }
        }).start();
    }

}
