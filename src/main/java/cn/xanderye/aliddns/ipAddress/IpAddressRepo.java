package cn.xanderye.aliddns.ipAddress;

import cn.xanderye.aliddns.util.StringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.*;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yezhendong
 * @description:
 * @date 2021/3/25 10:26
 */
@Slf4j
public abstract class IpAddressRepo {

    private String regionId;

    private String domainName;

    private IAcsClient client;


    public void setClient(IAcsClient client) {
        this.client = client;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public abstract List<DescribeDomainRecordsResponse.Record> getCacheRecordList();
    public abstract void setCacheRecordList(List<DescribeDomainRecordsResponse.Record> list);

    public abstract void setRrList();
    public abstract List<String> getRrList();

    abstract String getType();

    public abstract String getIp();

    public void changeRecord() {
        String value = getIp();
        if (StringUtil.isEmpty(value)){
            log.warn("未获取到IP,暂不变更IP!");
            return;
        }
        List<String> rrList = getRrList();
        if (rrList == null || rrList.isEmpty()){
            log.info("未配置类型为：[{}]的rr，暂不处理！",getType());
            return;
        }
        if (getCacheRecordList().isEmpty()) {
            log.info("拉取解析记录");
            List<DescribeDomainRecordsResponse.Record> recordList = getDescribeDomainRecords(domainName);
            if (recordList != null && !recordList.isEmpty()) {
                List<DescribeDomainRecordsResponse.Record> collected = recordList.parallelStream()
                        .filter(record -> rrList.contains(record.getRR()))
                        .collect(Collectors.toList());
                setCacheRecordList(collected);
            }
        }
        log.info("需要更新的解析记录:{}", JSONObject.toJSONString(getCacheRecordList()));
        if (getCacheRecordList().isEmpty()){
            log.info("未查询要解析的记录,所以新增解析!");
            addDescribeDomainRecords(value);
        }
        if (!getCacheRecordList().isEmpty()) {
            for (DescribeDomainRecordsResponse.Record cacheRecord : getCacheRecordList()) {
                if (cacheRecord.getValue().equals(value)) {
                    log.info("当前RR:[{}],的IP:[{}]无变动,所以不更新!",cacheRecord.getRR(),cacheRecord.getValue());
                    continue;
                }
                log.info("记录值变动，开始更新");
                cacheRecord.setType(getType());
                cacheRecord.setValue(value);
                boolean bool = updateDomainRecord(cacheRecord);
                if (bool) {
                    log.info("主机记录为" + cacheRecord.getRR() + "的记录值更新为：" + value);
                }
            }
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
        request.setPageSize(100L);
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
     * 添加记录
     * @return
     */
    public void addDescribeDomainRecords(String value) {
        for (String s : getRrList()) {
            AddDomainRecordRequest request = new AddDomainRecordRequest();
            request.setSysRegionId(regionId);
            request.setDomainName(domainName);
            request.setRR(s);
            request.setType(getType());
            request.setValue(value);
            try {
                client.getAcsResponse(request);
                log.info("添加解析成功,解析参数:{}", JSONObject.toJSONString(request));
            } catch (ServerException e) {
                e.printStackTrace();
            } catch (ClientException e) {
                log.error("ErrCode:" + e.getErrCode());
                log.error("ErrMsg:" + e.getErrMsg());
                log.error("RequestId:" + e.getRequestId());
            }
        }
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
