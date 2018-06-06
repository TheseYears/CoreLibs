import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

/**
 * Author: yangdm
 * Email:yangdm@bluemobi.cn
 * Description:(自定义的GlideModule)
 */
 public class CustomGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Do nothing.
    }
  
   /**
     * 此方法为Glide请求网络库组件的入口
     * @param context
     * @param glide
     */      
    @Override
    public void registerComponents(Context context, Glide glide) {
        /**
         * 传入已忽略证书的OkHttpClient
         */
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }


}
