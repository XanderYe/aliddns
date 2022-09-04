package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;

import java.io.IOException;

/**
 * @author cheng
 * @date 2022年9月4日
 */
public class Ipv6Idnet extends Ipv6AddressRepo {

    @Override
    public String getIp() {
        try {
            String result = HttpUtil.doGet("http://v6.ident.me/", null);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
