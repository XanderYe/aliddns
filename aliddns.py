#!/usr/bin/env python
# coding=utf-8

# pip install aliyun-python-sdk-core-v3
# pip install aliyun-python-sdk-cdn
# pip install aliyun-python-sdk-alidns
import sys

from aliyunsdkcore.client import AcsClient
from aliyunsdkalidns.request.v20150109.DescribeDomainRecordsRequest import DescribeDomainRecordsRequest
from aliyunsdkalidns.request.v20150109.UpdateDomainRecordRequest import UpdateDomainRecordRequest
import json
import requests

# regionId
regionId = "cn-hangzhou"
# accessKeyId
accessKeyId = ""
# accessSecret
accessSecret = ""
# 域名
domainName = ""
# 主机记录
rr = ""

client = AcsClient(accessKeyId, accessSecret, regionId)


def getDescribeDomainRecords(domain_name):
    try:
        request = DescribeDomainRecordsRequest()
        request.set_accept_format('json')
        request.set_DomainName(domain_name)
        response = client.do_action_with_exception(request)
        json_data = json.loads(str(response, encoding='utf-8'))
        for record in json_data['DomainRecords']['Record']:
            if rr == record['RR']:
                return record
    except Exception as e:
        print("获取记录失败")
        print(e)
        sys.exit(-1)


def updateDomainRecord(record):
    try:
        request = UpdateDomainRecordRequest()
        request.set_accept_format('json')
        request.set_Value(record['Value'])
        request.set_Type(record['Type'])
        request.set_RR(record['RR'])
        request.set_RecordId(record['RecordId'])
        response = client.do_action_with_exception(request)
        return True
    except Exception as e:
        print(e)
        return False


def main():
    url = "http://ip.taobao.com/outGetIpInfo"
    data = {"ip": "myip", "accessKey": "alibaba-inc"}
    res = requests.post(url, data)
    json_data = res.json()
    ip = json_data['data']['queryIp']
    print("获取到公网ip为：" + ip)
    record = getDescribeDomainRecords(domainName)
    if record['Value'] != ip:
        print("记录值变动，开始更新")
        record['Type'] = "A"
        record['Value'] = ip
        flag = updateDomainRecord(record)
        if flag:
            print("主机记录为" + rr + "的记录值更新为" + ip)
        else:
            print("更新失败")
    else:
        print("记录值未变动，不更新")


if __name__ == "__main__":
    main()
