package com.example.common.aspect;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.lang.Result;
import com.example.common.utils.RedisUtils;
import com.example.common.utils.RequestUtils;
import com.example.common.utils.SignatureGenerator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 接口签名切面
 *
 * @author kfg
 * @date 2023/4/4 17:11
 */
@Aspect
@Component
public class ApiSignAspect {

    // api key
    private static final String API_KEY = "apiKey";

    // api secret
    private static final String API_SECRET = "apiSecret";

    // 时间戳
    private static final String TIMESTAMP = "timestamp";

    // 流水号
    private static final String NONCE = "nonce";

    // 签名
    private static final String SIGNATURE = "signature";

    private static final String SIGN_ERROR_MSG = "签名错误";

    private static final String HTTP_EXPIRATION_MSG = "请求过期";

    private static final String HTTP_RESEND_MSG = "重发请求无效";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 定义切点
     */
    // @Pointcut("execution(* com.example.controller.*.*(..))")
    @Pointcut("@annotation(com.example.common.annotation.Sign)")
    public void logPointCut() {
    }

    /**
     * 切面 配置通知
     *
     * @param joinPoint
     */
    @Around("logPointCut()")
    public Result checkSign(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = RequestUtils.getRequest();

        String apiKey = request.getHeader(API_KEY);
        String apiSecret = request.getHeader(API_SECRET);
        long timestamp = Long.valueOf(request.getHeader(TIMESTAMP));
        int nonce = Integer.valueOf(request.getHeader(NONCE));
        String sign = request.getHeader(SIGNATURE);

        // TODO 检验key 和 secret


        // 检验请求是否过期
        long now = System.currentTimeMillis();
        if (timestamp + 60 * 1000 < now) {
            return Result.fail(HTTP_EXPIRATION_MSG);
        }

        // 检验是否是重发请求
        if (redisUtils.get(apiKey + nonce) != null) {
            return Result.fail(HTTP_RESEND_MSG);
        } else {
            redisUtils.set(apiKey + nonce, 0, 60);
        }

        // 获取参数
        Object[] args = joinPoint.getArgs();

        // proceedingJoinPoint.getArgs()返回的数组中携带有Request或者Response对象，导致序列化异常
        // 过滤request 或 reponse 对象
        //获取传参信息
        //过滤无法序列化
        Stream<?> stream = ArrayUtils.isEmpty(args) ? Stream.empty() : Arrays.stream(args);
        List<Object> argList = stream
                .filter(arg -> (!(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse)))
                .collect(Collectors.toList());

        Map<String, String> map = new HashMap<>();

        // RequestParam 参数不为对象 ["123456"]
        // RequestBody 参数不为对象 ["name=123456"]
        // RequestParam 参数为对象 ["{'name': '123456'}"] （正常情况）
        // 将参数加入map
        JSONArray jsonArray = JSONObject.parseArray(JSON.toJSONString(argList.toArray()));
        JSONObject jsonObject = new JSONObject();
        if (!jsonArray.isEmpty()) {

            // 是否是正常情况
            if (JSONUtil.isJsonObj(JSON.toJSONString(jsonArray.get(0)))) {
                jsonObject = jsonArray.getJSONObject(0);
            } else {
                Enumeration<String> params = request.getParameterNames();
                while (params.hasMoreElements()) {
                    String paramName = params.nextElement();
                    map.put(paramName, request.getParameter(paramName));
                }
            }

        }

        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            map.put(key, jsonObject.getString(key));
        }

        // 拼接参数
        String params = SignatureGenerator.getKeyAndValueStr(map);
        params = StringUtils.isEmpty(params) ? "" : "&" + params + "";

        // 校验sign
        try {
            if (sign == null || !sign.equals(SignatureGenerator.generateSignature(apiKey, apiSecret, params, timestamp, nonce))) {
                return Result.fail(SIGN_ERROR_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(SIGN_ERROR_MSG);
        }

        return (Result) joinPoint.proceed(args);
    }
}
