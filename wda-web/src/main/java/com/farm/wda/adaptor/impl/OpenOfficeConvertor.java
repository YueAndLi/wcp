package com.farm.wda.adaptor.impl;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.farm.wda.Beanfactory;
import com.farm.wda.adaptor.DocConvertorBase;
import com.farm.wda.util.AppConfig;
import com.farm.wda.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.ConnectException;

public class OpenOfficeConvertor extends DocConvertorBase {
    private static final Logger log = Logger.getLogger(OpenOfficeConvertor.class);

    @Override
    public void run(File file, String fileTypeName, File targetFile) {
        // 创建Openoffice连接
        String openOfficeServer = AppConfig.getString("config.server.ip");
        int openOfficePort = Integer.valueOf(AppConfig.getString("config.openoffice.port"));
        OpenOfficeConnection con = new SocketOpenOfficeConnection(openOfficeServer, openOfficePort);
        try {
            // 连接
            con.connect();
        } catch (ConnectException e) {
            log.error("获取OpenOffice连接失败...,重新开启OpenOffice服务" + e.getMessage());
            Beanfactory.startOpenOfficeServer();

        }
        try {
            // 创建转换器
            DocumentConverter converter = new OpenOfficeDocumentConverter(con);
            DefaultDocumentFormatRegistry factory = new DefaultDocumentFormatRegistry();
            DocumentFormat inputDocumentFormat = factory
                    .getFormatByFileExtension(fileTypeName.toLowerCase());

            DocumentFormat outputDocumentFormat = factory
                    .getFormatByFileExtension(FileUtil.getExtensionName(targetFile.getName()));
            //如果源文件为txt，为避免中文乱码，先将txt文件重命名为odt文件
            if (fileTypeName.toLowerCase().endsWith("txt")) {
                String newname = file.getName().replace(FileUtil.getExtensionName(file.getName()), "") + "txt";
                file = FileUtil.copyFileAndNewName(file.getParent(), file.getName(), newname);
                outputDocumentFormat.setImportOption("FilterOptions", AppConfig.getString("config.file.encode"));
            }
            // 3:执行转换
            converter.convert(file, inputDocumentFormat, targetFile, outputDocumentFormat);

            log.info("成功转换：" + file + "转换到" + targetFile);
        } catch (Exception e) {
            log.error("转换错误" + e + "/" + file + "转换到" + targetFile);
            throw new RuntimeException(e);
        } finally {
            // 关闭openoffice连接
            con.disconnect();
        }

    }
}
