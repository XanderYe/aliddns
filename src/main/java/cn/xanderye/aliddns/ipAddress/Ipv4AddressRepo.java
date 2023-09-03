package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.PropertyUtil;
import cn.xanderye.aliddns.util.StringUtil;
import cn.xanderye.aliddns.util.SystemUtil;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cheng
 * @date 2022年9月4日
 */
@Slf4j
public class Ipv4AddressRepo extends IpAddressRepo {

    private List<DescribeDomainRecordsResponse.Record> cacheRecordList = new ArrayList<>();
    private List<String> rrList = new ArrayList<>();
    private static final List<String> getIPList = Arrays.asList("http://ipv4.ident.me",
            "http://ipv4.icanhazip.com",
            "http://nsupdate.info/myip",
            "http://whatismyip.akamai.com",
            "http://ipv4.myip.dk/api/info/IPv4Address",
            "http://checkip4.spdyn.de");

    @Override
    public List<DescribeDomainRecordsResponse.Record> getCacheRecordList() {
        return cacheRecordList;
    }

    @Override
    public void setCacheRecordList(List<DescribeDomainRecordsResponse.Record> list) {
        this.cacheRecordList = list;
    }

    @Override
    public void setRrList() {
        String v4Rrs = SystemUtil.getOrDefault("IPV4_RR", PropertyUtil.get("ipv4.rr"));
        if (!StringUtil.isEmpty(v4Rrs)){
            this.rrList = Arrays.asList(v4Rrs.split(","));
        }
    }

    @Override
    public List<String> getRrList() {
        return this.rrList;
    }

    @Override
    String getType() {
        return "A";
    }

    @Override
    public String getIp() {
        for (String url : getIPList) {
            try {
                String result = HttpUtil.doGet(url, null);
                if (!StringUtil.isEmpty(result) && !"127.0.0.1".equals(result)){
                    log.info("ipv4地址为：{}",result);
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Ipv4AddressRepo().getIp());
    }
}
