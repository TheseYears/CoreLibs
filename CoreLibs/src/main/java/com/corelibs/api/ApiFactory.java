package com.corelibs.api;

import android.text.TextUtils;

import com.corelibs.common.Configuration;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 用于retrofit创建网络请求接口的Api实现，通过设置{@link Configuration#enableLoggingNetworkParams()}来启用网络请求
 * 参数与相应结果.
 * <br/>
 * 通过{@link #add(String)}或{@link #add(String, String)}来添加不同的BaseUrl，并通过create方法创建Api实现。
 * <br/><br/>
 * <pre>
 * ApiFactory.getFactory().add(baseUrl);
 * ApiFactory.getFactory().create(ProductApi.class);
 *
 * ApiFactory.getFactory().add("dev", baseUrl);
 * ApiFactory.getFactory().create("dev", ProductApi.class);
 * ApiFactory.getFactory().create(1, ProductApi.class);
 * </pre>
 * Created by Ryan on 2015/12/30.
 */
public class ApiFactory {

    private static ApiFactory factory;
    private HashMap<String, Retrofit> retrofitMap = new HashMap<>();
    private static final boolean IS_ADAPT_GSON_OBJECT_STRING_EXCEPTION = false;

    private int timeout = 30;

    public static ApiFactory getFactory() {
        if (factory == null) {
            synchronized (ApiFactory.class) {
                if (factory == null)
                    factory = new ApiFactory();
            }
        }

        return factory;
    }

    /**
     * 新增一个retrofit实例，可以通过{@link #create(Class)}或{@link #create(int, Class)}方法获取Api实现。<br/>
     * key默认自增长。
     */
    public void add(String baseUrl) {
        retrofitMap.put(retrofitMap.size() + "", createRetrofit(baseUrl));
    }

    /**
     * 新增一个retrofit实例，可以通过{@link #create(String, Class)}方法获取Api实现。<br/>
     */
    public void add(String key, String baseUrl) {
        retrofitMap.put(key, createRetrofit(baseUrl));
    }

    /**
     * 获取Api实现，默认通第一个retrofit实例创建。
     */
    public <T> T create(Class<T> clz) {
        checkRetrofitMap();
        return retrofitMap.get("0").create(clz);
    }

    /**
     * 根据key值获取Api实现
     */
    public <T> T create(int key, Class<T> clz) {
        checkRetrofitMap();
        return retrofitMap.get(key + "").create(clz);
    }

    /**
     * 根据key值获取Api实现
     */
    public <T> T create(String key, Class<T> clz) {
        checkRetrofitMap();
        return retrofitMap.get(key).create(clz);
    }

    /**
     * 设置OkHttp连接超时时间，默认30秒，请在{@link #add(String)}或{@link #add(String, String)}之前设置
     * @param seconds 秒
     */
    public void setTimeout(int seconds) {
        timeout = seconds;
    }

    private void checkRetrofitMap() {
        if (retrofitMap.size() <= 0)
            throw new IllegalStateException("Please add a Retrofit instance");
    }

    private Retrofit createRetrofit(String baseUrl) {

        if (baseUrl == null || baseUrl.length() <= 0)
            throw new IllegalStateException("BaseUrl cannot be null");

        Retrofit.Builder builder = new Retrofit.Builder();
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Gson double类型转换, 避免空字符串解析出错
        final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
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

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };

        // Gson long类型转换, 避免空字符串解析出错
        final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
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

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };

        // Gson int类型转换, 避免空字符串解析出错
        final TypeAdapter<Number> INT = new TypeAdapter<Number>() {
            @Override
            public Number read(JsonReader in) throws IOException {
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

            @Override
            public void write(JsonWriter out, Number value) throws IOException {
                out.value(value);
            }
        };

        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, DOUBLE));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, LONG));
        gsonBuilder.registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, INT));

        gsonBuilder.registerTypeAdapter(Timestamp.class, new JsonDeserializer<Timestamp>() {
            @Override
            public Timestamp deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
                return new Timestamp(json.getAsJsonPrimitive().getAsLong());
            }
        });

        // 避免当数据类型为OBJECT时，接口返回""会报错的情况。默认关闭，还需完善。
        if (IS_ADAPT_GSON_OBJECT_STRING_EXCEPTION) {
            ConstructorConstructor constructor =
                    new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
            gsonBuilder.registerTypeAdapterFactory(new ObjectTypeAdapterFactory(new ReflectiveTypeAdapterFactory(
                    constructor, FieldNamingPolicy.IDENTITY, Excluder.DEFAULT,
                    new JsonAdapterAnnotationTypeAdapterFactory(constructor))));
        }

        builder.baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).connectTimeout(timeout, TimeUnit.SECONDS);

        if (Configuration.isShowNetworkParams()) {
            clientBuilder.addInterceptor(new HttpLoggingInterceptor());
        }

        builder.client(clientBuilder.build());

        return builder.build();
    }

    /**
     * ReflectiveTypeAdapterFactory代理，加入自己的解析逻辑
     */
    private final class ObjectTypeAdapterFactory implements TypeAdapterFactory {
        private final ReflectiveTypeAdapterFactory delegate;

        ObjectTypeAdapterFactory(ReflectiveTypeAdapterFactory delegate) {
            this.delegate = delegate;
        }

        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            // 还需要排除更多的类型..
            if (String.class.isAssignableFrom(typeToken.getRawType()) ||
                    StringBuilder.class.isAssignableFrom(typeToken.getRawType()) ||
                    StringBuffer.class.isAssignableFrom(typeToken.getRawType()) ||
                    !Object.class.isAssignableFrom(typeToken.getRawType())) {
                return null;
            }
            return new Adapter<>(delegate.create(gson, typeToken));
        }

        private final class Adapter<E> extends TypeAdapter<E> {
            private final TypeAdapter<E> delegate;

            Adapter(TypeAdapter<E> delegate) {
                this.delegate = delegate;
            }

            @Override
            public E read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.STRING) {
                    in.nextString();
                    return null;
                }
                return delegate.read(in);
            }

            @Override
            public void write(JsonWriter out, E value) throws IOException {
                delegate.write(out, value);
            }
        }
    }

}
