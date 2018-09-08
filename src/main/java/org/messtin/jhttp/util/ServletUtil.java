package org.messtin.jhttp.util;

import java.util.regex.Pattern;

/**
 * The Util about Servlet
 *
 * @author majinliang
 */
public final class ServletUtil {

    public static boolean isAntMatch(String origal, String target) {
        return Pattern.matches(target, origal);
    }
}
