package cn.xanderye.aliddns;

import cn.xanderye.aliddns.service.DDnsService;
import cn.xanderye.aliddns.util.PropertyUtil;

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
        DDnsService dDnsService = new DDnsService();
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        int period = 5;
        String s = PropertyUtil.get("schedule.period");
        if (s != null && Integer.parseInt(s) > 0) {
            period = Integer.parseInt(s);
        }
        executorService.scheduleAtFixedRate(dDnsService::ddns, 0, period, TimeUnit.MINUTES);
    }
}
