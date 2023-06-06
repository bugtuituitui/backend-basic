package com.example.common.pay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.pay.enums.PayStatus;
import com.pay.ApiClient;
import com.pay.dto.NotifyDTO;
import com.pay.enums.PayEnums;
import com.pay.exception.ApiException;
import com.pay.request.PayRequest;
import com.pay.request.QueryRequest;
import com.pay.response.PayResponse;
import com.pay.response.QueryResponse;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * 7支付工具类
 * https://7-pay.cn/doc.php
 */
public class PayUtils {

    private static String PID = "xxxxx";
    private static String PKEY = "xxxxx";
    private static String SERVER_URL = "https://www.7-pay.cn";

    //回调地址
    private static String NOTIFY_URL = "xxxxxx";
    //交易完成后浏览器跳转（统一下单）
    private static String RETURN_URL = "xxxxxx";


    /**
     * 生成订单 (跳转收银台)
     *
     * @param body   名称
     * @param fee    金额
     * @param remark 备注
     * @return
     */
    public static Map<String, String> pay(String body, String fee, String remark) {

        Map<String, String> map = new HashedMap();

        //统一下单（跳转收银台）
        PayRequest payRequest = new PayRequest();
        payRequest.setBody(body);
        // 需要去除小数点后就多余的0，否则支付宝报签名错误
        payRequest.setFee(new BigDecimal(fee).stripTrailingZeros().toPlainString());
        payRequest.setPay_type(PayEnums.PAY_TYPE.ALIPAY.getCode());
        payRequest.setRemark(remark);
        String no = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + num();
        payRequest.setNo(no);
        payRequest.setNotify_url(NOTIFY_URL);
        payRequest.setPid(PID);
        payRequest.setReturn_url(RETURN_URL);

        ApiClient client = new ApiClient(SERVER_URL, PKEY);

        map.put("no", no);
        try {
            PayResponse payResponse = client.execute(payRequest, true);

            System.out.println(JSONUtil.toJsonStr(payResponse));
            map.put("url", payResponse.getUrl());
            return map;
        } catch (ApiException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * 查询订单是否支付成功
     *
     * @param no 订单编号
     * @return
     */
    public static boolean paySuccess(String no) {
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setNo(no);
        queryRequest.setPid(PID);

        ApiClient client = new ApiClient(SERVER_URL, PKEY);

        try {
            QueryResponse queryResponse = client.execute(queryRequest, false);
            return PayStatus.SUCCESS.getCode().equals(queryResponse.getCode());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 付款后回调信息验证
     *
     * @param order 订单相关信息
     * @return
     */
    public static boolean payCallback(String order) {
        // 付款后回调信息验证
        NotifyDTO param = JSONUtil.toBean(order, NotifyDTO.class);
        ApiClient client = new ApiClient(PKEY);
        try {
            boolean result = client.checkParam(param);
            System.out.println(result);
            return result;
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
//        //统一下单（跳转收银台）
//        PayRequest payRequest = new PayRequest();
//        payRequest.setBody("测试商品");
//        payRequest.setFee("0.01");
//        payRequest.setPay_type(PayEnums.PAY_TYPE.ALIPAY.getCode());
//        payRequest.setRemark("备注");
//        String no = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS")+num();
//        payRequest.setNo(no);
//        payRequest.setNotify_url(NOTIFY_URL);
//        payRequest.setPid(PID);
//        payRequest.setReturn_url(RETURN_URL);
//        ApiClient client = new ApiClient(SERVER_URL,PKEY);
//        try {
//            PayResponse payResponse = client.execute(payRequest,true);
//            System.out.println(JSONUtil.toJsonStr(payResponse));
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }

//        //统一下单（API）
//        CreatRequest creatRequest = new CreatRequest();
//        creatRequest.setBody("测试商品");
//        creatRequest.setFee("0.01");
//        creatRequest.setPay_type(PayEnums.PAY_TYPE.ALIPAY.getCode());
//        creatRequest.setRemark("备注");
//        String no = DateUtil.format(new Date(),"yyyyMMddHHmmssSSS")+num();
//        creatRequest.setNo(no);
//        creatRequest.setNotify_url(NOTIFY_URL);
//        creatRequest.setPid(PID);
//
//        ApiClient client = new ApiClient(SERVER_URL,PKEY);
//        try {
//            CreatResponse creatResponse = client.execute(creatRequest,false);
//            System.out.println(JSONUtil.toJsonStr(creatResponse));
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//
//        //发起退款
//        RefundRequest refundRequest = new RefundRequest();
//        refundRequest.setNo("20221202145449589681306");
//        refundRequest.setPid(PID);
//
//        ApiClient client = new ApiClient(SERVER_URL,PKEY);
//        try {
//            RefundResponse refundResponse = client.execute(refundRequest,false);
//            System.out.println(JSONUtil.toJsonStr(refundResponse));
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//
//        //查询订单状态
//        QueryRequest queryRequest = new QueryRequest();
//        queryRequest.setNo("202342612411962932052");
//        queryRequest.setPid(PID);
//
//        ApiClient client = new ApiClient(SERVER_URL,PKEY);
//        try {
//            QueryResponse queryResponse = client.execute(queryRequest,false);
//            System.out.println(JSONUtil.toJsonStr(queryResponse));
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//
//        //付款后回调信息验证
//        String str = "{\"title\":\"测试商品\",\"money\":\"0.01\",\"no\":\"2023426124135609652091\",\"tradeno\":\"20221222172225\",\"remark\":\"备注\",\"time\":\"2022-12-02 23:07:31\",\"paytype\":\"alipay\",\"sign\":\"ccb60fbc0355f7663d4a034159c0611d\"}";
//        NotifyDTO param = JSONUtil.toBean(str,NotifyDTO.class);
//        ApiClient client = new ApiClient(PKEY);
//        try {
//            boolean success = client.checkParam(param);
//            System.out.println(success);
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
    }


    private static String num() {
        String num = "";
        for (int i = 0; i < 6; i++) {
            num += new Random().nextInt(10);
        }
        return num;
    }

}
