package com.corelibs.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.List;

/**
 * SharedPreferences工具类, 可以通过传入实体对象保存其至SharedPreferences中,
 * 并通过实体的类型Class将保存的对象取出. 支持不带泛型的对象以及List集合
 * <BR/>
 * Created by Ryan on 2016/1/12.
 */
public class PreferencesHelper {

    private static final String LIST_TAG = ".LIST";
    private static SharedPreferences sharedPreferences;
    private static Gson gson;

    /**
     * 使用之前初始化, 可在Application中调用
     * @param context 请传入ApplicationContext避免内存泄漏
     */
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        gson = new Gson();
    }

    private static void checkInit() {
        if (sharedPreferences == null || gson == null) {
            throw new IllegalStateException("Please call init(context) first.");
        }
    }

    /**
     * 保存对象数据至SharedPreferences, key默认为类名, 如
     * <pre>
     * PreferencesHelper.saveData(saveUser);
     * </pre>
     * @param data 不带泛型的任意数据类型实例
     */
    public static <T> void saveData(T data) {
        saveData(data.getClass().getName(), data);
    }

    /**
     * 根据key保存对象数据至SharedPreferences, 如
     * <pre>
     * PreferencesHelper.saveData(key, saveUser);
     * </pre>
     * @param data 不带泛型的任意数据类型实例
     */
    public static <T> void saveData(String key, T data) {
        checkInit();
        if (data == null)
            throw new IllegalStateException("data should not be null.");
        sharedPreferences.edit().putString(key, gson.toJson(data)).apply();
    }

    /**
     * 保存List集合数据至SharedPreferences, 请确保List至少含有一个元素, 如
     * <pre>
     * PreferencesHelper.saveData(users);
     * </pre>
     * @param data List类型实例
     */
    public static <T> void saveData(List<T> data) {
        checkInit();
        if (data == null || data.size() <= 0)
            throw new IllegalStateException(
                    "List should not be null or at least contains one element.");
        saveData(data.get(0).getClass().getName() + LIST_TAG, data);
    }

    /**
     * 根据key将List集合数据保存至SharedPreferences, 请确保List至少含有一个元素, 如
     * <pre>
     * PreferencesHelper.saveData(users);
     * </pre>
     * @param key 键值
     * @param data List类型实例
     */
    public static <T> void saveData(String key, List<T> data) {
        checkInit();
        if (data == null || data.size() <= 0)
            throw new IllegalStateException(
                    "List should not be null or at least contains one element.");
        sharedPreferences.edit().putString(key, gson.toJson(data)).apply();
    }

    /**
     * 将数据从SharedPreferences中取出, key默认为类名, 如
     * <pre>
     * User user = PreferencesHelper.getData(key, User.class)
     * </pre>
     */
    public static <T> T getData(Class<T> clz) {
        return getData(clz.getName(), clz);
    }

    /**
     * 根据key将数据从SharedPreferences中取出, 如
     * <pre>
     * User user = PreferencesHelper.getData(User.class)
     * </pre>
     */
    public static <T> T getData(String key, Class<T> clz) {
        checkInit();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, clz);
    }

    /**
     * 将集合从SharedPreferences中取出, 如
     * <pre>List&lt;User&gt; users = PreferencesHelper.getData(List.class, User.class)</pre>
     */
    public static <T> List<T> getData(Class<List> clz, Class<T> gClz) {
        checkInit();
        return getData(gClz.getName() + LIST_TAG, clz, gClz);
    }

    /**
     * 根据key将集合从SharedPreferences中取出, 如
     * <pre>List&lt;User&gt; users = PreferencesHelper.getData(List.class, User.class)</pre>
     */
    public static <T> List<T> getData(String key, Class<List> clz, Class<T> gClz) {
        checkInit();
        String json = sharedPreferences.getString(key, "");
        return gson.fromJson(json, ParameterizedTypeImpl.get(clz, gClz));
    }

    /**
     * 简易字符串保存, 仅支持字符串
     */
    public static void saveData(String key, String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }

    /**
     * 简易字符串获取, 仅支持字符串
     */
    public static String getData(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 删除保存的对象
     */
    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * 删除保存的对象
     */
    public static void remove(Class clz) {
        remove(clz.getName());
    }

    /**
     * 删除保存的数组
     * @param clz 集合中的实际类型
     */
    public static void removeList(Class clz) {
        removeList(clz.getName() + LIST_TAG);
    }

    /**
     * 删除保存的数组
     */
    public static void removeList(String key) {
        sharedPreferences.edit().remove(key).apply();
    }
}
