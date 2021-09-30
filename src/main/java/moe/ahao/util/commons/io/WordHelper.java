package moe.ahao.util.commons.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;

public class WordHelper {
    public static void write(String text, String path) {
        try (XWPFDocument doc = new XWPFDocument();
             FileOutputStream out = new FileOutputStream(path)) {

            String[] rows = StringUtils.split(text, "\n");

            for (String row : rows) {
                XWPFParagraph p = doc.createParagraph();
                XWPFRun r = p.createRun();
                r.setText(row);
            }
            doc.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String path) throws FileNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("文件路径不存在, " + path);
        }
        try (InputStream in = new FileInputStream(file);) {
            if (StringUtils.endsWithIgnoreCase(file.getName(), ".doc")) {
                try (HWPFDocument doc = new HWPFDocument(in);
                     WordExtractor docEx = new WordExtractor(doc);) {
                    return docEx.getText();
                }
            } else {
                try (XWPFDocument doc = new XWPFDocument(in);
                     XWPFWordExtractor docEx = new XWPFWordExtractor(doc);) {
                    return docEx.getText();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
