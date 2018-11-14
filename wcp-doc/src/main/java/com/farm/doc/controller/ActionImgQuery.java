package com.farm.doc.controller;

import com.farm.core.page.ViewMode;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.FarmFileManagerInter.FILE_TYPE;
import com.farm.parameter.FarmParameterService;
import com.farm.web.WebUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档管理
 *
 * @author autoCode
 */
@RequestMapping("/actionImg")
@Controller
public class ActionImgQuery extends WebUtils {
    private static final Logger log = Logger.getLogger(ActionImgQuery.class);

    @Resource
    private FarmFileManagerInter farmFileManagerImpl;


    private Map<String, Object> uploadFileToFastDfs(DiskFileItem item) {

        HttpHeaders headers = new HttpHeaders();

        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        headers.setContentType( MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<String, Object> ();
        form.add("files",new FileSystemResource(item.getStoreLocation()));
        //form.add("files",fileSystemResource);
        HttpEntity< MultiValueMap<String, Object> > formEntity = new HttpEntity<>(form,headers);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(farmFileManagerImpl.getUploadUrl(),formEntity, HashMap.class);
    }
    /**
     * 上传附件文件
     *
     * @return
     */
    @RequestMapping(value = "/webuploader.do")
    @ResponseBody
    public Map<String, Object> uploadMultiple(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest Murequest, ModelMap model, HttpSession session) {
        int error = 0;
        String message = "";
        String url = null;
        String id = null;
        String fileName = "";
        Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象

        try {
            for (MultipartFile file : files.values()) {
                String fileid = null;
                // 验证大小
                String maxLength = FarmParameterService.getInstance().getParameter("config.doc.upload.length.max");
                if (file.getSize() > Integer.valueOf(maxLength)) {
                    throw new Exception("文件不能超过" + Integer.valueOf(maxLength) / (1024 * 1024) + "MB");
                }
                CommonsMultipartFile cmFile = (CommonsMultipartFile) file;
                DiskFileItem item = (DiskFileItem) cmFile.getFileItem();
                {// 小于8k不生成到临时文件，临时解决办法。zhanghc20150919
                    if (!item.getStoreLocation().exists()) {
                        item.write(item.getStoreLocation());
                    }
                }

                fileName = URLEncoder.encode(item.getName(), "utf-8");
                // 验证类型
                List<String> types = parseIds(FarmParameterService.getInstance().getParameter("config.doc.upload.types")
                        .toUpperCase().replaceAll("，", ","));
                if (!types.contains(file.getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length())
                        .toUpperCase())) {
                    throw new Exception("文件类型错误，允许的类型为：" + FarmParameterService.getInstance()
                            .getParameter("config.doc.upload.types").toUpperCase().replaceAll("，", ","));
                }
                Map<String, Object> result = uploadFileToFastDfs(item);
                if(StringUtils.isNotBlank(result.get("path").toString())){
                    fileid = farmFileManagerImpl.saveFile(item.getStoreLocation(), FILE_TYPE.HTML_INNER_IMG,
                            file.getOriginalFilename(), getCurrentUser(session),result.get("path").toString());
                    error = 0;
                    url = farmFileManagerImpl.getFileURL(fileid);
                    message = "";
                    id = fileid;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            error = 1;
            message = e.getMessage();
        }
        return ViewMode.getInstance().putAttr("error", error).putAttr("url", url).putAttr("message", message)
                .putAttr("id", id).putAttr("fileName", fileName).returnObjMode();
    }

    /**
     * 上传附件文件
     *
     * @return
     */
    @RequestMapping(value = "/PubFPupload.do")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam(value = "imgFile", required = false) MultipartFile file,
                                      HttpServletRequest request, ModelMap model, HttpSession session) {
        int error = 1;
        String message = "";
        String url = null;
        String id = null;
        String fileName = "";
        try {
            String fileid = null;
            // 验证大小
            String maxLength = FarmParameterService.getInstance().getParameter("config.doc.upload.length.max");
            if (file.getSize() > Integer.valueOf(maxLength)) {
                throw new Exception("文件不能超过" + Integer.valueOf(maxLength) / (1024 * 1024) + "MB");
            }
            CommonsMultipartFile cmFile = (CommonsMultipartFile) file;
            DiskFileItem item = (DiskFileItem) cmFile.getFileItem();
            {// 小于8k不生成到临时文件，临时解决办法。zhanghc20150919
                if (!item.getStoreLocation().exists()) {
                    item.write(item.getStoreLocation());
                }
            }

            fileName = URLEncoder.encode(item.getName(), "utf-8");
            // 验证类型
            List<String> types = parseIds(FarmParameterService.getInstance().getParameter("config.doc.upload.types")
                    .toUpperCase().replaceAll("，", ","));
            if (!types.contains(file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length())
                    .toUpperCase())) {
                throw new Exception("文件类型错误，允许的类型为：" + FarmParameterService.getInstance()
                        .getParameter("config.doc.upload.types").toUpperCase().replaceAll("，", ","));
            }
            Map<String, Object> result = uploadFileToFastDfs(item);
            if(StringUtils.isNotBlank(result.get("path").toString())) {
                fileid = farmFileManagerImpl.saveFile(item.getStoreLocation(), FILE_TYPE.HTML_INNER_IMG,
                        file.getOriginalFilename(), getCurrentUser(session), "");
                error = 0;
                url = farmFileManagerImpl.getFileURL(fileid);
                message = "";
                id = fileid;
            }
        } catch (Exception e) {
            e.printStackTrace();
            error = 1;
            message = e.getMessage();
        }
        return ViewMode.getInstance().putAttr("error", error).putAttr("url", url).putAttr("message", message)
                .putAttr("id", id).putAttr("fileName", fileName).returnObjMode();
    }

    /**
     * 上传图片文件
     *
     * @return
     */
    @RequestMapping(value = "/PubFPuploadImg.do")
    @ResponseBody
    public Map<String, Object> PubFPuploadImg(@RequestParam(value = "imgFile", required = false) MultipartFile file,
                                              HttpServletRequest request, ModelMap model, HttpSession session) {
        int error = 1;
        String message = "";
        String url = null;
        String id = null;
        String fileName = "";
        try {
            String fileid = null;
            // 验证大小
            String maxLength = FarmParameterService.getInstance().getParameter("config.doc.upload.length.max");
            if (file.getSize() > Integer.valueOf(maxLength)) {
                throw new Exception("文件不能超过" + Integer.valueOf(maxLength) / 1024 + "kb");
            }
            CommonsMultipartFile cmFile = (CommonsMultipartFile) file;
            DiskFileItem item = (DiskFileItem) cmFile.getFileItem();
            {// 小于8k不生成到临时文件，临时解决办法。zhanghc20150919
                if (!item.getStoreLocation().exists()) {
                    item.write(item.getStoreLocation());
                }
            }

            fileName = URLEncoder.encode(item.getName(), "utf-8");
            // 验证类型
            List<String> types = parseIds(FarmParameterService.getInstance().getParameter("config.doc.img.upload.types")
                    .toUpperCase().replaceAll("，", ","));
            if (!types.contains(file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length())
                    .toUpperCase())) {
                throw new Exception("文件类型错误，允许的类型为：" + FarmParameterService.getInstance()
                        .getParameter("config.doc.img.upload.types").toUpperCase().replaceAll("，", ","));
            }
            Map<String, Object> result = uploadFileToFastDfs(item);
            if(StringUtils.isNotBlank(result.get("path").toString())) {
                fileid = farmFileManagerImpl.saveFile(item.getStoreLocation(), FILE_TYPE.HTML_INNER_IMG,
                        file.getOriginalFilename(), getCurrentUser(session), result.get("path").toString());
                error = 0;
                url = farmFileManagerImpl.getFileURL(fileid);
                message = "";
                id = fileid;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            error = 1;
            message = e.getMessage();
        }
        return ViewMode.getInstance().putAttr("error", error).putAttr("url", url).putAttr("message", message)
                .putAttr("id", id).putAttr("fileName", fileName).returnObjMode();
    }

    /**
     * 下载文件
     *
     * @return
     */
    @RequestMapping("/Publoadfile")
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        FarmDocfile file = null;
        try {
            file = farmFileManagerImpl.getFile(id);
        } catch (Exception e) {
            file = null;
        }
        if (file == null || file.getFile() == null || !file.getFile().exists()) {
            file = new FarmDocfile();
            file.setFile(farmFileManagerImpl.getNoneImg());
            file.setName("none");
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        try {
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + new String(file.getName().getBytes("gbk"), "iso-8859-1"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(file.getFile());
            os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (FileNotFoundException e) {
            InputStream is1 = null;
            OutputStream os1 = null;
            try {
                String webPath = FarmParameterService.getInstance().getParameter("farm.constant.webroot.path");
                String filePath = "/WEB-FACE/img/style/nullImg.png".replaceAll("/",
                        File.separator.equals("/") ? "/" : "\\\\");
                File nullFile = new File(webPath + filePath);
                is1 = new FileInputStream(nullFile);
                os1 = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = is1.read(b)) > 0) {
                    os1.write(b, 0, length);
                }

            } catch (Exception e1) {
                log.error(e.getMessage());
            } finally {
                try {
                    is1.close();
                    os1.close();
                } catch (IOException e1) {
                    log.error(e.getMessage());
                }
            }
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            // 这里主要关闭。
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                log.error(e.getMessage());
            }
        }
    }
}