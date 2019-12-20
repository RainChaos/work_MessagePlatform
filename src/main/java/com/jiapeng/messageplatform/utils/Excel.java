package com.jiapeng.messageplatform.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.read.biff.BiffException;
import jxl.write.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.Boolean;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Excel {
    private org.apache.poi.ss.usermodel.Workbook get_workBook(String file) throws IOException {
        org.apache.poi.ss.usermodel.Workbook _workBook;
        String ext = file.substring(file.lastIndexOf(".") + 1).toLowerCase();
        try (InputStream inputStream = new FileInputStream(file)) {
            if (ext.equals("xls")) {
                _workBook = new HSSFWorkbook(inputStream);
            } else {
                _workBook = new XSSFWorkbook(inputStream);
            }
            return _workBook;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 获取电子表中的工作表
     *
     * @param file
     * @return
     */
    public List<String> getSheets(String file) throws IOException, BiffException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = get_workBook(file);
            List<String> list = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                list.add(workbook.getSheetName(i));
            }
            workbook.close();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取指定工作表的字段
     *
     * @param file
     * @param sheetName
     * @return
     */
    public List<String> getField(String file, String sheetName) throws IOException, BiffException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = get_workBook(file);
            List<String> list = new ArrayList<>();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int cols = row.getPhysicalNumberOfCells();
            for (int i = 0; i < cols; i++) {
                //getCell第一个参数是列，第二个参数是行
                row.getCell(i).setCellType(CellType.STRING);
                String colName = row.getCell(i).getStringCellValue();
                list.add(colName);
            }
            workbook.close();
            return list;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 获取指定工作表的数据
     *
     * @param file
     * @param sheetName
     * @return
     */
    public List<JsonObject> getData(String file, String sheetName) throws IOException, BiffException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = get_workBook(file);
            List<JsonObject> list = new ArrayList<>();
            List<String> colList = new ArrayList<>();

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int cols = row.getPhysicalNumberOfCells();
            int rows = sheet.getLastRowNum() + 1;

            for (int i = 0; i < cols; i++) {
                //getCell第一个参数是列，第二个参数是行
                row.getCell(i).setCellType(CellType.STRING);
                String colName = row.getCell(i).getStringCellValue();
                colList.add(colName);
            }

            for (int i = 1; i < rows; i++) {
                JsonObject jsonObject = new JsonObject();
                Row r = sheet.getRow(i);
                for (int j = 0; j < cols; j++) {
                    Cell cell = r.getCell(j);
                    if (cell != null) {
                        try {
                            switch (cell.getCellTypeEnum()) {
                                case _NONE:
                                    jsonObject.addProperty(colList.get(j), "");
                                    break;
                                case BLANK:
                                    jsonObject.addProperty(colList.get(j), "");
                                    break;
                                case BOOLEAN:
                                    jsonObject.addProperty(colList.get(j), cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    jsonObject.addProperty(colList.get(j), "");
                                    break;
                                case FORMULA:
                                    jsonObject.addProperty(colList.get(j), "");
                                    break;
                                case NUMERIC:
                                    jsonObject.addProperty(colList.get(j), formatNumericCell(cell.getNumericCellValue(), cell));
                                    break;
                                case STRING:
                                    jsonObject.addProperty(colList.get(j), cell.getStringCellValue().trim());
                                    break;
                            }
                        } catch (Exception e) {
                            jsonObject.addProperty(colList.get(j), "");
                        }
                    } else {
                        jsonObject.addProperty(colList.get(j), "");
                    }

                }
                list.add(jsonObject);
            }
            workbook.close();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取工作表数据，如未设置工作表名，则获取第一个工作表的数据
     * @param file
     * @param sheetName
     * @return
     * @throws IOException
     * @throws BiffException
     */
    public List<Map<String,Object>> getDataMap(String file,String sheetName) throws IOException, BiffException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = get_workBook(file);
            List<Map<String,Object>> list = new ArrayList<>();
            List<String> colList = new ArrayList<>();

            if(StringUtils.isBlank(sheetName)){
                sheetName = getSheets(file).get(0);
            }

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int cols = row.getPhysicalNumberOfCells();
            int rows = sheet.getLastRowNum() + 1;

            for (int i = 0; i < cols; i++) {
                //getCell第一个参数是列，第二个参数是行
                row.getCell(i).setCellType(CellType.STRING);
                String colName = row.getCell(i).getStringCellValue();
                colList.add(colName);
            }

            for (int i = 1; i < rows; i++) {
                Map<String,Object> map = new HashMap<>();
                Row r = sheet.getRow(i);
                for (int j = 0; j < cols; j++) {
                    Cell cell = r.getCell(j);
                    if (cell != null) {
                        try {
                            switch (cell.getCellTypeEnum()) {
                                case _NONE:
                                    map.put(colList.get(j),null);
                                    break;
                                case BLANK:
                                    map.put(colList.get(j),null);
                                    break;
                                case BOOLEAN:
                                    map.put(colList.get(j),cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    map.put(colList.get(j),null);
                                    break;
                                case FORMULA:
                                    map.put(colList.get(j),null);
                                    break;
                                case NUMERIC:
                                    map.put(colList.get(j),formatNumericCell(cell.getNumericCellValue(), cell));
                                    break;
                                case STRING:
                                    map.put(colList.get(j),cell.getStringCellValue().trim());
                                    break;
                            }
                        } catch (Exception e) {
                            map.put(colList.get(j),null);
                        }
                    } else {
                        map.put(colList.get(j),null);
                    }

                }
                list.add(map);
            }
            workbook.close();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取电子表中指定列的值
     *
     * @param file
     * @param sheetName
     * @param colName
     * @return
     */
    public List<String> getData(String file, String sheetName, String colName) throws IOException, BiffException {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = get_workBook(file);
            List<String> list = new ArrayList<>();

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int cols = row.getPhysicalNumberOfCells();
            int rows = sheet.getLastRowNum() + 1;
            int colIndex = 0;

            for (int i = 0; i < cols; i++) {
                //getCell第一个参数是列，第二个参数是行
                row.getCell(i).setCellType(CellType.STRING);
                String col = row.getCell(i).getStringCellValue();
                if (col.equals(colName)) {
                    colIndex = i;
                    break;
                }
            }

            for (int i = 1; i < rows; i++) {
                Row r = sheet.getRow(i);
                Cell cell = r.getCell(colIndex);
                String value = "";
                if (cell != null) {
                    try {
                        switch (cell.getCellTypeEnum()) {
                            case _NONE:
                                value = "";
                                break;
                            case BLANK:
                                value = "";
                                break;
                            case BOOLEAN:
                                value = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case ERROR:
                                value = "";
                                break;
                            case FORMULA:
                                value = "";
                                break;
                            case NUMERIC:
                                value = formatNumericCell(cell.getNumericCellValue(), cell);
                                break;
                            case STRING:
                                value = cell.getStringCellValue().trim();
                                break;
                        }
                    } catch (Exception e) {
                        value = "";
                    }
                } else {
                    value = "";
                }
                list.add(value);
            }
            workbook.close();
            return list;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 原样返回数值单元格的内容
     */
    public static String formatNumericCell(Double value, Cell cell) {
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            switch (cell.getCellStyle().getDataFormat()) {
                case 14:
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    break;
                case 22:
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
            }

            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            return sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 31) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            return sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 57) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            return sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            return sdf.format(date);
        } else {
            DataFormatter dataFormatter = new DataFormatter();
            java.text.Format format = dataFormatter.createFormat(cell);
            return format.format(value);
        }
    }

    /**
     * 写入excel
     *
     * @param fileName
     * @param list     数据，一个封装的对象列表
     * @return
     */
    public void writeData(String fileName, String sheetName, List<Object> list, Map<String, String> colMap)
            throws IOException, WriteException, IllegalAccessException, NoSuchFieldException {
        File file = new File(fileName);
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet writableSheet = wwb.createSheet(sheetName, 0);

        int rowIndex = 0;
        int colIndex = 0;
        Iterator iterator = colMap.entrySet().iterator();
        List<String> colList = new ArrayList<>();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            colList.add(entry.getValue().toString());

            WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            Label label = new Label(colIndex, rowIndex, entry.getKey().toString(), wcf);
            writableSheet.addCell(label);
            colIndex++;
        }

        rowIndex++;

        for (Object obj :
                list) {
            colIndex = 0;
            for (String str :
                    colList) {
                Field field = obj.getClass().getDeclaredField(str);
                field.setAccessible(true);
                Label label = new Label(colIndex, rowIndex, field.get(obj).toString());
                writableSheet.addCell(label);
                colIndex++;
            }
            rowIndex++;
        }

        wwb.write();
        wwb.close();
        os.flush();
        os.close();

    }

    /**
     * 写入Excel
     *
     * @param fileName
     * @param sheetName
     * @param data
     */
    public void writeData(String fileName, String sheetName, List<JsonObject> data) throws Exception {
        if (data.size() == 0) {
            throw new Exception("没有任何数据");
        }

        List<String> colList = new ArrayList<>();
        JsonObject jsonObject = data.get(0);
        Iterator iterator = jsonObject.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            colList.add(entry.getKey().toString());
        }

        File file = new File(fileName);
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet writableSheet = wwb.createSheet(sheetName, 0);

        int rowIndex = 0;
        int colIndex = 0;

        for (String str :
                colList) {
            WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            Label label = new Label(colIndex, rowIndex, str, wcf);
            writableSheet.addCell(label);
            colIndex++;
        }

        rowIndex++;

        for (JsonObject obj :
                data) {
            colIndex = 0;
            for (String str :
                    colList) {
                String value = obj.get(str).getAsString();
                Label label = new Label(colIndex, rowIndex, value);
                writableSheet.addCell(label);
                colIndex++;
            }
            rowIndex++;
        }

        wwb.write();
        wwb.close();
        os.flush();
        os.close();
    }

    /**
     * 将Map集合写入电子表
     *
     * @param fileName
     * @param sheetName
     * @param data
     * @throws Exception
     */
    public void writeMapData(String fileName, String sheetName, List<Map<String, Object>> data) throws Exception {
        if (data.size() == 0) {
            throw new Exception("没有任何数据");
        }

        List<String> colList = new ArrayList<>();
        Map<String, Object> mapCol = data.get(0);
        Iterator iterator = mapCol.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            colList.add(entry.getKey().toString());
        }

        File file = new File(fileName);
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet writableSheet = wwb.createSheet(sheetName, 0);

        int rowIndex = 0;
        int colIndex = 0;

        for (String str :
                colList) {
            WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            Label label = new Label(colIndex, rowIndex, str, wcf);
            writableSheet.addCell(label);
            colIndex++;
        }

        rowIndex++;

        for (Map<String, Object> obj :
                data) {
            colIndex = 0;
            for (String str :
                    colList) {
                String value = "";
                if (obj.get(str) != null) {
                    value = obj.get(str).toString();
                }
                Label label = new Label(colIndex, rowIndex, value);
                writableSheet.addCell(label);
                colIndex++;
            }
            rowIndex++;
        }

        wwb.write();
        wwb.close();
        os.flush();
        os.close();
    }

    /**
     * 写入电子表
     *
     * @param fileName
     * @param sheetName
     * @param title
     * @param subTitle
     * @param data
     * @throws Exception
     */
    public void writeMapData(String fileName, String sheetName, String title, String subTitle, List<Map<String, Object>> data) throws Exception {
        if (data.size() == 0) {
            throw new Exception("没有任何数据");
        }

        List<String> colList = new ArrayList<>();
        Map<String, Object> mapCol = data.get(0);
        Iterator iterator = mapCol.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            colList.add(entry.getKey().toString());
        }

        File file = new File(fileName);
        file.createNewFile();
        OutputStream os = new FileOutputStream(file);
        WritableWorkbook wwb = Workbook.createWorkbook(os);
        WritableSheet writableSheet = wwb.createSheet(sheetName, 0);

        int rowIndex = 0;
        int colIndex = 0;

        if (title != null) {
            WritableFont wf = new WritableFont(WritableFont.createFont("黑体"), 11, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            wcf.setAlignment(Alignment.CENTRE);
            Label label = new Label(colIndex, rowIndex, title, wcf);
            writableSheet.addCell(label);
            writableSheet.mergeCells(0, rowIndex, colList.size() - 1, rowIndex);
            rowIndex++;
        }
        if (subTitle != null) {
            WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            wcf.setAlignment(Alignment.CENTRE);
            Label label = new Label(colIndex, rowIndex, subTitle, wcf);
            writableSheet.addCell(label);
            writableSheet.mergeCells(0, rowIndex, colList.size() - 1, rowIndex);
            rowIndex++;
        }

        for (String str :
                colList) {
            WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false);
            WritableCellFormat wcf = new WritableCellFormat(wf);
            Label label = new Label(colIndex, rowIndex, str, wcf);
            writableSheet.addCell(label);
            colIndex++;
        }

        rowIndex++;

        for (Map<String, Object> obj :
                data) {
            colIndex = 0;
            for (String str :
                    colList) {
                String value = "";
                if (obj.get(str) != null) {
                    value = obj.get(str).toString();
                }
                Label label = new Label(colIndex, rowIndex, value);
                writableSheet.addCell(label);
                colIndex++;
            }
            rowIndex++;
        }

        wwb.write();
        wwb.close();
        os.flush();
        os.close();
    }
}
