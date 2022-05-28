package demo.test;

import digital.inception.core.util.ResourceUtil;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

public class PDFEncryptionTest {
  public static void main(String[] args) {
    try {
      byte[] pdfBytes = ResourceUtil.getClasspathResource("dummy.pdf");

      PDDocument document = PDDocument.load(pdfBytes);

      // Enable the access permissions: CanExtractContent, CanModify and CanPrint
      AccessPermission accessPermission = new AccessPermission();
      accessPermission.setCanExtractContent(true);
      accessPermission.setCanModify(true);
      accessPermission.setCanPrint(true);

      // Use the same password for the owner and user
      // The owner password is required to change or remove security
      StandardProtectionPolicy standardProtectionPolicy =
          new StandardProtectionPolicy("Password1", "Password1", accessPermission);
      standardProtectionPolicy.setEncryptionKeyLength(256);
      standardProtectionPolicy.setPermissions(accessPermission);
      document.protect(standardProtectionPolicy);

      String encryptedFilePath =
          System.getProperty("user.dir") + File.separator + "encrypted-dummy.pdf";

      System.out.println("Writing encrypted PDF file: " + encryptedFilePath);

      document.save(encryptedFilePath);
      document.close();

    } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
