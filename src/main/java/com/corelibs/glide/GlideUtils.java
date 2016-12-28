import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Author: yangdm
 * Email:yangdm@bluemobi.cn
 * Description:(Glide工具类)
 */
public class GlideUtils {

    private static GlideUtils instance;
    public static GlideUtils getInstance(){
        if(instance==null){
            synchronized (GlideUtils.class) {
                if(instance==null){
                    instance=new GlideUtils();
                }
            }
        }
        return instance;
    }
    //默认加载
    public  void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    //加载指定大小
    public  void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).centerCrop().into(mImageView);
    }

    //加载指定大小和圆角
    public  void loadImageViewSizeOrCircleHorn(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).override(width, height).centerCrop().bitmapTransform(new RoundedCornersTransformation(mContext,30,0, RoundedCornersTransformation.CornerType.ALL)).into(mImageView);
    }
    //圆形处理
    public void loadImageViewCircleShape(Context mContext, String path,ImageView mImageView){
        Glide.with(mContext).load(path).centerCrop().bitmapTransform(new CropCircleTransformation(mContext)).into(mImageView);
    }

    //圆角处理
    public void loadImageViewCircleHorn(Context mContext, String path,ImageView mImageView){

        Glide.with(mContext).load(path).centerCrop().bitmapTransform(new RoundedCornersTransformation(mContext,10,0, RoundedCornersTransformation.CornerType.ALL)).into(mImageView);
    }
    //灰度处理
    public void loadImageViewGrayscale(Context mContext, String path,ImageView mImageView){

        Glide.with(mContext).load(path).centerCrop().bitmapTransform(new GrayscaleTransformation(mContext)).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public  void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).centerCrop().placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public  void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).centerCrop().override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().skipMemoryCache(true).into(mImageView);
    }

    //设置下载优先级
    public  void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().priority(Priority.NORMAL).into(mImageView);
    }


    //设置缓存策略
    public  void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * 几个常用的动画：比如crossFade()
     */

    //设置加载动画
    public  void loadImageViewAnim(Context mContext, String path, int anim, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().animate(anim).into(mImageView);
    }

    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public  void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public  void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

//    //设置动态GIF加载方式
//    public  void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
//        Glide.with(mContext).load(path).asGif().into(mImageView);
//    }

//    //设置静态GIF加载方式
//    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
//        Glide.with(mContext).load(path).asBitmap().into(mImageView);
//    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘

    //设置监听请求接口
    public  void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排

    //设置要加载的内容
    public  void loadImageViewContent(Context mContext, String path, SimpleTarget<GlideDrawable> simpleTarget) {
        Glide.with(mContext).load(path).centerCrop().into(simpleTarget);
    }

    //清理磁盘缓存
    public  void GuideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public  void GuideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }

    //高斯模糊
    public void loadImageViewBlurred(Context mContext, String path,ImageView mImageView){

        Glide.with(mContext).load(path).bitmapTransform(new BlurTransformation(mContext)).into(mImageView);
    }
