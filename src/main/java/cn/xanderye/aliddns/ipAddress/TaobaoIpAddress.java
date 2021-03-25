package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yezhendong
 * @description:
 * @date 2021/3/25 10:27
 */
public class TaobaoIpAddress extends IpAddressRepo {
    @Override
    public String getIp() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Origin", "http://ip.taobao.com");
        headers.put("Referer", "http://ip.taobao.com/");
        Map<String, Object> params = new HashMap<>();
        params.put("ip", "myip");
        params.put("accessKey", "alibaba-inc");
        try {
            String result = HttpUtil.doPost("http://ip.taobao.com/outGetIpInfo", headers, null, params);
            return StringUtil.substringBetween(result, "queryIp\":\"", "\",");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
