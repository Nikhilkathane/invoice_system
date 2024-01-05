package invoice_system.controller;

import invoice_system.service.InvoiceProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    @Autowired
    private InvoiceProcessingService invoiceProcessingService;

    @PostMapping("/process")
    public ResponseEntity<String> processInvoiceFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            invoiceProcessingService.processAndSaveInvoice(file);
            return ResponseEntity.ok("Invoice processed and saved!");
        } catch (RuntimeException e) {
            LOGGER.error("Error processing invoice: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing invoice: " + e.getMessage());
        }
    }
}