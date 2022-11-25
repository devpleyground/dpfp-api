package com.innople.devpleyground.dpfpapi.common.utils;

import com.google.gson.*;
import com.google.gson.annotations.Expose;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ObjectTransfer {
    public static <T> String toJson(T obj) {
        // Gson 빌더에 ".disableHtmlEscaping()" 부분을 넣어야지 toJson으로 객체가 String으로 변할 때 특수문자가 유니코드로 변환되는 것을 방지한다.
        return new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).disableHtmlEscaping().create().toJson(obj);
    }

    public static <T1, T2> T2 toObject(T1 obj1, Class<T2> c) {
        return new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).create().fromJson(new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).disableHtmlEscaping().create().toJson(obj1), c);
    }

    public static <T> T toObject(String jsonStr, Class<T> c) {
        return new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).create().fromJson(jsonStr, c);

    }

    public static <T1, T2> T2 toObject(T1 obj1, T2 obj2, Class<T2> c) {
        final T2 existing = obj2;
        InstanceCreator<T2> creator = returnValue -> existing;
        return new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).registerTypeAdapter(c, creator).create().fromJson(new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).disableHtmlEscaping().create().toJson(obj1), c);
    }

    public static <T> T toObject(String jsonStr, T obj, Class<T> c) {
        final T existing = obj;
        InstanceCreator<T> creator = returnValue -> existing;
        return new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).registerTypeAdapter(c, creator).create().fromJson(jsonStr, c);
    }

    public static <T1, T2> T2 toObjectWithLocalDateTime(T1 obj1, Class<T2> c) {
        return new Gson().newBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        })
                .setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).create().fromJson(new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).disableHtmlEscaping().create().toJson(obj1), c);
    }

    public static <T1, T2> List<T2> toObjectListWithLocalDateTime(List<T1> obj1, Class<T2[]> c) {
        T2[] resultArray = new Gson().newBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                    throws JsonParseException {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
        })
                .setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).create().fromJson(new Gson().newBuilder().setExclusionStrategies(new ExposeAnnotationExclusionStrategy()).disableHtmlEscaping().create().toJson(obj1), c);
        return Arrays.asList(resultArray);
    }

    public static class ExposeAnnotationExclusionStrategy implements ExclusionStrategy
    {
        public boolean shouldSkipClass(Class<?> c) {
            return c.getAnnotation(Expose.class) != null;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            // TODO Auto-generated method stub
            return f.getAnnotation(Expose.class) != null;
        }
    }
}