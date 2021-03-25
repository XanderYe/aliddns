package cn.xanderye.aliddns.service;

import cn.xanderye.aliddns.ipAddress.IpAddressRepo;
import cn.xanderye.aliddns.ipAddress.TaobaoIpAddress;
import cn.xanderye.aliddns.util.HttpUtil;
import cn.xanderye.aliddns.util.PropertyUtil;
import cn.xanderye.aliddns.util.StringUtil;
import cn.xanderye.aliddns.util.SystemUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 2020/7/9.
 *
 * @author XanderYe
 */
@Slf4j
public class DDnsService {

    private static String regionId;
    private static String accessKeyId;
    private static String accessSecret;
    private static List<String> rrList;
    private static String domainName;

    private static IAcsClient client;

    static {
        PropertyUtil.init();
        regionId = SystemUtil.getOrDefault("REGIN_ID", PropertyUtil.get("aliyun.region-id"));
        accessKeyId = SystemUtil.getOrDefault("ACCESS_KEY_ID", PropertyUtil.get("aliyun.access-key-id"));
        accessSecret = SystemUtil.getOrDefault("ACCESS_SECRET", PropertyUtil.get("aliyun.access-secret"));
        String rrs = SystemUtil.getOrDefault("RR", PropertyUtil.get("aliyun.rr"));
        domainName = SystemUtil.getOrDefault("DOMAIN_NAME", PropertyUtil.get("aliyun.domainName"));
        if (StringUtil.isAnyEmpty(regionId, accessKeyId, accessSecret, rrs, domainName)) {
            log.error("请先配置参数");
            System.exit(-1);
        }
        rrList = Arrays.asList(rrs.split(","));
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        client = new DefaultAcsClient(profile);
    }

    private static List<DescribeDomainRecordsResponse.Record> cacheRecordList = new ArrayList<>();

    public void ddns() {
        IpAddressRepo ipAddress = new TaobaoIpAddress();
        ddns(ipAddress);
    }

    public void ddns(IpAddressRepo ipAddress) {
        String ip = ipAddress.getIp();
        changeRecord(ip);
    }

    /**
     * 查询并判断更改记录
     * @param value
     * @return void
     * @author XanderYe
     * @date 2020/7/9
     */
    public void changeRecord(String value) {
        if (cacheRecordList.isEmpty()) {
            log.info("拉取解析记录");
            List<DescribeDomainRecordsResponse.Record> recordList = getDescribeDomainRecords(domainName);
            if (recordList != null && !recordList.isEmpty()) {
                cacheRecordList = recordList.parallelStream()
                        .filter(record -> rrList.contains(record.getRR()))
                        .collect(Collectors.toList());
            }
        }
        if (!cacheRecordList.isEmpty()) {
            if (!cacheRecordList.get(0).getValue().equals(value)) {
                log.info("记录值变动，开始更新");
                for (DescribeDomainRecordsResponse.Record cacheRecord : cacheRecordList) {
                    cacheRecord.setType("A");
                    cacheRecord.setValue(value);
                    boolean bool = updateDomainRecord(cacheRecord);
                    if (bool) {
                        log.info("主机记录为" + cacheRecord.getRR() + "的记录值更新为：" + value);
                    }
                }
            }
        } else {
            log.error("未获取到记录");
        }
    }

    /**
     * 查询记录
     *
     * @param domainName
     * @return java.util.List<com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse.Record>
     * @author XanderYe
     * @date 2020/7/9
     */
    public List<DescribeDomainRecordsResponse.Record> getDescribeDomainRecords(String domainName) {
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest();
        request.setSysRegionId(regionId);
        request.setDomainName(domainName);
        request.setPageNumber(1L);
        request.setPageSize(10L);
        try {
            DescribeDomainRecordsResponse response = client.getAcsResponse(request);
            return response.getDomainRecords();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return null;
    }

    /**
     * 更新记录
     *
     * @param record
     * @return boolean
     * @author XanderYe
     * @date 2020/7/9
     */
    public boolean updateDomainRecord(DescribeDomainRecordsResponse.Record record) {
        UpdateDomainRecordRequest request = new UpdateDomainRecordRequest();
        request.setSysRegionId(regionId);
        request.setRecordId(record.getRecordId());
        request.setRR(record.getRR());
        request.setType(record.getType());
        request.setValue(record.getValue());
        try {
            UpdateDomainRecordResponse response = client.getAcsResponse(request);
            return true;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("ErrCode:" + e.getErrCode());
            log.error("ErrMsg:" + e.getErrMsg());
            log.error("RequestId:" + e.getRequestId());
        }
        return false;
    }

    public static String recordToString(DescribeDomainRecordsResponse.Record record) {
        return "主机记录=" + record.getRR() + "，解析线路=" + record.getLine()
                + "，当前的解析记录状态=" + record.getStatus() + "，当前解析记录锁定状态=" + record.getLocked()
                + "，记录类型=" + record.getType() + "，域名名称=" + record.getDomainName()
                + "，记录值=" + record.getValue() + "，解析记录ID=" + record.getRecordId()
                + "，生存时间=" + record.getTTL() + "，负载均衡权重=" + record.getWeight();
    }

}
