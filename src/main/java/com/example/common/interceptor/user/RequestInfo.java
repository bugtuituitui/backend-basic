package com.example.common.interceptor.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求信息
 *
 * @author kfg
 * @date 2023/6/4 22:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {

    private String ip;

    private String UID;

    private String role;
}
