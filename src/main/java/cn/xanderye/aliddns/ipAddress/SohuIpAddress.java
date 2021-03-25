package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yezhendong
 * @description:
 * @date 2021/3/25 10:27
 */
public class SohuIpAddress extends IpAddressRepo {
    private static final Pattern IP_PATTERN = Pattern.compile("\"cip\": \"([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\",");

    @Override
    public String getIp() {
        try {
            String result = HttpUtil.doGet("http://pv.sohu.com/cityjson?ie=utf-8", null);
            Matcher matcher = IP_PATTERN.matcher(result);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new SohuIpAddress().getIp());
    }
}
