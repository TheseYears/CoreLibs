package com.corelibs.utils.rxbus;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * 基于事件RxJava的事件总线
 */
public class RxBus {

    private static final RxBus instance = new RxBus();

    //private final PublishSubject<Object> bus = PublishSubject.create();

    // If multiple threads are going to emit events to this
    // then it must be made thread-safe like this instead
    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    /** 获取默认总线 **/
    public static RxBus getDefault() {
        return instance;
    }

    /**
     * 发送一个事件
     * @param o 事件类型
     */
    public void send(Object o) {
        bus.onNext(o);
    }

    /**
     * 发送一个事件, 如果没有订阅者则不发送
     * @param o 事件类型
     */
    public void sendWithObservers(Object o) {
        if (hasObservers())
            send(o);
    }

    /**
     * 发送一个事件
     * @param o 携带的数据
     * @param tag 数据类型
     */
    public void send(Object o, String tag) {
        bus.onNext(new RxBusObject(tag, o));
    }

    /**
     * 发送一个事件, 如果没有订阅者则不发送
     * @param o 携带的数据
     * @param tag 数据类型
     */
    public void sendWithObservers(Object o, String tag) {
        if (hasObservers())
            send(o, tag);
    }

    /**
     * 获取Observable以便订阅
     */
    public Observable<Object> toObservable() {
        return bus;
    }

    /**
     * 获取Observable以便订阅
     * @param eventType 事件类型, 只接收符合事件类型的事件
     */
    public <T> Observable<T> toObservable(final Class<T> eventType) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return eventType.isInstance(o);
            }
        }).cast(eventType);
    }

    /**
     * 获取Observable以便订阅
     * @param eventType 事件数据, 只接收符合事件数据类型的事件
     * @param tag 事件类型, 只接收与事件类型相同的事件
     */
    public <T> Observable<T> toObservable(final Class<T> eventType, final String tag) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                if (!(o instanceof RxBusObject)) return false;
                RxBusObject ro = (RxBusObject) o;
                return eventType.isInstance(ro.getObj()) && tag != null
                        && tag.equals(ro.getTag());
            }
        }).map(new Func1<Object, T>() {
            @Override
            public T call(Object o) {
                RxBusObject ro = (RxBusObject) o;
                return (T) ro.getObj();
            }
        });
    }

    /**
     * 是否拥有订阅者
     */
    public boolean hasObservers() {
        return bus.hasObservers();
    }

    /**
     * 事件数据, 携带一个Object的数据, 以及String的标志.
     */
    public static class RxBusObject {
        private String tag;
        private Object obj;

        public RxBusObject(String tag, Object obj) {
            this.tag = tag;
            this.obj = obj;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public static RxBusObject newInstance(String tag, Object obj) {
            return new RxBusObject(tag, obj);
        }
    }
}
