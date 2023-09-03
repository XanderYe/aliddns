package cn.xanderye.aliddns.service;

import cn.xanderye.aliddns.ipAddress.IpAddressRepo;
import cn.xanderye.aliddns.util.PropertyUtil;
import cn.xanderye.aliddns.util.StringUtil;
import cn.xanderye.aliddns.util.SystemUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * Created on 2020/7/9.
 *
 * @author XanderYe
 */
@Slf4j
public class DDnsService {

    private static final String regionId;
    private static final String accessKeyId;
    private static final String accessSecret;
    private static final String domainName;

    private static final IAcsClient client;

    static {
        PropertyUtil.init();
        regionId = SystemUtil.getOrDefault("REGIN_ID", PropertyUtil.get("aliyun.region-id"));
        accessKeyId = SystemUtil.getOrDefault("ACCESS_KEY_ID", PropertyUtil.get("aliyun.access-key-id"));
        accessSecret = SystemUtil.getOrDefault("ACCESS_SECRET", PropertyUtil.get("aliyun.access-secret"));
        domainName = SystemUtil.getOrDefault("DOMAIN_NAME", PropertyUtil.get("aliyun.domainName"));
        if (StringUtil.isAnyEmpty(regionId, accessKeyId, accessSecret, domainName)) {
            log.error("请先配置参数");
            System.exit(-1);
        }
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
        client = new DefaultAcsClient(profile);
    }


    public void ddns(IpAddressRepo ipAddress) {
        ipAddress.setClient(client);
        ipAddress.setDomainName(domainName);
        ipAddress.setRegionId(regionId);
        ipAddress.setRrList();
        ipAddress.changeRecord();
    }


}
