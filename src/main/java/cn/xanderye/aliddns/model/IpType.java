package cn.xanderye.aliddns.model;

import lombok.Getter;

/**
 * @author cheng
 * @date 2022年9月4日
 */
@Getter
public enum IpType {
    IPV4("ipv4"),
    IPV6("ipv6"),
    ;

    IpType(String type) {
        this.type = type;
    }

    private String type;
}
