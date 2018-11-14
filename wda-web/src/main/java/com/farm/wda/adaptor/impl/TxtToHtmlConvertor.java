package com.farm.wda.adaptor.impl;

import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.farm.wda.Beanfactory;
import com.farm.wda.adaptor.DocConvertorBase;
import com.farm.wda.util.AppConfig;
import com.farm.wda.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.net.ConnectException;

public class TxtToHtmlConvertor extends DocConvertorBase {
    private static final Logger log = Logger.getLogger(TxtToHtmlConvertor.class);

    public void run(File file, String fileTypeName, File targetFile) {
        // 创建Openoffice连接
        String openOfficeServer = AppConfig.getString("config.server.ip");
        int openOfficePort = Integer.valueOf(AppConfig.getString("config.openoffice.port"));
        OpenOfficeConnection con = new SocketOpenOfficeConnection(openOfficeServer, openOfficePort);
        try {
            // 连接
            con.connect();
        } catch (ConnectException e) {
            log.error("获取OpenOffice连接失败..." + e.getMessage());
        }
        try {

            String newname = file.getName().replace(FileUtil.getExtensionName(file.getName()), "") + "txt";
            file = FileUtil.copyFileAndNewName(file.getParent(), file.getName(), newname);
            String content = FileUtil.readTxtFile(file);
            StringBuffer sb = new StringBuffer();
            sb.append("<html>");
            sb.append("<head>");
            sb.append("<title>Txt文件预览</title>");
            sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");

            //样式
            sb.append("<link href=\"" + Beanfactory.WEB_URL
                    + "/css/wdacontent.css\" rel=\"stylesheet\" type=\"text/css\" />");
            sb.append("<body>");
            sb.append("<div class=\"wdaTitleBox\"><img src=\"" + Beanfactory.WEB_URL
                    + "/img/htmllogo.png\"/></div><div class=\"wdahtmlbox\">" + "<p>");

            sb.append(content.replaceAll("\n", "<br>"));

            sb.append("</p></div></body></html>");

            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(sb.toString().getBytes(AppConfig.getString("config.file.encode")));
            fos.close();

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
