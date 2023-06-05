package com.example.controller;

import com.example.common.annotation.Auth;
import com.example.common.annotation.FrequencyControl;
import com.example.common.annotation.Sign;
import com.example.common.lang.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author kfg
 * @date 2023/6/5 10:21
 */
@RestController
@RequestMapping("/test")
public class TestController {

    //@Auth(type = Auth.Type.HasRole, hasRole = {"1"})
    @Auth(type = Auth.Type.OnlyRole, onlyRole = "1")
    @RequestMapping("/auth")
    public Result auth() {
        return Result.success();
    }


    // 接口频控为每分钟3次
    @FrequencyControl(api = "/test/frequency", count = 3, time = 60, unit = TimeUnit.SECONDS)
    @RequestMapping("/frequency")
    public Result freControl() {
        return Result.success();
    }


    @Sign(value = "接口签名")
    @RequestMapping("/sign")
    public Result sign() {
        return Result.success();
    }

}
