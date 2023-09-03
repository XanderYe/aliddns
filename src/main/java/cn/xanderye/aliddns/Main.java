package cn.xanderye.aliddns;

import cn.xanderye.aliddns.ipAddress.Ipv4AddressRepo;
import cn.xanderye.aliddns.ipAddress.Ipv6AddressRepo;
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
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        int period = 5;
        String s = SystemUtil.getOrDefault("PERIOD", PropertyUtil.get("schedule.period"));
        if (s != null && Integer.parseInt(s) > 0) {
            period = Integer.parseInt(s);
        }
        executorService.scheduleAtFixedRate(() -> dDnsService.ddns(new Ipv4AddressRepo()), 0, period, TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(() -> dDnsService.ddns(new Ipv6AddressRepo()), 0, period, TimeUnit.MINUTES);
    }
}
