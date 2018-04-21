package com.corelibs.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestBodyCreator {

    public static final String TYPE_DEFAULT = "multipart/form-data";
    public static final String MULTIPART_HACK = "\"; filename=\"image.png";

    public static RequestBody create(File file) {
        return RequestBody.create(MediaType.parse(TYPE_DEFAULT), file);
    }

    public static List<RequestBody> create(List<File> files) {
        List<RequestBody> bodies = new ArrayList<>();
        for (File file : files) {
            bodies.add(create(file));
        }
        return bodies;
    }

    public static RequestBody create(String string) {
        if (string == null) string = "";
        return RequestBody.create(MediaType.parse(TYPE_DEFAULT), string);
    }
}
