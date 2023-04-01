package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.StringUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yezhendong
 * @description:
 * @date 2021/3/25 10:27
 */
public class SohuIpAddress extends Ipv4AddressRepo {
    private static final Pattern IP_PATTERN = Pattern.compile("\"cip\": \"([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\",");

    private static final List<String> getIPList = Arrays.asList("http://ipv4.ident.me","http://ipv4.icanhazip.com","http://nsupdate.info/myip","http://whatismyip.akamai.com","http://ipv4.myip.dk/api/info/IPv4Address", "http://checkip4.spdyn.de");

    @Override
    public String getIp() {
        for (String url : getIPList) {
            try {
                String result = HttpUtil.doGet(url, null);
                if (!StringUtil.isEmpty(result) && !"127.0.0.1".equals(result)){
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new SohuIpAddress().getIp());
    }
}
