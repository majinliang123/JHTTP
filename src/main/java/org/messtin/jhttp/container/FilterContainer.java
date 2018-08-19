package org.messtin.jhttp.container;

import org.messtin.jhttp.entity.JFilter;

import java.util.HashMap;
import java.util.Map;

public final class FilterContainer {

    private static Map<String, JFilter> filterMap;

    // lazy init when have filter
    // new filter will replace old filter if have same path
    public static void put(String path, JFilter jFilter) {
        if (filterMap == null) {
            filterMap = new HashMap<>();
        }
        filterMap.put(path, jFilter);
    }

    public static JFilter get(String path) {
        if (filterMap == null) {
            return null;
        }
        return filterMap.get(path);
    }
}
