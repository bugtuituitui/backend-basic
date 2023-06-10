package com.example.common.constant;

/**
 * 自定义请求头参数
 *
 * @author kfg
 * @date 2023/6/4 22:48
 */
public interface Header {

    String TOKEN = "token";

    /**
     * api key
     */
    String APIKEY = "key";

    /**
     * api secret
     */
    String APISECRET = "secret";

    /**
     * timestamp
     */
    String TIMESTAMP = "timestamp";

    /**
     * nonce
     */
    String NONCE = "nonce";

    /**
     * signature
     */
    String SIGNATURE = "signature";
}
