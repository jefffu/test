package org.jfu.test.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

public class TestPdfService {

    private PdfService pdfService = new PdfServiceFopImpl();

    private File getClasspathFile(String fileName) {
        return ClasspathFileHelper.getPath(getClass().getClassLoader(),
                "org/jfu/test/pdf/fo/" + fileName + ".fo").toFile();
    }

    @Test
    public void test() {
        String fileName = "test_table";
        genPdf(fileName);

        fileName = "first_page_special";
        genPdf(fileName);
    }

    private void genPdf(String fileName) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(getClasspathFile(fileName));

            os = new FileOutputStream("/tmp/" + fileName + ".pdf");

            pdfService.generate(is, os);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
