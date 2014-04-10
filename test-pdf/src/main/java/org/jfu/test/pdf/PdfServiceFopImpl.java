package org.jfu.test.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.SAXException;

public class PdfServiceFopImpl implements PdfService {

    private FopFactory fopFactory;

    private TransformerFactory transformerFactory;

    private Transformer transformer;

    public PdfServiceFopImpl() {
        try {
            fopFactory = FopFactory.newInstance();
            fopFactory.setUserConfig(ClasspathFileHelper.getPath(
                    getClass().getClassLoader(), "org/jfu/pdf/config/fop.xconf")
                    .toFile());
            fopFactory.getFontManager().setFontBaseURL(
                    ClasspathFileHelper.getPath(getClass().getClassLoader(),
                            "org/jfu/pdf/fonts").toString());

            transformerFactory = TransformerFactory.newInstance();

            transformer = transformerFactory.newTransformer();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void generate(InputStream is, OutputStream os) {
        try {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, os);

            Source src = new StreamSource(is);

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(src, res);

        } catch (FOPException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

}
