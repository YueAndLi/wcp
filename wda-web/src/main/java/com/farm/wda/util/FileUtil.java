package com.farm.wda.util;

import com.farm.wda.FileEncodeUtil.EncodingDetect;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    private static final Logger log = Logger.getLogger(FileUtil.class);

    public static String readTxtFile(File file, String encodeset) {
        StringBuffer buffer = new StringBuffer();
        InputStreamReader read = null;
        InputStream inputStream = null;
        try {
            String encoding = encodeset;
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                inputStream = new FileInputStream(file);
                read = new InputStreamReader(inputStream, encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    buffer.append(lineTxt);
                }
                read.close();
            } else {
                log.error("找不到指定的文件");
            }
        } catch (Exception e) {
            log.error("读取文件内容出错" + e);
        } finally {
            try {
                read.close();
                inputStream.close();
            } catch (Exception e) {
                log.error("读取文件内容出错" + e);
            }
        }
        return buffer.toString();

    }

    /**
     * 获取txt文件编码格式
     *
     * @param file
     * @return
     */
    public static String getTxtCharset(File file) {

        InputStream inputStream = null;
        String code = "GBK"; // 或GBK
        try {
            inputStream = new FileInputStream(file);
            byte[] head = new byte[3];
            inputStream.read(head);

            if (head[0] == -1 && head[1] == -2)
                code = "UTF-16";
            else if (head[0] == -2 && head[1] == -1)
                code = "Unicode";
            else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
                code = "UTF-8";
        } catch (IOException e) {
            log.error("获取TXT编码错误" + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("获取TXT编码错误" + e);
            }
        }
        return code;
    }

    public static String getEncode(File file) {
        String fileEncode = EncodingDetect.getJavaEncode(file);
        if (StringUtils.isEmpty(fileEncode)) {
            fileEncode = "utf8";
        }
        return fileEncode;
    }

    public static String readTxtFile(File file) {
        // 获得文件编码
        String fileEncode = EncodingDetect.getJavaEncode(file);
        if (StringUtils.isEmpty(fileEncode)) {
            fileEncode = "utf8";
        }
        // 根据文件编码获得文件内容
        String fileContent = "";
        try {
            fileContent = FileUtils.readFileToString(file, fileEncode);
        } catch (IOException e) {
            log.error("获取txt文件内容失败");
        }
        return fileContent;
    }

//	public static String readTxtFile(File file) {
//		StringBuffer buffer = new StringBuffer();
//		InputStreamReader read = null;
//		InputStream inputStream = null;
//		try {
//			String encoding = AppConfig.getString("config.file.encode");
//			if (file.isFile() && file.exists()) { // 判断文件是否存在 
//				inputStream = new FileInputStream(file);
//				String fileName = file.getName();
//				String suffix = fileName
//						.substring(fileName.lastIndexOf(".") + 1);
//				if (suffix.toLowerCase().endsWith("txt")) {
//					encoding = getEncode(file);
//				}
//				read = new InputStreamReader(inputStream, encoding);// 考虑到编码格式
//				BufferedReader bufferedReader = new BufferedReader(read);
//				String lineTxt = null;
//				while ((lineTxt = bufferedReader.readLine()) != null) {
//					buffer.append(lineTxt + "\n");
//				}
//				read.close();
//			} else {
//				log.error("找不到指定的文件");
//			}
//		} catch (Exception e) {
//			log.error("读取文件内容出错" + e);
//		} finally {
//			try {
//				read.close();
//				inputStream.close();
//			} catch (Exception e) {
//				log.error("读取文件内容出错" + e);
//			}
//		}
//		return buffer.toString();
//
//	}

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 写日志
     *
     * @param log2 文件
     * @param e异常
     */
    public static void wirteLog(File log2, String message) {
        RandomAccessFile raf;
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd   hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            if (!log2.exists()) {
                log2.createNewFile();
            }
            raf = new RandomAccessFile(log2, "rw");
            try {
                // 按读写方式创建一个随机访问文件流
                long fileLength = raf.length();// 获取文件的长度即字节数
                // 将写文件指针移到文件尾。
                raf.seek(fileLength);
                // 按字节的形式将内容写到随机访问文件流中
                raf.writeBytes("Log0N  " + date + ":   " + message + "\r\n");
            } catch (IOException e1) {
                log.error(e1);
            } finally {
                // 关闭流
                raf.close();
            }
        } catch (Exception e2) {
            log.error(e2);
        }
    }

    /**
     * 写日志
     *
     * @param log2 文件
     * @param e异常
     */
    public static void wirteInfo(File file, String text) {
        FileOutputStream out = null;
        try {
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new FileOutputStream(file, false);
                out.write(text.getBytes(AppConfig
                        .getString("config.file.encode")));
                out.close();
            } catch (IOException ex) {
                log.error(ex);
            } finally {
                // 关闭流
                out.close();
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern
                .compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern
                .compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim().replaceAll("&nbsp;", ""); // 返回文本字符串
    }

    /**
     * 获得页面字符匹配
     */
    public static String matchCharset(String content) {
        String chs = "gb2312";
        Pattern p = Pattern.compile("(?<=charset=)(.+)(?=\")");
        Matcher m = p.matcher(content);
        if (m.find())
            return m.group();
        return chs;
    }

    /**
     * 获得html字符集
     *
     * @param content
     * @return
     */
    public static String getCharSet(String html) {
        String charset = matchCharset(html).replaceAll("'", "\"");
        charset = charset.substring(
                0,
                charset.indexOf("\"") > 0 ? charset.indexOf("\"") : charset
                        .length());
        return charset.trim();
    }

    /**
     * 文件重命名
     *
     * @param path    源目录
     * @param oldName 原文件名
     * @param newName 新文件名
     */
    public static File reNameFile(String path, String oldname, String newname) {
        File oldfile = new File(path + "/" + oldname);
        File newfile = new File(path + "/" + newname);
        if (!oldfile.exists()) {
            log.error("重命名的源文件不存在");
            return oldfile;// 重命名文件不存在
        }
        oldfile.renameTo(newfile);
        return newfile;
    }

    /**
     * 文件重命名
     *
     * @param path    源目录
     * @param oldname 原文件名
     * @param newname 新文件名
     * @return
     */
    public static File copyFileAndNewName(String path, String oldname,
                                          String newname) {
        String oldPath = path + "/" + oldname;
        String newPath = path + "/" + newname;
        File oldfile = new File(oldPath);
        File newfile = new File(newPath);
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldPath); // 读入原文件
                fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("复制单个文件操作出错");
            return oldfile;
        } finally {
            try {
                inStream.close();
                fs.close();
            } catch (IOException e) {
                log.error("复制单个文件操作出错");
            }

        }
        return newfile;
    }
}
