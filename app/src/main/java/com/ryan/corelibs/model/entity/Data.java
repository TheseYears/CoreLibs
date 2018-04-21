package com.ryan.corelibs.model.entity;

import com.corelibs.subscriber.ResponseHandler;

import java.util.List;

public class Data<T> implements ResponseHandler.IBaseData {
    public int total_count;
    public List<T> items;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public int status() {
        return 0;
    }

    @Override
    public Object data() {
        return items;
    }

    @Override
    public String msg() {
        return "";
    }
}
