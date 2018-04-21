package com.corelibs.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type[] typeArguments;

    public static ParameterizedTypeImpl get(Type rawType, Type... typeArguments) {
        return new ParameterizedTypeImpl(rawType, typeArguments);
    }

    private ParameterizedTypeImpl(Type rawType, Type... typeArguments) {
        this.rawType = rawType;
        this.typeArguments = canonicalize(typeArguments.clone());
    }

    private Type[] canonicalize(Type[] typeArguments) {
        if (typeArguments != null && typeArguments.length > 1) {
            Type[] types = new Type[typeArguments.length - 1];
            for (int i = 0; i < typeArguments.length; i++) {
                if (i > 0) {
                    types[i - 1] = typeArguments[i];
                }
            }

            return new Type[] { new ParameterizedTypeImpl(typeArguments[0], types) };
        }

        return typeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return typeArguments.clone();
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    public String typeToString(Type type) {
        return type instanceof Class ? ((Class<?>) type).getName() : type.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(30 * (typeArguments.length + 1));
        stringBuilder.append(typeToString(rawType));

        if (typeArguments.length == 0) {
            return stringBuilder.toString();
        }

        stringBuilder.append("<").append(typeToString(typeArguments[0]));
        for (int i = 1; i < typeArguments.length; i++) {
            stringBuilder.append(", ").append(typeToString(typeArguments[i]));
        }
        return stringBuilder.append(">").toString();
    }
}
