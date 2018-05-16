# CoreLibs

这是一个MVP-Based, 集成了很多常用库, 工具以及控件的库集合, 旨在提高开发效率并保证一定的代码质量.

## 关于工程

* 请使用Android Studio 3.0以上版本编译, Gradle版本3.0.1, buildToolsVersion 26.0.2, 请根据需要自行选择.
* app模块中包含MVP的使用示例-调用GitHub的搜索接口（关键词CoreLibs）并用RecyclerView显示出来（带有分页）.
* app模块中包含一些项目基础代码, 以及一些个人常用的工具和控件, 这些工具和控件不完全具备通用性, 因此没法放在CoreLibs下.

## 截图

![screen shot](https://raw.githubusercontent.com/TheseYears/CoreLibs/master/screen.jpg)

## 引入方法

* 下载CoreLibs
* 新建一个Android工程
* 将CoreLibs文件夹下的CoreLibs作为一个Module导入该项目
* 主模块添加CoreLibs的依赖
* 主模块添加RxGalleryFinal的依赖
* 主模块添加butterknife的依赖
* 在最外层的build.gradle文件中添加maven库:

````
....
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}

````
* 主模块添加如下代码以开启lambda语法:
````
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
````

* 新建一个Application类并在onCreate中加入如下代码:

````
GlobalExceptionHandler.getInstance().init(this, getResources().getString(R.string.app_name)); //初始化全局异常捕获
ToastMgr.init(getApplicationContext()); //初始化Toast管理器
Configuration.enableLoggingNetworkParams(); //打开网络请求Log打印，需要在初始化Retrofit接口工厂之前调用
ApiFactory.getFactory().add(Urls.ROOT_API); //初始化Retrofit接口工厂
PreferencesHelper.init(getApplicationContext()); //初始化SharedPreferences工具类

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    // android 7.0系统解决拍照报exposed beyond app through ClipData.Item.getUri()
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());
    builder.detectFileUriExposure();
}
````
* 根据服务器接口返回JSON的结构, 新建一个实体类, 并实现IBaseData.
* 根据需要项目的主题可以使用CoreLibs中的AppBaseCompactTheme.
* 如果在Application中初始化了GlobalExceptionHandler还需要在Manifest中加入如下声明:

````
<activity
    android:name="com.corelibs.exception.ExceptionDialogActivity"
    android:screenOrientation="portrait"
    android:theme="@style/ExceptionDialogStyle" />

<activity
    android:name="com.corelibs.exception.ExceptionDetailActivity"
    android:screenOrientation="portrait" />
````

## 使用方法

请参考 https://www.gitbook.com/book/theseyears/android-architecture-journey

## Change Log

* 2016-02 完善雏形
* 2016-04 完善 [文档](https://www.gitbook.com/book/theseyears/android-architecture-journey)
* 2016-09-07 添加Studio插件 [MvpClassesGenerator](https://github.com/TheseYears/MvpClassesGenerator)
* 2016-09-22 
    * 扩展Adapter相关工具类 <BR/>
        * 添加多布局支持
        * 添加RecyclerView支持
        * 添加RecyclerView的多布局支持
    * 添加RecyclerView的Header, Footer支持
    * 扩展PTR组件 <BR/>
        * 添加对RecyclerView的支持
* 2016-12 添加对透明状态栏的支持
* 2017-01 
    * BaseRecyclerAdapter默认直接支持添加header与footer <BR/>
    * 重构分页方式
    * 需要使用新的 [MvpClassesGenerator](https://github.com/TheseYears/MvpClassesGenerator/blob/master/MvpClassesGenerator.jar) 插件
* 2018-04 
    * 修改项目目录为可执行的application，包含CoreLibs模块，而非一个单独的android module
    * 从RxJava1升级至RxJava2
    * 各个库均升级至较新版本
        * glide -> 4.6.1
        * butterknife -> 8.8.1
        * rxjava -> 2.1.12
        * rxandroid -> 2.0.1
        * rxlifecycle -> 2.2.1
        * retrofit -> 2.4.0
        * material-dialogs -> 0.9.6.0
        * RxGalleryFinal -> 1.1.3
    * 引入lambda语法
    * 升级GalleryFinal至RxGalleryFinal，但由于有一点不太满意的地方，因此fork到本地做了些修改
        * 微调ToolBar样式，微调列表样式
        * 修改某些文件夹有图片但显示不出来的限制
        * 升级UCrop至2.2.2，微调UCrop ToolBar样式
        * 修复MediaActivity可能会内存泄漏的问题
