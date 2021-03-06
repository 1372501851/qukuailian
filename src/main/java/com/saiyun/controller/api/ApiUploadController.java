package com.saiyun.controller.api;

import com.saiyun.config.UpConfig;
import com.saiyun.service.common.FileService;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/app/upload")
public class ApiUploadController {
    @Resource
    private UpConfig upConfig;

    @Resource
    private FileService fileService;
    @RequestMapping(value = "/uploader", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap postUploader(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        if (!file.isEmpty()) {
            if(StringUtils.isEmpty(upConfig.getHardDisk()) && "local".equals(upConfig.getUpType())){
                return ReturnUtil.error("请配置上传目录");
            }
            String diskPath = upConfig.getHardDisk();
            //扩展名格式
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //验证文件类型
            if(!fileService.checkExt(extName)){
                return ReturnUtil.error("上传文件格式不支持");
            }
            //根据文件类型获取上传目录
            String uploadPath = fileService.getUploadPath(extName);
            uploadPath = uploadPath.replace(File.separator,"/");
            if(StringUtils.isEmpty(uploadPath)){
                return ReturnUtil.error("上传文件路径错误");
            }
            String fileName = UuidUtil.getUUID()+extName;
            String retPath = "";
            if("local".equals(upConfig.getUpType()) && StringUtils.isNotEmpty(upConfig.getUpType())){
                retPath= fileService.fileSave(file, diskPath, uploadPath, fileName);
            }else if("oss".equals(upConfig.getUpType())){
                retPath = fileService.ossSave(file, uploadPath, fileName);
            }else if("qiniu".equals(upConfig.getUpType())){
                retPath = fileService.qiniuSave(file,uploadPath,fileName);
            }
            if("null".equals(retPath)){
                return ReturnUtil.error("上传文件异常");
            }
            Map<String, String> upMap = fileService.getReturnMap(retPath, fileName);

            return ReturnUtil.success("上传成功",upMap);
        } else {
            return ReturnUtil.error("上传文件为空,");
        }
    }
}
