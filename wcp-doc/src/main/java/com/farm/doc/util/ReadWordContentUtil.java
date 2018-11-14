package com.farm.doc.util;

import com.farm.doc.FileEncodeUtil.EncodingDetect;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadWordContentUtil {

    public static final String[] FILETYPE = {".doc", ".docx", ".pdf", ".txt"};

    public static void main(String[] args) {
        // File file = new
        // File("F:\\220 验收文档\\参考文件\\国家农产品质量安全追溯管理信息平台需求规格说明书-修订版(定稿)v4.2.3.docx");
        File file = new File(
                "F:\\农业农村部资源环境板块整合\\00-参考资料\\其它\\11 生态总站 周报2018年 第34周 20180511.pdf");
        // File file = new File("F:\\220 验收文档\\参考文件\\国家追溯平台-概要设计（终）.doc");
        System.out.println(readWordContent(file, ".pdf"));
    }

    public static String readWordContent(File file, String fileType) {
        String result = "";
        try {

            String fileName = file.getName();
//			String fileType = fileName.substring(fileName.lastIndexOf("."),
//					fileName.length());
            if (!fileType.startsWith(".")) {
                fileType = "." + fileType;
            }

            if (Arrays.asList(FILETYPE).contains(fileType)) {
                switch (fileType) {
                    case ".doc":
                        result = doc2String2(file);
                        break;
                    case ".docx":
                        result = docx2String(file);
                        break;
                    case ".pdf":
                        result = pdf2String(file);
                        break;
                    case ".txt":
                        result = txt2String(file);
                        break;
                    default:
                        result = "";
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(result);
        result = m.replaceAll("");
        return result;
    }

    private static String txt2String(File file) {
        String fileEncode = EncodingDetect.getJavaEncode(file);
        FileInputStream inputStream = null;
        Scanner sc = null;
        String result = "";
        try {
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, fileEncode);
            while (sc.hasNextLine()) {
                result += sc.nextLine();
                // System.out.println(line);
            }
            // note that Scanner suppresses exceptions
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return result;
    }

    /**
     * 读取doc文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String doc2String(File file) {
        String result = "";
        StringBuffer sb = new StringBuffer("");
        try {
            FileInputStream fis = new FileInputStream(file);
            WordExtractor ext = new WordExtractor(fis);
            String[] strArr = ext.getParagraphText();
            for (String str : strArr) {
                sb.append(str.trim());
            }
            result = sb.toString().replaceAll("r", "");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取doc文件内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String doc2String2(File file) {
        String result = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            HWPFDocument doc = new HWPFDocument(fis);
            Range range = doc.getRange();
            result += range.text().toString().trim().replaceAll("r", "");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取docx文件
     *
     * @param file
     * @return
     */
    public static String docx2String(File file) {
        String str = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument xdoc = new XWPFDocument(fis);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            String doc1 = extractor.getText();
            str += doc1;
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 读取pdf
     *
     * @param file
     * @return
     */
    public static String pdf2String(File file) {
        String result = "";
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(false);
            result = stripper.getText(document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
