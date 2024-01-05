package invoice_system.service;

import invoice_system.model.Invoice;
import invoice_system.repo.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InvoiceProcessingService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    public void processAndSaveInvoice(MultipartFile file) {
        try {
            byte[] pdfContentBytes = file.getBytes();
            String parsedText = extractTextFromPDF(pdfContentBytes);
            System.out.println("Parsed Text: " + parsedText); // Log the parsed text

            Invoice invoice = extractInvoiceFromText(parsedText);

            if (invoice == null) {
                throw new RuntimeException("Unable to extract or incomplete invoice data from the provided file.");
            }

            if (isValidInvoice(invoice)) {
                invoiceRepository.save(invoice);
            } else {
                throw new RuntimeException("Invalid or incomplete invoice data.");
            }
        } catch (IOException e) {
            handleIOException(e);
        } catch (RuntimeException e) {
            handleRuntimeException(e);
        } catch (Exception e) {
            handleUnexpectedException(e);
        }
    }

    private void handleIOException(IOException e) {
        System.err.println("IOException occurred: " + e.getMessage());
        throw new RuntimeException("Error processing the file.", e);
    }

    private void handleRuntimeException(RuntimeException e) {
        if ("Invalid or incomplete invoice data.".equals(e.getMessage())) {
            System.err.println("Unexpected exception occurred: Invalid or incomplete invoice data.");
        } else {
            System.err.println("Unexpected exception occurred: " + e.getMessage());
        }
        throw e;
    }

    private void handleUnexpectedException(Exception e) {
        System.err.println("Unexpected exception occurred: " + e.getMessage());
        throw new RuntimeException("Unexpected error occurred.", e);
    }

    private String extractTextFromPDF(byte[] pdfContentBytes) {
        // Logic to extract text from PDF
        return "Extracted text from PDF"; // Replace this with your actual extraction logic
    }

    private Invoice extractInvoiceFromText(String parsedText) {
        Invoice invoice = new Invoice();

        try {
            invoice.setInvoiceNumber(extractInvoiceNumber(parsedText));
            invoice.setDate(extractInvoiceDate(parsedText));
            invoice.setAmount(extractInvoiceAmount(parsedText));
            invoice.setVendorName(extractVendorName(parsedText));
        } catch (Exception e) {
            System.err.println("Error during extraction: " + e.getMessage()); // Log the extraction error
            return null; // Return null in case of extraction failure
        }

        return invoice;
    }

    private String extractInvoiceNumber(String parsedText) {
        String regex = "Invoice Number:\\s*(\\w+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(parsedText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private LocalDate extractInvoiceDate(String parsedText) {
        String regex = "Invoice Date:\\s*(\\d{4}-\\d{2}-\\d{2})"; // Date format (YYYY-MM-DD)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parsedText);
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(1));
        }
        return null;
    }

    private BigDecimal extractInvoiceAmount(String parsedText) {
        String regex = "Total Amount:\\s*(\\d+\\.\\d{2})"; // Amount format (e.g., 1234.56)
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parsedText);
        if (matcher.find()) {
            return new BigDecimal(matcher.group(1));
        }
        return null;
    }

    private String extractVendorName(String parsedText) {
        String regex = "Vendor:\\s*(.+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(parsedText);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isValidInvoice(Invoice invoice) {
        return invoice != null && isValidInvoiceNumber(invoice.getInvoiceNumber()) && isValidDate(invoice.getDate()) && isValidAmount(invoice.getAmount()) && isValidVendorName(invoice.getVendorName());
    }

    private boolean isValidInvoiceNumber(String invoiceNumber) {
        return invoiceNumber != null && !invoiceNumber.isEmpty();
    }

    private boolean isValidDate(LocalDate date) {
        return date != null;
    }

    private boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isValidVendorName(String vendorName) {
        return vendorName != null && !vendorName.isEmpty();
    }
}