package com.example.common.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Reqponse 工具类
 *
 * @author kfg
 * @date 2023/6/5 15:29
 */
public class ResponseUtils {

    /**
     * 写入内容
     *
     * @param response
     * @param object
     */
    public static void writeText(ServletResponse response, Object object) throws IOException {
        response.setContentType("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSONUtil.toJsonStr(object));
        writer.close();
    }

    /**
     * 写入响应结果
     *
     * @param response
     * @param code
     * @param message
     * @throws IOException
     */
    public static void writeText(ServletResponse response, Integer code, String message) throws IOException {
        JSON json = new JSONObject();
        json.putByPath("code", code);
        json.putByPath("message", message);
        json.putByPath("data", null);

        writeText(response, json);
    }

}
