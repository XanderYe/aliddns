package cn.xanderye.aliddns.util;

/**
 * @author XanderYe
 * @description:
 * @date 2020/11/21 14:36
 */
public class SystemUtil {

    /**
     * 获取环境变量
     * @param name
     * @param dft
     * @return java.lang.String
     * @author XanderYe
     * @date 2020/11/21
     */
    public static String getOrDefault(String name, String dft) {
        return StringUtil.isEmpty(System.getenv(name)) ? dft : System.getenv(name);
    }
}
