package cn.xanderye.aliddns;

import cn.xanderye.aliddns.ipAddress.IpAddressRepo;
import cn.xanderye.aliddns.ipAddress.Ipv6Address;
import cn.xanderye.aliddns.ipAddress.Ipv6Idnet;
import cn.xanderye.aliddns.ipAddress.SohuIpAddress;
import cn.xanderye.aliddns.model.IpType;
import cn.xanderye.aliddns.service.DDnsService;
import cn.xanderye.aliddns.util.PropertyUtil;
import cn.xanderye.aliddns.util.SystemUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2020/7/9.
 *
 * @author XanderYe
 */
public class Main {
    public static void main(String[] args) {
        String type = SystemUtil.getOrDefault("TYPE", PropertyUtil.get("aliyun.type"));
        DDnsService dDnsService = new DDnsService();
        IpAddressRepo ipAddress = IpType.IPV4.getType().equals(type) ? new SohuIpAddress() : new Ipv6Idnet();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        int period = 5;
        String s = SystemUtil.getOrDefault("PERIOD", PropertyUtil.get("schedule.period"));
        if (s != null && Integer.parseInt(s) > 0) {
            period = Integer.parseInt(s);
        }
        executorService.scheduleAtFixedRate(() -> dDnsService.ddns(ipAddress), 0, period, TimeUnit.MINUTES);
    }
}
