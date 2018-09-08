package org.messtin.jhttp.container;

import org.messtin.jhttp.servlet.AbstractHttpFilter;
import org.messtin.jhttp.util.ServletUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The container of {@link AbstractHttpFilter}
 *
 * @author majinliang
 */
public final class FilterContainer {

    /**
     * a map from:
     * path{@link String} -> filter{@link AbstractHttpFilter}
     */
    private static Map<String, AbstractHttpFilter> filterMap;

    /**
     * Lazy init when have httpFilter
     * new httpFilter will replace old httpFilter if have same path
     *
     * @param path the path of filter
     * @param httpFilter filter
     */
    public static void put(String path, AbstractHttpFilter httpFilter) {
        if (filterMap == null) {
            filterMap = new HashMap<>();
        }
        filterMap.put(path, httpFilter);
    }

    public static List<AbstractHttpFilter> get(String path, boolean antMatch) {
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
