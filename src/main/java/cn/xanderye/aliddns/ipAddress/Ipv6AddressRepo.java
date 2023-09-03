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
public class Ipv6AddressRepo extends IpAddressRepo {

    private List<DescribeDomainRecordsResponse.Record> cacheRecordList = new ArrayList<>();

    private List<String> rrList = new ArrayList<>();

    @Override
    public List<DescribeDomainRecordsResponse.Record> getCacheRecordList() {
        return this.cacheRecordList;
    }

    @Override
    public void setCacheRecordList(List<DescribeDomainRecordsResponse.Record> list) {
        this.cacheRecordList = list;
    }

    @Override
    public void setRrList() {
        String v6Rrs = SystemUtil.getOrDefault("IPV6_RR", PropertyUtil.get("ipv6.rr"));
        if (!StringUtil.isEmpty(v6Rrs)){
            this.rrList = Arrays.asList(v6Rrs.split(","));
        }
    }

    @Override
    public List<String> getRrList() {
        return this.rrList;
    }

    @Override
    public String getType() {
        return "AAAA";
    }

    @Override
    public String getIp() {
        try {
            String result = HttpUtil.doGet("http://v6.ident.me/", null);
            log.info("ipv6地址为：{}",result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
