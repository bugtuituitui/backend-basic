package com.example.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 七牛云工具类
 */
@Component
public class QiNiuUtil {

    /**
     * 七牛云的域名，如果自己没有域名
     * 这里可以存放临时域名（七牛自带的临时域名只有30天）
     * 这个变量可能会在其他地方用到（比如拼接字符串），所以设为public
     */
    public static final String QINIU_IMAGE_DOMAIN = "rssnkd77g.hn-bkt.clouddn.com";
    /**
     * 可接收的后缀名
     */
    private static String[] IMAGE_FILE_EXTD = new String[]{"png", "jpg", "jpeg", "docx", "ppt", "pdf", "pptx", "pdf"};
    /**
     * 七牛云AK
     */
    private final String accessKey = "xxxxxx";
    /**
     * 七牛云密钥
     */
    private final String secretKey = "xxxxxx";
    /**
     * 仓库名
     */
    private final String bucket = "xxxxxx";
    /**
     * 通过AK和SK创建Auth
     */
    private Auth auth = Auth.create(accessKey, secretKey);
    /**
     * 创建地区
     */
    private Configuration cfg = new Configuration(Region.huanan());
    /**
     * 七牛封装的上传工厂
     */
    private UploadManager uploadManager = new UploadManager(cfg);

    /**
     * 判断是否是允许的格式
     */
    private static boolean isFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取简单的上传凭证
     *
     * @return 此处的token可以配合前端JS是哦那个
     */
    public String getUpToken() {
        return auth.uploadToken(bucket);
    }


    /**
     * 上传文件
     *
     * @param file 文件
     * @return hashmap 状态
     */
    public HashMap uploadFile(MultipartFile file) throws IOException {
        HashMap<String, String> map = Maps.newHashMap();

        /** 获取文件格式 */
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            map.put("error", "文件没有后缀名");
            return map;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();


        // 判断后缀是否合法
//        if (!isFileAllowed(fileExt)) {
//            map.put("error", "文件后缀名不合法");
//            return map;
//        }

        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        // 上传
        Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());

        if (res.isOK() && res.isJson()) {

            map.put("path", QINIU_IMAGE_DOMAIN + "/" + JSONObject.parseObject(res.bodyString()).get("key"));
            return map;
        }

        map.put("error", "上传失败");
        return map;
    }


    /**
     * 从前台接收文件保存到七牛云中
     *
     * @param files 多个文件
     * @return hashmap存状态
     */
    public HashMap uploadFiles(MultipartFile[] files) throws IOException {
        HashMap<String, String> maps = Maps.newHashMap();
        if (files.length > 0) {
            for (MultipartFile file : files) {
                /** 获取文件格式 */
                int dotPos = file.getOriginalFilename().lastIndexOf(".");
                if (dotPos < 0) {
                    maps.put(file.getOriginalFilename(), "文件没有后缀名");
                } else {
                    String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
                    /** 判断是否是合法的文件后缀 */
                    if (!isFileAllowed(fileExt)) {
                        maps.put(file.getOriginalFilename(), "文件后缀名不合法");
                    }
                    String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
                    /** 调用put方法上传 */
                    Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
                    /** 打印返回的信息 */
                    if (res.isOK() && res.isJson()) {
                        maps.put(QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key"), "success");
                    } else {
                        maps.put(file.getOriginalFilename(), "上传失败");
                    }
                }
            }
        } else {
            maps.put("fail", "未上传文件");
        }

        return maps;
    }


    /**
     * 删除文件 单删
     *
     * @param fileName 要删除的文件名
     * @return
     */
    public int deleteFile(String fileName) {
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            Response delete = bucketManager.delete(bucket, fileName);
            return delete.statusCode;
        } catch (QiniuException ex) {
            ex.printStackTrace();
            System.err.println(ex.code());
            System.err.println(ex.response.toString());
        }
        return -1;
    }


    /**
     * 通过数组批量删除七牛云已上传的文件
     *
     * @param fileNames 数组形式存放的文件名
     */
    public HashMap deleteFiles(String[] fileNames) throws QiniuException {
        HashMap<String, String> maps = Maps.newHashMap();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
        batchOperations.addDeleteOp(bucket, fileNames);
        Response response = bucketManager.batch(batchOperations);
        BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
        for (int i = 0; i < fileNames.length; i++) {
            BatchStatus status = batchStatusList[i];
            String key = fileNames[i];
            System.out.print(key + "\t");
            maps.put(key, String.valueOf(status.code));
        }
        return maps;
    }


    /**
     * 通过List批量删除文件
     *
     * @param fileNames list储存的文件名
     */
    public HashMap deleteFiles(List<String> fileNames) throws QiniuException {
        String[] fileArr = fileNames.toArray(new String[fileNames.size()]);
        for (int i = 0; i < fileNames.size(); i++) {
            fileArr[i] = fileNames.get(i);
        }
        return deleteFiles(fileArr);
    }
}

