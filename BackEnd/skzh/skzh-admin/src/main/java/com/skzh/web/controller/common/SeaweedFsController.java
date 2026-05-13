package com.skzh.web.controller.common;

import com.skzh.common.core.domain.AjaxResult;
import com.skzh.common.utils.StringUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seaweedfs.client.FilerClient;
import seaweedfs.client.SeaweedInputStream;
import seaweedfs.client.SeaweedOutputStream;

import javax.servlet.http.HttpServletResponse;
import java.util.Random;

/**
 * @ClassName 分布式文件服务
 * @Description
 * @Author wangq
 * @Date 15:28 2024/8/14
 **/
@RequestMapping("/seaweedfs")
@RestController
public class SeaweedFsController {

    @Value("${seaweedFs.host}")
    private String host;

    @Value("${seaweedFs.grpcPort}" )
    private int grpcPort;

    /**
     * 文件上传
     *
     * @param file
     */
    @PostMapping("/upload")
    public AjaxResult filerUpload(@RequestParam("file") MultipartFile file) {
        try {
            //文件名
            String fileName = file.getOriginalFilename();
            Random random = new Random();
            int ranNum = random.nextInt(9000) + 1000;
            String newFileName = System.currentTimeMillis()+ "-" + ranNum + "." + StringUtils.substringAfterLast(fileName, ".");
            FilerClient filerClient = new FilerClient(host, grpcPort);
            filerClient.setAccessVolumeServerByPublicUrl();
            String fullPath = "/bijia/"+newFileName;
            SeaweedOutputStream seaweedOutputStream = new SeaweedOutputStream(filerClient, fullPath);
            seaweedOutputStream.setCollection("DefaultDataCenter");
            byte[] fileByte = file.getBytes();
            seaweedOutputStream.write(fileByte);
            seaweedOutputStream.flush();
            seaweedOutputStream.close();
            return AjaxResult.success("上传成功", newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("上传失败");
        }
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
            FilerClient filerClient = new FilerClient(host, grpcPort);
            SeaweedInputStream seaweedInputStream = new SeaweedInputStream(filerClient, "/bijia/"+fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(seaweedInputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
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
            FilerClient filerClient = new FilerClient(host, grpcPort);
            filerClient.rm("/bijia/"+fileName, true, true);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("删除失败");
        }
        return AjaxResult.success("删除成功");
    }

}
