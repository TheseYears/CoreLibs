使用说明:

dependencies {
 注释掉coreLibs里build.gradle里的此包
//    compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar'
    }
    
    
 在自己的项目工程AndroidManifest.xml的<application>里  配置
        <meta-data
            android:name=".....glide.CustomGlideModule" 
            android:value="GlideModule"/>   
    
