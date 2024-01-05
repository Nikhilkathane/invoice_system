package invoice_system.main;

import invoice_system.service.PDFBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceSystemApplication {

    @Autowired
    private PDFBoxService pdfBoxService;

    public static void main(String[] args) {
        SpringApplication.run(InvoiceSystemApplication.class, args);
    }

}
