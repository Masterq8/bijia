package com.skzh.web.controller.common;

import com.skzh.common.config.MinioConfig;
import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.utils.StringUtils;
import com.skzh.common.utils.file.FileUtils;
import io.minio.*;
import io.minio.http.Method;
import lombok.Cleanup;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Random;

/**
 * @ClassName minio文件上传
 * @Description
 * @Author wangq
 * @Date 9:40 2024/9/11
 **/
@RequestMapping("/minio")
@RestController
public class MinioController {

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * @Description 文件上传
     * @Author wangq
     * @Date 2024/9/11 10:17
     * @Param [file]
     * @return
     **/
    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 获取文件真实名称
            String originalFilename = file.getOriginalFilename();
            Random random = new Random();
            int ranNum = random.nextInt(9000) + 1000;
            String newFileName = System.currentTimeMillis()+ "-" + ranNum + "." + StringUtils.substringAfterLast(originalFilename, ".");
            @Cleanup
            InputStream inputStream = file.getInputStream();
            // 构建文件上传相关信息
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(newFileName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            // 将文件上传到minio服务器
            client.putObject(args);
            // 获取文件外链地址
            GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(newFileName)
                    .method(Method.GET)
                    .build();
            String fileUrl = client.getPresignedObjectUrl(urlArgs);
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url" , fileUrl);
            ajax.put("fileName" , newFileName);
            ajax.put("newFileName" , newFileName);
            ajax.put("originalFilename" , file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    @DeleteMapping
    public AjaxResult delete(@RequestParam("fileName") String fileName) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .build();
            client.removeObject(args);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success("删除成功");
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param response
     */
    @GetMapping("/download")
    public void filerDownload(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .build();
            InputStream inputStream = client.getObject(args);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
