package org.jfu.test.pdf;

import java.io.InputStream;
import java.io.OutputStream;

public interface PdfService {

    void generate(InputStream is, OutputStream os);

}
