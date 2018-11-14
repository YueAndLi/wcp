package com.farm.wda.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static List getTitltByXSLExcel(InputStream input) {
        ArrayList list = new ArrayList();
        Workbook workBook = null;
        Sheet sheet = null;
        if (!((InputStream) input).markSupported()) {
            input = new PushbackInputStream((InputStream) input, 8);
        }

        try {
            if (!POIFSFileSystem.hasPOIFSHeader((InputStream) input)
                    && !POIXMLDocument.hasOOXMLHeader((InputStream) input)) {
                log.error("非法的输入流：当前输入流非OLE2流或OOXML流！");
            } else {
                workBook = WorkbookFactory.create((InputStream) input);
            }
        } catch (IOException arg17) {
            log.error("创建表格工作簿对象发生IO异常！原因：" + arg17.getMessage(), arg17);
        } catch (InvalidFormatException arg18) {
            log.error("非法的输入流：当前输入流非OLE2流或OOXML流！", arg18);
        }

        try {
            if (workBook != null) {
                int numberSheet = workBook.getNumberOfSheets();
                if (numberSheet > 0) {
                    sheet = workBook.getSheetAt(0);
                    int rowCount = sheet.getPhysicalNumberOfRows();
                    if (rowCount > 1) {
                        Row titleRow = sheet.getRow(0);
                        int colNum = titleRow.getPhysicalNumberOfCells();

                        for (int i = 0; i < colNum; ++i) {
                            list.add(titleRow.getCell(i).getStringCellValue());
                        }
                    }
                } else {
                    log.warn("目标表格工作簿(Sheet)数目为0！");
                }
            }
        } finally {
            try {
                if (input != null) {
                    ((InputStream) input).close();
                }
            } catch (IOException arg15) {
                ;
            }
        }

        return list;
    }

    public static List<Object> getSheets(File file) {
        ArrayList list = new ArrayList();
        Workbook workBook = null;
        Sheet sheet = null;
        Object input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException arg16) {
            log.error("未找到指定的文件！", arg16);
        } catch (Exception arg17) {
            log.error("读取Excel文件发生异常！", arg17);
        }

        if (!((InputStream) input).markSupported()) {
            input = new PushbackInputStream((InputStream) input, 8);
        }

        try {
            if (!POIFSFileSystem.hasPOIFSHeader((InputStream) input)
                    && !POIXMLDocument.hasOOXMLHeader((InputStream) input)) {
                log.error("非法的输入流：当前输入流非OLE2流或OOXML流！");
            } else {
                workBook = WorkbookFactory.create((InputStream) input);
            }
        } catch (IOException arg19) {
            log.error("创建表格工作簿对象发生IO异常！原因：" + arg19.getMessage(), arg19);
        } catch (InvalidFormatException arg20) {
            log.error("非法的输入流：当前输入流非OLE2流或OOXML流！", arg20);
        }

        try {
            if (workBook != null) {
                int numberSheet = workBook.getNumberOfSheets();
                if (numberSheet > 0) {
                    for (int i = 0; i < numberSheet; ++i) {
                        sheet = workBook.getSheetAt(i);
                        list.add(sheet.getSheetName());
                    }
                } else {
                    log.warn("目标表格工作簿(Sheet)数目为0！");
                }
            }
        } finally {
            try {
                if (input != null) {
                    ((InputStream) input).close();
                }
            } catch (IOException arg15) {
                ;
            }
        }
        return list;
    }

    public static Map excelToHtml(File file) {
        HashMap map = new HashMap();
        Workbook workBook = null;
        Sheet sheet = null;
        Object input = null;

        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException arg16) {
            log.error("未找到指定的文件！", arg16);
        } catch (Exception arg17) {
            log.error("读取Excel文件发生异常！", arg17);
        }

        if (!((InputStream) input).markSupported()) {
            input = new PushbackInputStream((InputStream) input, 8);
        }

        try {
            if (!POIFSFileSystem.hasPOIFSHeader((InputStream) input)
                    && !POIXMLDocument.hasOOXMLHeader((InputStream) input)) {
                log.error("非法的输入流：当前输入流非OLE2流或OOXML流！");
            } else {
                workBook = WorkbookFactory.create((InputStream) input);
            }
        } catch (IOException arg19) {
            log.error("创建表格工作簿对象发生IO异常！原因：" + arg19.getMessage(), arg19);
        } catch (InvalidFormatException arg20) {
            log.error("非法的输入流：当前输入流非OLE2流或OOXML流！", arg20);
        }

        try {
            if (workBook != null) {
                int numberSheet = workBook.getNumberOfSheets();
                if (numberSheet > 0) {
                    for (int i = 0; i < numberSheet; ++i) {
                        sheet = workBook.getSheetAt(i);
                        map.put(sheet.getSheetName(), sheetToHtml(sheet, i));
                    }
                } else {
                    log.warn("目标表格工作簿(Sheet)数目为0！");
                }
            }
        } finally {
            try {
                if (input != null) {
                    ((InputStream) input).close();
                }
            } catch (IOException arg15) {
                ;
            }

        }

        return map;
    }

    public static String sheetToHtml(Sheet sheet, int sheetNum) {
        StringBuffer html = new StringBuffer();
        boolean tempCellNum = false;
        short lastCellNum = 0;
        Workbook workbook = sheet.getWorkbook();
        boolean isCellNull = true;
        int firstRowNum = sheet.getFirstRowNum();
        int lastRowNum = sheet.getLastRowNum();
        sheet.getPhysicalNumberOfRows();
        html.append("<table id=\"sheet_" + sheetNum + "\"");
        if (sheetNum != 0) {
            html.append(" style='display:none;' ");
        }
        html.append(">");
        if (lastRowNum == 0) {
            html.append("</table>");
            return html.toString();
        } else {
            int rowNum;
            Row row;
            for (rowNum = firstRowNum; rowNum <= lastRowNum; ++rowNum) {
                row = sheet.getRow(rowNum);
                if (row != null) {
                    short arg28 = row.getLastCellNum();
                    if (arg28 > lastCellNum) {
                        lastCellNum = arg28;
                    }
                }
            }

            for (rowNum = firstRowNum; rowNum <= lastRowNum; ++rowNum) {
                row = sheet.getRow(rowNum);

                StringBuffer trhtml = new StringBuffer();
                trhtml.append("<tr>");
                for (short height = 0; height < lastCellNum; ++height) {
                    Cell td_rowSpanCount = null;
                    if (row != null) {
                        td_rowSpanCount = row.getCell(height);
                    }

                    if (td_rowSpanCount != null
                            && td_rowSpanCount.getCellType() != 3) {
                        isCellNull = false;
                        break;
                    }
                }

                if (row != null && !row.getZeroHeight()) {
                    if (isCellNull) {
                        ;
                    }

                    int arg29 = (int) ((double) row.getHeight() / 15.625D);
                    int arg30 = 0;
                    int td_count = 0;

                    for (short cellNum = 0; cellNum < lastCellNum; ++cellNum) {
                        Cell cell = row.getCell(cellNum);
                        boolean isHidden = sheet.isColumnHidden(cellNum);
                        StringBuffer tdHtml = new StringBuffer();
                        StringBuffer tdStyle = new StringBuffer();
                        if (!isHidden) {
                            if (cell == null) {
                                html.append("");
                            } else {
                                tdHtml.append("<td ");
                                CellStyle cellStyle = cell.getCellStyle();
                                String bgColor = null;
                                String fontColor = null;
                                if (workbook instanceof HSSFWorkbook) {
                                    HSSFPalette boldWeight = ((HSSFWorkbook) workbook)
                                            .getCustomPalette();
                                    HSSFColor fontName = boldWeight
                                            .getColor(cellStyle
                                                    .getFillForegroundColor());
                                    HSSFColor fontHeight = boldWeight
                                            .getColor(workbook.getFontAt(
                                                    cellStyle.getFontIndex())
                                                    .getColor());
                                    bgColor = convertToStardColor(fontName);
                                    fontColor = convertToStardColor(fontHeight);
                                } else if (workbook instanceof XSSFWorkbook) {
                                    XSSFColor arg31 = (XSSFColor) cellStyle
                                            .getFillForegroundColorColor();
                                    if (arg31 != null
                                            && arg31.getARGBHex() != null) {
                                        bgColor = "#"
                                                + arg31.getARGBHex().substring(
                                                2);
                                    }

                                    XSSFFont arg33 = ((XSSFCellStyle) cellStyle)
                                            .getFont();
                                    if (arg33 != null) {
                                        arg31 = arg33.getXSSFColor();
                                        if (arg31 != null
                                                && arg31.getARGBHex() != null) {
                                            fontColor = "#"
                                                    + arg31.getARGBHex()
                                                    .substring(2);
                                        }
                                    }
                                }

                                short arg32 = workbook.getFontAt(
                                        cellStyle.getFontIndex())
                                        .getBoldweight();
                                String arg34 = workbook.getFontAt(
                                        cellStyle.getFontIndex()).getFontName();
                                short arg35 = (short) (workbook.getFontAt(
                                        cellStyle.getFontIndex())
                                        .getFontHeight() / 20);
                                int cellReginCol = getMergerCellRegionCol(
                                        sheet, rowNum, cellNum);
                                int cellReginRow = getMergerCellRegionRow(
                                        sheet, rowNum, cellNum);
                                String align = convertAlignToHtml(cellStyle
                                        .getAlignment());
                                String vAlign = convertVerticalAlignToHtml(cellStyle
                                        .getVerticalAlignment());

                                if (bgColor != null
                                        && !"".equals(bgColor.trim())) {
                                    tdStyle.append(" background-color:"
                                            + bgColor + "; ");
                                }

                                if (fontColor != null
                                        && !"".equals(fontColor.trim())) {
                                    tdStyle.append(" color:" + fontColor + "; ");
                                }

                                if (arg34 != null && !"".equals(arg34.trim())
                                        && !"宋体".equals(arg34.trim())) {
                                    tdStyle.append(" font-family:" + arg34
                                            + "; ");
                                }

                                if (arg32 != 400) {
                                    tdStyle.append(" font-weight:")
                                            .append(arg32).append(";");
                                }

                                if (arg35 > 13) {
                                    tdStyle.append(" font-size: " + arg35
                                            + "px;");
                                } else {
                                    tdStyle.append(" font-size: 13px;");
                                }

                                if (tdStyle != null && tdStyle.length() > 0) {
                                    tdHtml.append(" style=\"" + tdStyle + "\" ");
                                }

                                if (!"left".equals(align)) {
                                    tdHtml.append(" align=\"" + align + "\" ");
                                }

                                if (!"center".equals(vAlign)) {
                                    tdHtml.append(" valign=\"" + vAlign + "\" ");
                                }

                                if (cellReginCol > 1) {
                                    tdHtml.append(" colspan=\"" + cellReginCol
                                            + "\" ");
                                }

                                if (cellReginRow > 1) {
                                    tdHtml.append(" rowspan=\"" + cellReginRow
                                            + "\" ");
                                    ++arg30;
                                }

                                tdHtml.append(">");
                                if (cell.getCellType() == 3) {
                                    int cellReginRowTemp = getMergerCellRegionRow(
                                            sheet, rowNum - 1, cellNum);
                                    if (cellReginRowTemp == 1) {
                                        tdHtml.append(" ");
                                        tdHtml.append("</td>");
                                        ++td_count;
                                    }
                                } else {
                                    tdHtml.append(getCellValue(cell).toString());
                                    tdHtml.append("</td>");
                                    ++td_count;

                                }
                                trhtml.append(tdHtml);

                                cellNum = (short) (cellNum + cellReginCol - 1);
                            }
                        }
                    }

                    isCellNull = true;
                    if (arg30 == td_count) {
                        html.append("");
                    }
                }
                trhtml.append("</tr>");
                html.append(trhtml);
            }

            html.append("</table>");
            return html.toString();
        }
    }

    private static Object getCellValue(Cell cell) {
        Object value = "";
        if (cell.getCellType() == 1) {
            value = cell.getRichStringCellValue().toString();
        } else if (cell.getCellType() == 0) {
            String forString = cell.getCellStyle().getDataFormatString();
            short dataFormat = cell.getCellStyle().getDataFormat();
            String regExNum = "0+_";
            String regExDateY = "y+";
            String regExDateM = "m+";
            String regExDateD = "d+";
            String regExDateH = "h+";
            boolean result = false;
            boolean resultDate = false;
            if (forString != null) {
                if (result = Pattern.compile(regExNum).matcher(forString)
                        .find()) {
                    result = true;
                } else if (Pattern.compile(regExDateY).matcher(forString)
                        .find()
                        || Pattern.compile(regExDateM).matcher(forString)
                        .find()
                        || Pattern.compile(regExDateD).matcher(forString)
                        .find()
                        || Pattern.compile(regExDateH).matcher(forString)
                        .find()) {
                    resultDate = true;
                }
            }

            if (result) {
                value = getCellNumValue(cell, value);
            } else if (resultDate) {
                Date date;
                SimpleDateFormat sdf;
                if (dataFormat != 14 && dataFormat != 176 && dataFormat != 178
                        && dataFormat != 180 && dataFormat != 181
                        && dataFormat != 182) {
                    if (dataFormat == 181) {
                        date = cell.getDateCellValue();
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        value = sdf.format(date);
                    } else if (dataFormat == 179) {
                        date = cell.getDateCellValue();
                        sdf = new SimpleDateFormat("yyyy年MM月dd日");
                        value = sdf.format(date);
                    } else if (dataFormat != 188 && dataFormat != 177) {
                        if (dataFormat == 31) {
                            date = cell.getDateCellValue();
                            sdf = new SimpleDateFormat("MM月dd日");
                            value = sdf.format(date);
                        }
                    } else {
                        date = cell.getDateCellValue();
                        sdf = new SimpleDateFormat("yyyy年MM月");
                        value = sdf.format(date);
                    }
                } else {
                    date = cell.getDateCellValue();
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    value = sdf.format(date);
                }
            } else {
                value = getCellNumValue(cell, value);
            }
        }

        if (cell.getCellType() == 3) {
            value = "";
        }

        return value;
    }

    private static Object getCellNumValue(Cell cell, Object value) {
        cell.setCellType(1);
        String tempValue = cell.getRichStringCellValue().toString();
        String value1;
        if (tempValue.indexOf(".") > -1) {
            double value_temp = Double.parseDouble(tempValue);
            if (value_temp <= 999999.0D) {
                value1 = tempValue.trim();
            } else {
                NumberFormat nf = NumberFormat.getInstance();
                nf.setGroupingUsed(false);
                value1 = nf.format(value_temp);
            }
        } else {
            value1 = tempValue.trim();
        }

        return value1;
    }

    private static int getMergerCellRegionCol(Sheet sheet, int cellRow,
                                              int cellCol) {
        int retVal = 1;
        int sheetMergerCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergerCount; ++i) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            int col = region.getLastColumn() - region.getFirstColumn() + 1;
            int firstRow = region.getFirstRow();
            int firstCol = region.getFirstColumn();
            int lastRow = region.getLastRow();
            int lastCol = region.getLastColumn();
            if (cellRow >= firstRow && cellRow <= lastRow
                    && cellCol >= firstCol && cellCol <= lastCol) {
                retVal = col;
                break;
            }
        }

        return retVal;
    }

    private static int getMergerCellRegionRow(Sheet sheet, int cellRow,
                                              int cellCol) {
        int retVal = 1;
        int sheetMergerCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergerCount; ++i) {
            CellRangeAddress region = sheet.getMergedRegion(i);
            int row = region.getLastRow() - region.getFirstRow() + 1;
            int firstRow = region.getFirstRow();
            int firstCol = region.getFirstColumn();
            int lastRow = region.getLastRow();
            int lastCol = region.getLastColumn();
            if (cellRow >= firstRow && cellRow <= lastRow
                    && cellCol >= firstCol && cellCol <= lastCol) {
                retVal = row;
                break;
            }
        }

        return retVal;
    }

    private static String convertAlignToHtml(short alignment) {
        String align = "left";
        switch (alignment) {
            case 1:
                align = "left";
                break;
            case 2:
                align = "center";
                break;
            case 3:
                align = "right";
        }

        return align;
    }

    private static String convertVerticalAlignToHtml(short verticalAlignment) {
        String valign = "middle";
        switch (verticalAlignment) {
            case 0:
                valign = "top";
                break;
            case 1:
                valign = "center";
                break;
            case 2:
                valign = "bottom";
        }

        return valign;
    }

    private static String convertToStardColor(HSSFColor hc) {
        StringBuffer sb = new StringBuffer("");
        if (hc != null) {
            byte a = 64;
            short b = hc.getIndex();
            if (a == b) {
                return null;
            }

            sb.append("#");

            for (int i = 0; i < hc.getTriplet().length; ++i) {
                String str_tmp = Integer.toHexString(hc.getTriplet()[i]);
                String str;
                if (str_tmp != null && str_tmp.length() < 2) {
                    str = "0" + str_tmp;
                } else {
                    str = str_tmp;
                }

                sb.append(str);
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        File file = new File(
                "C:\\Users\\Administrator\\Desktop\\7月第4周工作计划（投标文件）.xlsx");
        // 获取excel的sheet当做key值
        List sheetsKeys = ExcelUtil.getSheets(file);
        // 获取sheet的内容（html代码）当做value值
        Map sheetsValues = ExcelUtil.excelToHtml(file);

        Object tit = JSONObject.toJSON(sheetsKeys);
        Object value = JSONObject.toJSON(sheetsValues);

        System.out.println(sheetsValues.toString());
    }
}