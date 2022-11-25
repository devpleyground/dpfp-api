package com.innople.devpleyground.dpfpapi.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringUtil {
    public static void encryptJsonElementByKey(String key, JsonElement jsonElement, List<String> tempList) {
        try{
            if (jsonElement.isJsonArray()) {
                for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                    encryptJsonElementByKey(key, jsonElement1, tempList);
                }
            } else {
                if (jsonElement.isJsonObject()) {
                    Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
                            .getAsJsonObject().entrySet();
                    for (Map.Entry<String, JsonElement> entry : entrySet) {
                        String currentKey = entry.getKey();
                        if (currentKey.equals(key)) {
                            String toChange= entry.getValue().toString().replace("\"", "");
                            entry.setValue(new Gson().fromJson(CryptoUtil.getMD5HashString(toChange), JsonElement.class));
                            tempList.add(entry.getValue().toString());
                        }
                        encryptJsonElementByKey(key, entry.getValue(), tempList);
                    }
                } else {
                    if (jsonElement.toString().equals(key)) {
                        tempList.add(jsonElement.toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("#====== Error While log paramter encryption ======#");
            System.out.println(e.getMessage());
        }
    }
}
