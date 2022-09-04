package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.StringUtil;
import com.alibaba.fastjson2.JSONObject;

import java.io.IOException;

/**
 * @author cheng
 * @date 2022年9月4日
 */
public class Ipv6Address extends Ipv6AddressRepo{
    @Override
    public String getIp() {
        try {
            String result = HttpUtil.doGet("https://ipv6.ipaddress.com/", null);
            if (StringUtil.isEmpty(result)){
                return result;
            }
            return JSONObject.parseObject(result).getString("ipv6");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
