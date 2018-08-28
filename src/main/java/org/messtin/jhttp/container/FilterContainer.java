package org.messtin.jhttp.container;

import org.messtin.jhttp.servlet.HttpFilter;
import org.messtin.jhttp.util.ServletUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class FilterContainer {

    private static Map<String, HttpFilter> filterMap;

    // lazy init when have httpFilter
    // new httpFilter will replace old httpFilter if have same path
    public static void put(String path, HttpFilter httpFilter) {
        if (filterMap == null) {
            filterMap = new HashMap<>();
        }
        filterMap.put(path, httpFilter);
    }

    public static List<HttpFilter> get(String path, boolean antMatch) {
        if (filterMap == null) {
            return null;
        }

        if (antMatch) {
            return filterMap.keySet()
                    .stream()
                    .filter(filterRegex -> ServletUtil.isAntMatch(path, filterRegex))
                    .map(filterRegex -> filterMap.get(filterRegex))
                    .collect(Collectors.toList());

        } else {
            return filterMap.keySet()
                    .stream()
                    .filter(filterPath -> filterPath.equals(path))
                    .map(filterPath -> filterMap.get(filterPath))
                    .collect(Collectors.toList());
        }

    }
}
