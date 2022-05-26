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

      int keyLength = 128;

      AccessPermission accessPermission = new AccessPermission();

      // Disable printing, everything else is allowed
      //accessPermission.setCanAssembleDocument(true);
      //accessPermission.setCanExtractContent(true);
      //accessPermission.setCanExtractForAccessibility(true);
      //accessPermission.setCanFillInForm(true);
      //accessPermission.s


      //accessPermission.setCanPrint(false);



      // Owner password (to open the file with all permissions) is "12345"
      // User password (to open the file but with restricted permissions, is empty here)
      StandardProtectionPolicy standardProtectionPolicy = new StandardProtectionPolicy("owner", "user", accessPermission);
      standardProtectionPolicy.setEncryptionKeyLength(keyLength);
      standardProtectionPolicy.setPermissions(accessPermission);
      document.protect(standardProtectionPolicy);

      String encryptedFilePath = System.getProperty("user.dir") + File.separator + "encrypted-dummy.pdf";

      System.out.println("Writing encrypted PDF file: " + encryptedFilePath);



      document.save(encryptedFilePath);
      document.close();

          } catch (Throwable e) {
      System.err.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.err);
    }
  }
}
