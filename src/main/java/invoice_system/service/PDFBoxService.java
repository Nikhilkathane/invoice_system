package invoice_system.service;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class PDFBoxService {

    public String extractTextFromPDF(byte[] pdfContentBytes) throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(pdfContentBytes)) {
            PDDocument document = PDDocument.load(inputStream);
            PDFTextStripper stripper = new PDFTextStripper();
            String parsedText = stripper.getText(document);
            document.close();
            return parsedText;
        }
    }
}