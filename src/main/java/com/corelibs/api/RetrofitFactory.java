package com.corelibs.api;

import android.text.TextUtils;

import com.corelibs.common.Configuration;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用于获取配置好的retrofit对象, 通过设置{@link Configuration#enableLoggingNetworkParams()}来启用网络请求
 * 参数与相应结果.
 * <br/>
 * Created by Ryan on 2015/12/30.
 */
public class RetrofitFactory {

    private static Retrofit retrofit;
    private static String baseUrl;

    public static void init(String url) {
        baseUrl = url;
    }

    /**
     * 获取配置好的retrofit对象来生产Manager对象
     */
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            if (baseUrl == null || baseUrl.length() <= 0)
                throw new IllegalStateException("请在调用getFactory之前先调用setBaseUrl");

            Retrofit.Builder builder = new Retrofit.Builder();
            GsonBuilder gsonBuilder = new GsonBuilder();

            // Gson double类型转换, 避免空字符串解析出错
            final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
                @Override public Number read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    if (in.peek() == JsonToken.STRING) {
                        String tmp = in.nextString();
                        if (TextUtils.isEmpty(tmp)) tmp = "0";
                        return Double.parseDouble(tmp);
                    }
                    return in.nextDouble();
                }

                @Override public void write(JsonWriter out, Number value) throws IOException {
                    out.value(value);
                }
            };

            // Gson long类型转换, 避免空字符串解析出错
            final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
                @Override public Number read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    if (in.peek() == JsonToken.STRING) {
                        String tmp = in.nextString();
                        if (TextUtils.isEmpty(tmp)) tmp = "0";
                        return Long.parseLong(tmp);
                    }
                    return in.nextLong();
                }

                @Override public void write(JsonWriter out, Number value) throws IOException {
                    out.value(value);
                }
            };

            // Gson int类型转换, 避免空字符串解析出错
            final TypeAdapter<Number> INT = new TypeAdapter<Number>() {
                @Override public Number read(JsonReader in) throws IOException {
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull();
                        return null;
                    }
                    if (in.peek() == JsonToken.STRING) {
                        String tmp = in.nextString();
                        if (TextUtils.isEmpty(tmp)) tmp = "0";
                        return Integer.parseInt(tmp);
                    }
                    return in.nextInt();
                }

                @Override public void write(JsonWriter out, Number value) throws IOException {
                    out.value(value);
                }
            };

            gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, DOUBLE));
            gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, LONG));
            gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, INT));

            builder.baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            if (Configuration.isShowNetworkParams()) {
                clientBuilder.addInterceptor(new HttpLoggingInterceptor());
            }

            builder.client(clientBuilder.build());

            retrofit = builder.build();
        }

        return retrofit;
    }
}
