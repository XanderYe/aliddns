package cn.xanderye.aliddns.util;

/**
 * @author XanderYe
 * @description:
 * @date 2020/11/21 14:31
 */
public class StringUtil {

    public static boolean isEmpty(String s) {
        return null == s || "".equals(s);
    }

    public static boolean isAnyEmpty(String...args) {
        if (args.length == 0) {
            return true;
        }
        for (String s : args) {
            if (null == s || "".equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static String substringBetween(String str, String open, String close) {
        if (str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
