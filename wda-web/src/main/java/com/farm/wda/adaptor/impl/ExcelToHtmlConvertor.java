package com.farm.wda.adaptor.impl;

import com.farm.wda.Beanfactory;
import com.farm.wda.adaptor.DocConvertorBase;
import com.farm.wda.util.ExcelUtil;
import com.farm.wda.util.FileUtil;
import com.farm.wda.util.Transform;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelToHtmlConvertor extends DocConvertorBase {
    private static final Logger log = Logger
            .getLogger(TxtToHtmlConvertor.class);

    @SuppressWarnings("resource")
    @Override
    public void run(File file, String fileTypeName, File targetFile) {
        try {

            String newname = file.getName().replace(
                    FileUtil.getExtensionName(file.getName()), "")
                    + fileTypeName;
            file = FileUtil.copyFileAndNewName(file.getParent(),
                    file.getName(), newname);

            String content = Transform.getContent(file);

            String charset = FileUtil.getCharSet(content);

            Document htmldoc = Jsoup.parse(content);

            Elements head = htmldoc.getElementsByTag("HEAD");

            Elements title = htmldoc.getElementsByTag("TITLE");
            if (title == null) {
                head.append("<title>Excel文件预览</title>");
            } else {
                title.append("Excel文件预览");
            }

            head.append("<link href=\"" + Beanfactory.WEB_URL + "/css/excel.css\" rel=\"stylesheet\" type=\"text/css\" />");

            /* 引入js */
            head.append("<script src=\"" + Beanfactory.WEB_URL + "/js/jquery11.3.js\"  type='text/javascript'></script>");

            Elements body = htmldoc.getElementsByTag("body");

            Elements tables = htmldoc.getElementsByTag("table");


            // String content = FileUtil.readTxtFile(file);
            // 获取excel的sheet当做key值
            List sheetsKeys = (List) ExcelUtil.getSheets(file);
            // 获取sheet的内容（html代码）当做value值
            Map sheetsValues = ExcelUtil.excelToHtml(file);

            String sheetContext = "";
            String sheetValues = "";

            for (int i = 0; i < sheetsKeys.size(); ++i) {
                sheetContext += "<li id=\"" + i + "\">" + sheetsKeys.get(i) + "</li>";
                String table = "";
                if (i > tables.size() - 1) {
                    table = "<table id=\"sheet_" + i + "\" style='display:none;'></table>";
                } else {
                    if (i != 0) {
                        tables.get(i).attr("style", "display:none;");
                    }
                    table = tables.get(i).attr("id", "sheet_" + i).outerHtml();

                }
                sheetValues += table;
            }

            body.html("");


            StringBuffer sb = new StringBuffer();

            sb.append("<div class='tab_infos'>").append("\n");

            sb.append("<div class='tab_txt'>").append("\n");

            sb.append(sheetValues).append("\n");

            sb.append("</div></div>").append("\n");
            sb.append("<ul class='tab_tit'>");
            sb.append(sheetContext).append("\n");
            sb.append("</ul>");
            /* js代码 */

            sb.append(
                    "<script type='text/javascript'>"
                            + "$(function(){$('li').each(function(i){"
                            + "var id = $(this).attr('id');$(this).click ( function(){"
                            + "$('table').each(function(){$(this).hide();$(this).removeClass('selected');});"
                            + "$('#sheet_' + id).show();$(this).addClass('selected');});});});</script>")
                    .append("\n");

            body.append("<div class=\"wdahtmlbox\"><div class=\"wdaTitleBox\"><img src=\"" + Beanfactory.WEB_URL + "/img/htmllogo.png\"/></div>" + sb.toString() + "</div>");
			/*sb.append("<html>").append("\n");
			sb.append("<head>").append("\n");
			sb.append("<title>Excel文件预览</title>").append("\n");
			sb.append(
					"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />")
					.append("\n");

			// 样式
			sb.append(
					"<link href=\""
							+ Beanfactory.WEB_URL
							+ "/css/wdacontent.css\" rel=\"stylesheet\" type=\"text/css\" />")
					.append("\n");

			sb.append(
					"<link href=\""
							+ Beanfactory.WEB_URL
							+ "/css/excel.css\" rel=\"stylesheet\" type=\"text/css\" />")
					.append("\n");

			 引入js 
			sb.append(
					"<script src=\""
							+ Beanfactory.WEB_URL
							+ "/js/jquery11.3.js\"  type='text/javascript'></script>")
					.append("\n");
			sb.append("</head>").append("\n");
			sb.append("<body>").append("\n");*/
			/*sb.append(
					"<div class=\"wdaTitleBox\"><img src=\""
							+ Beanfactory.WEB_URL
							+ "/img/htmllogo.png\"/></div><div class=\"wdahtmlbox\">")
					.append("\n");

			


			for (int i = 0; i < sheetsKeys.size(); ++i) {
				sheetContext += "<li id=\"" + i + "\">" + sheetsKeys.get(i)
						+ "</li>";
				sheetValues += sheetsValues.get(sheetsKeys.get(i)).equals("") ? "<table id=\"sheet_"
						+ i + "\" >"
						: sheetsValues.get(sheetsKeys.get(i));
			}

			sb.append(sheetContext).append("\n");

			sb.append("</ul><div class='tab_txt'>").append("\n");

			sb.append(sheetValues).append("\n");

			sb.append("</div></div>").append("\n");

			 js代码 

			sb.append(
					"<script type='text/javascript'>"
							+ "$(function(){$('li').each(function(i){"
							+ "var id = $(this).attr('id');$(this).click ( function(){"
							+ "$('table').each(function(){$(this).hide();});"
							+ "$('#sheet_' + id).show();});});});</script>")
					.append("\n");

			// sb.append(content.replaceAll("\n", "<br>"));

			sb.append("</div></body></html>").append("\n");*/

            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(htmldoc.toString().getBytes(charset));
            fos.close();

            log.info("成功转换：" + file + "转换到" + targetFile);
        } catch (Exception e) {
            log.error("转换错误" + e + "/" + file + "转换到" + targetFile);
            throw new RuntimeException(e);
        }
    }

}
