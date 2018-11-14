package com.farm.doc.server.impl;

import com.farm.core.auth.domain.LoginUser;
import com.farm.core.sql.query.DBRule;
import com.farm.core.time.TimeTool;
import com.farm.doc.dao.FarmDocfileDaoInter;
import com.farm.doc.dao.FarmRfDoctextfileDaoInter;
import com.farm.doc.domain.FarmDocfile;
import com.farm.doc.domain.FarmRfDoctextfile;
import com.farm.doc.server.FarmFileManagerInter;
import com.farm.doc.server.commons.DocumentConfig;
import com.farm.doc.server.commons.FarmDocFiles;
import com.farm.doc.server.commons.FastDfsConfig;
import com.farm.doc.util.ReadWordContentUtil;
import com.farm.parameter.FarmParameterService;
import org.apache.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

@Service
public class FarmFileManagerImpl implements FarmFileManagerInter {
    @Resource
    private FarmDocfileDaoInter farmDocfileDao;
    @Resource
    private FarmRfDoctextfileDaoInter farmRfDoctextfileDao;
    private static final Logger log = Logger.getLogger(FarmFileManagerImpl.class);
    /**
     * 附件信息缓存
     */
    private static final Map<String, FarmDocfile> fileCatch = new HashMap<String, FarmDocfile>();

    @Override
    @Transactional
    public String saveFile(File file, FILE_TYPE type, String title, LoginUser user, String path) {
        String exName = FarmDocFiles.getExName(title);
        if (exName.trim().toUpperCase().replace(".", "").equals("ZIP")) {
            type = FILE_TYPE.RESOURCE_ZIP;
        }
        String userId = null;
        String userName = null;
        if (user == null || user.getName() == null) {
            userId = "none";
            userName = "none";
        } else {
            userId = user.getId();
            userName = user.getName();
        }
        FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
                UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), title, file.getName(),
                TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), userName, userId, userName, userId, "0", null,
                exName, Float.valueOf(String.valueOf(file.length())));
        /*文件服务器保存地址*/
        docfile.setFastPath(path);
        if (user == null || user.getName() == null) {
            docfile.setCusername("none");
            docfile.setEusername("none");
        }
        FarmDocFiles.copyFile(file, FarmDocFiles.getFileDirPath() + docfile.getDir());
        docfile = farmDocfileDao.insertEntity(docfile);

        return docfile.getId();
    }

    public String saveFile(InputStream inStream, String filename, String title, FILE_TYPE type, LoginUser user) {
        String exName = FarmDocFiles.getExName(title);
        if (exName.trim().toUpperCase().replace(".", "").equals("ZIP")) {
            type = FILE_TYPE.RESOURCE_ZIP;
        }
        String userId = null;
        String userName = null;
        if (user == null || user.getName() == null) {
            userId = "none";
            userName = "none";
        } else {
            userId = user.getId();
            userName = user.getName();
        }
        FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
                UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), title, filename,
                TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), userName, userId, userName, userId, "0", null,
                exName, Float.valueOf(String.valueOf(0)));
        if (user == null || user.getName() == null) {
            docfile.setCusername("none");
            docfile.setEusername("none");
        }
        long length = FarmDocFiles.saveFile(inStream, filename,
                DocumentConfig.getString("config.doc.dir") + docfile.getDir());
        docfile.setLen(Float.valueOf(String.valueOf(length)));
        docfile = farmDocfileDao.insertEntity(docfile);
        return docfile.getId();
    }

    /**
     * 本地下载地址
     * @param fileid 文件id
     * @return
     */
    @Override
    public String getFileURL(String fileid) {
        String url = DocumentConfig.getString("config.doc.download.url") + fileid;
        return url;
    }

    /**
     * 获取fastDFS文件服务器上传地址
     * @return
     */
    public String getUploadUrl(){
        String url = FastDfsConfig.getString("file.server.upload.url");
        String file_server_ip = FastDfsConfig.getString("file.server.ip");
        String file_server_port = FastDfsConfig.getString("file.server.port");
        url = url.replaceAll("file_server_ip",file_server_ip).replaceAll("file_server_port",file_server_port);
        return url;
    }

    /**
     * 文件服务器下载地址
     * @param file
     * @return
     */
    @Override
    public String getFileURL(FarmDocfile file) {
        String url = FastDfsConfig.getString("file.server.download.url");
        String file_server_ip = FastDfsConfig.getString("file.server.ip");
        String file_server_port = FastDfsConfig.getString("file.server.port");
        url = url.replaceAll("file_server_ip",file_server_ip).replaceAll("file_server_port",file_server_port) + "?fileName=" + file.getName() + "&fileUrl=" + file.getFastPath();
        return url;
    }


    @Override
    @Transactional
    public FarmDocfile getFile(String fileid) {
        if (fileCatch.containsKey(fileid)) {
            log.debug("load file from catch");
            return fileCatch.get(fileid);
        }
        FarmDocfile file = farmDocfileDao.getEntity(fileid);
        if (file == null) {
            return null;
        }

        File dir = new File(FarmDocFiles.getFileDirPath());
        if(!dir.exists() && !dir.isDirectory()){
            dir.mkdir();
        }

        File diskFile = new File(FarmDocFiles.getFileDirPath() + File.separator + file.getDir() + file.getFilename());
        if(!diskFile.exists()){
            String fileUrl = getFileURL(file);
            diskFile = downFileFromFastDfs(file.getFilename(),fileUrl,FarmDocFiles.getFileDirPath() + File.separator +  file.getDir());
        }

        file.setFile(diskFile);
        // 如果文件是大小是0的话就刷新文件大小
        if (file.getLen() == 0) {
            file.setLen(Float.valueOf(String.valueOf(file.getFile().length())));
            if (file.getLen() == 0) {
                file.setLen(Float.valueOf(-1));
            }
            farmDocfileDao.editEntity(file);
        }
        if (fileCatch.size() < 10000) {
            fileCatch.put(fileid, file);
        }
        return file;
    }

    @Override
    public File getNoneImg() {
        String imgpath = FarmParameterService.getInstance().getParameter("config.doc.none.img.path");
        return new File(FarmParameterService.getInstance().getParameter("farm.constant.webroot.path") + File.separator
                + imgpath.replaceAll("\\\\", File.separator).replaceAll("//", File.separator));
    }

    @Override
    @Transactional
    public void submitFile(String fileId) {
        FarmDocfile file = farmDocfileDao.getEntity(fileId);
        file.setPstate("1");
        file.setEtime(TimeTool.getTimeDate14());
        farmDocfileDao.editEntity(file);
    }

    @Override
    @Transactional
    public void cancelFile(String fileId) {
        FarmDocfile file = farmDocfileDao.getEntity(fileId);
        if (file == null) {
            return;
        }
        file.setPstate("0");
        farmDocfileDao.editEntity(file);
    }

    @Override
    @Transactional
    public void delFile(String fileId, LoginUser user) {
        FarmDocfile docfile = farmDocfileDao.getEntity(fileId);
        if (docfile == null) {
            return;
        }
        File file = this.getFile(fileId).getFile();
        farmDocfileDao.deleteEntity(docfile);
        if (file.exists()) {
            if (file.delete()) {
                log.info("删除成功！");
            } else {
                log.error("文件删除失败,未能删除请手动删除！");
            }
        }
    }

    @Override
    @Transactional
    public FarmDocfile openFile(String exname, String content, LoginUser user) {
        FILE_TYPE type = FILE_TYPE.OHTER;
        String filename = UUID.randomUUID().toString();
        FarmDocfile docfile = new FarmDocfile(FarmDocFiles.generateDir(),
                UUID.randomUUID().toString().replaceAll("-", ""), type.getValue(), filename + "." + exname,
                filename + ".tmp", TimeTool.getTimeDate14(), TimeTool.getTimeDate14(), user.getName(), user.getId(),
                user.getName(), user.getId(), "0", content, exname, Float.valueOf(0));

        File file = new File(FarmDocFiles.getFileDirPath() + docfile.getDir() + File.separator + docfile.getFilename());
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException("文件创建失败!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        docfile = farmDocfileDao.insertEntity(docfile);
        docfile.setFile(file);
        return docfile;
    }

    @Override
    @Transactional
    public void addFileForDoc(String docid, String fileId, LoginUser user) {
        farmRfDoctextfileDao.insertEntity(new FarmRfDoctextfile(docid, fileId));
    }

    @Override
    @Transactional
    public void delFileForDoc(String docid, String fileId, LoginUser user) {
        List<DBRule> list = new ArrayList<DBRule>();
        list.add(new DBRule("FILEID", fileId, "="));
        list.add(new DBRule("DOCID", docid, "="));
        farmRfDoctextfileDao.deleteEntitys(list);
    }

    @Override
    public List<FarmDocfile> getAllFileForDoc(String docid) {
        List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
        for (FarmDocfile file : refiles) {
            file = getFile(file.getId());
        }
        return refiles;
    }

    @Override
    public List<FarmDocfile> getAllTypeFileForDoc(String docid, String exname) {
        List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
        List<FarmDocfile> newrefiles = new ArrayList<FarmDocfile>();
        for (FarmDocfile file : refiles) {
            if (file.getExname().toUpperCase().equals(exname.toUpperCase())) {
                file = getFile(file.getId());
                newrefiles.add(file);
            }
        }
        return newrefiles;
    }

    @Override
    public boolean containFileByDoc(String docid, String fileId) {
        List<FarmDocfile> list = farmDocfileDao.getEntityByDocId(docid);
        for (FarmDocfile node : list) {
            if (node.getId().equals(fileId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delAllFileForDoc(String docid, String exname, LoginUser aloneUser) {
        List<FarmDocfile> refiles = farmDocfileDao.getEntityByDocId(docid);
        for (FarmDocfile file : refiles) {
            if (file.getExname().toUpperCase().equals(exname.toUpperCase())) {
                delFileForDoc(docid, file.getId(), aloneUser);
            }
        }

    }

    @Override
    @Transactional
    public void delFileForDoc(String docid, LoginUser user) {
        List<DBRule> list = new ArrayList<DBRule>();
        list.add(new DBRule("DOCID", docid, "="));
        farmRfDoctextfileDao.deleteEntitys(list);
    }

    @Override
    public String getWordFileContent(File file, String fileType) {

        return ReadWordContentUtil.readWordContent(file, fileType);
    }

    @Override
    public FarmDocfile getFarmDocFile(String fileid) throws RemoteException {
        if (fileCatch.containsKey(fileid)) {
            log.debug("load file from catch");
            return fileCatch.get(fileid);
        }
        FarmDocfile file = farmDocfileDao.getEntity(fileid);
        if (file == null) {
            return null;
        }

        File dir = new File(FarmDocFiles.getFileDirPath());
        if(!dir.exists() && !dir.isDirectory()){
            dir.mkdir();
        }

        File diskFile = new File(FarmDocFiles.getFileDirPath() + File.separator + file.getDir() + file.getFilename());
        if(!diskFile.exists()){
            String fileUrl = getFileURL(file);
            diskFile = downFileFromFastDfs(file.getFilename(),fileUrl,FarmDocFiles.getFileDirPath()  + File.separator + file.getDir());
        }

        file.setFile(diskFile);
        // 如果文件是大小是0的话就刷新文件大小
        if (file.getLen() == 0) {
            file.setLen(Float.valueOf(String.valueOf(file.getFile().length())));
            if (file.getLen() == 0) {
                file.setLen(Float.valueOf(-1));
            }
            farmDocfileDao.editEntity(file);
        }
        if (fileCatch.size() < 10000) {
            fileCatch.put(fileid, file);
        }
        return file;
    }

    public  File downFileFromFastDfs(String fileName ,String fileUrl, String dir) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    fileUrl,
                    HttpMethod.GET,
                    new HttpEntity<byte[]>(headers),
                    byte[].class);

            byte[] result = response.getBody();
            inputStream = new ByteArrayInputStream(result);


            File dirFile = new File(dir);
            if(!dirFile.exists() && !dirFile.isDirectory()){
                dirFile.mkdirs();
            }

            file = new File(dir + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
