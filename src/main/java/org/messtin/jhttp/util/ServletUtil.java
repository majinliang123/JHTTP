package org.messtin.jhttp.util;

import java.util.regex.Pattern;

public final class ServletUtil {

    public static boolean isAntMatch(String origal, String target) {
        return Pattern.matches(target, origal);
    }
}
