package com.ahao.util.commons.io;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.util.Date;


class ExcelHelperTest {

    @Test
    void writeAndReadTest() {
        // 1. 初始化数据
        int row = 10, col = 20;
        String[][] writeData = new String[row][col];
        for(int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                writeData[i][j] = "data["+i+"]["+j+"]";
            }
        }

        // 2. 写入Excel
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File excelFile = new File(tmpDir, "test.xlsx");
        ExcelHelper.writeSheet(writeData, excelFile);
        Assertions.assertAll("写入文件错误",
            () -> Assertions.assertTrue(excelFile.exists()),
            () -> Assertions.assertTrue(excelFile.isFile()),
            () -> Assertions.assertTrue(excelFile.length() > 10)
        );

        // 3. 读取Excel
        String[][] readData = ExcelHelper.readSheet(excelFile);
        for(int i = 0, rowLen = readData.length; i < rowLen; i++) {
            for (int j = 0, colLen = readData[i].length; j < colLen; j++) {
                Assertions.assertEquals(writeData[i][j], readData[i][j]);
            }
        }

        // 4. 删除excel
        boolean success = excelFile.delete();
        Assertions.assertTrue(success, "删除Excel失败");
    }

    @Test
    void getCellValueTest() throws Exception {
        File file = new ClassPathResource("excel/excel.xlsx").getFile();
        int sheetIndex = 0;

        try (Workbook workbook = WorkbookFactory.create(file)){
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if(sheet == null) {
                Assertions.fail();
            }

            for (int r = sheet.getFirstRowNum(), rowCount = sheet.getLastRowNum(); r < rowCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                Cell cell0 = row.getCell(0);
                Assertions.assertAll("读取第0行第0列失败",
                    () -> Assertions.assertEquals(Integer.valueOf("100"), ExcelHelper.getInteger(cell0)),
                    () -> Assertions.assertEquals(String.valueOf("100"), ExcelHelper.getString(cell0)),
                    () -> Assertions.assertEquals(Double.valueOf("100"), ExcelHelper.getDouble(cell0)),
                    () -> Assertions.assertNull(ExcelHelper.getDate(cell0, "yyyy-MM-dd"))
                );

                Cell cell1 = row.getCell(1);
                Assertions.assertAll("读取第0行第1列失败",
                    () -> Assertions.assertEquals(Integer.valueOf("100"), ExcelHelper.getInteger(cell1)),
                    () -> Assertions.assertEquals(String.valueOf("100.00"), ExcelHelper.getString(cell1)),
                    () -> Assertions.assertEquals(Double.valueOf("100.00"), ExcelHelper.getDouble(cell1)),
                    () -> Assertions.assertNull(ExcelHelper.getDate(cell1, "yyyy-MM-dd"))
                );


                Cell cell2 = row.getCell(2);
                Assertions.assertAll("读取第0行第2列失败",
                    () -> Assertions.assertNull(ExcelHelper.getInteger(cell2)),
                    () -> Assertions.assertEquals("测试数据",  ExcelHelper.getString(cell2)),
                    () -> Assertions.assertNull(ExcelHelper.getDouble(cell2)),
                    () -> Assertions.assertNull(ExcelHelper.getDate(cell2, "yyyy-MM-dd"))
                );

                Cell cell3 = row.getCell(3);
                Date expect = DateUtils.parseDate("2019-01-01", "yyyy-MM-dd");
                Assertions.assertAll("读取第0行第3列失败",
                    () -> Assertions.assertEquals(Integer.valueOf((int) expect.getTime()), ExcelHelper.getInteger(cell3)),
                    () -> Assertions.assertEquals(String.valueOf(expect.getTime()),  ExcelHelper.getString(cell3)),
                    () -> Assertions.assertEquals(Double.valueOf((double) expect.getTime()),  ExcelHelper.getDouble(cell3)),
                    () -> Assertions.assertEquals(expect, ExcelHelper.getDate(cell3, "yyyy-MM-dd"))
                );
            }
        }
    }
}
