package come.personal.lanre.extended.cryptography;

import static come.personal.lanre.extended.cryptography.AppUtils.hex2byte;

import com.didisoft.pgp.KeyStore;
import com.didisoft.pgp.KeyStore;
import com.didisoft.pgp.PGPLib;
import com.didisoft.pgp.PGPLib;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.StringTokenizer;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Component;

@Component
public class RemitaSmartCashCryptoUtilOLd {

    private PGPLib pgp;

    private String smartcashPublicKey;
    private String remitaPublicKey;
    private String remitaKeyStore;

    public RemitaSmartCashCryptoUtilOLd() {
        initializePaths();
        try {
            loadKeys();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*try {
             smartcashPublicKey = loadResource(SMARTCASH_PUBLIC_KEY);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            remitaPublicKey = loadResource(REMITA_PUBLIC_KEY);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
             remitaKeyStore = loadResource(REMITA_KEYSTORE);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        System.out.println("smartcashPublicKey: " + smartcashPublicKey);
        System.out.println("remitaPublicKey: " + remitaPublicKey);
        System.out.println("remitaKeyStore: " + remitaKeyStore);
        /*this.pgp = new PGPLib();
        // Use non-JCE implementation
        this.pgp.setJCE(false);*/
    }

    private void initializePaths() {
        String homeDir = System.getProperty("user.home");
        Path keyDir = Paths.get(homeDir, KEY_DIRECTORY);

        smartcashPublicKey = keyDir.resolve(SMARTCASH_PUBLIC_KEY).toString();
        remitaPublicKey = keyDir.resolve(REMITA_PUBLIC_KEY).toString();
        remitaKeyStore = keyDir.resolve(REMITA_KEYSTORE).toString();
    }

    private void loadKeys() throws IOException {
        ensureDirectoryExists();
        ensureKeyExists(smartcashPublicKey);
        ensureKeyExists(remitaPublicKey);
        ensureKeyExists(remitaKeyStore);
        // Now you can use these paths to initialize your PGP library
        // For example:
        // pgp.setPublicKeyFile(smartcashPublicKeyPath);
        // ... other initialization code ...
    }

    private void ensureDirectoryExists() throws IOException {
        Path keyDir = Paths.get(System.getProperty("user.home"), KEY_DIRECTORY);
        if (!Files.exists(keyDir)) {
            Files.createDirectories(keyDir);
        }
    }

    private void ensureKeyExists(String keyPath) throws IOException {
        File keyFile = new File(keyPath);
        if (!keyFile.exists()) {
            throw new IOException("Key file not found: " + keyPath);
        }
    }

    /* private String smartcashPublicKey="/Users/olanrewajuogunseye/Downloads/PGPKeystoreGeneratorTwo/smartcashpsb_public_key_sit.key";
    private String remitaPublicKey="/Users/olanrewajuogunseye/Downloads/PGPKeystoreGeneratorTwo/remitapublickey20241006.key";
    private String remitaKeyStore="/Users/olanrewajuogunseye/Downloads/PGPKeystoreGeneratorTwo/pgp20241006.keystore";  */
    private static final String SMARTCASH_PUBLIC_KEY = "keys/smartcashpsb_public_key_sit.key";
    private static final String REMITA_PUBLIC_KEY = "keys/remitapublickey20241006.key";
    private static final String REMITA_KEYSTORE = "keys/pgp20241006.keystore";
    private static final String KEY_DIRECTORY = ".remita_keys";

    private String remitaKeyStorePassword = "changeit";

    public String encryptRequestToSmartCash(String resquest) {
        try {
            return encryptMessage(resquest, smartcashPublicKey);
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptRequestToRemita(String resquest) {
        try {
            return encryptMessage(resquest, remitaPublicKey);
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptResponseFromSmartCash(String response) {
        try {
            return decryptFile(response, remitaKeyStore, remitaKeyStorePassword);
        } catch (PGPException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encryptMessage(String text, String path) throws PGPException, IOException {
        try {
            String encryptedMessage = "";
            int start = 0;
            int len = 2048;
            int end = len;
            int intDiv = text.length() / len;
            int divRemainder = text.length() % len;
            String tempEncryptedData;
            for (int i = 0; i < intDiv; ++i) {
                tempEncryptedData = text.substring(start, end);
                tempEncryptedData = this.encryptMessage_1(tempEncryptedData, path);
                if (tempEncryptedData.equals("***Error***")) {
                    encryptedMessage = "";
                    break;
                }
                encryptedMessage = encryptedMessage + tempEncryptedData;
                encryptedMessage = encryptedMessage + ";";
                start = end;
                end += len;
            }
            if (divRemainder != 0) {
                end = start + divRemainder;
                String newText = text.substring(start, end);
                tempEncryptedData = this.encryptMessage_1(newText, path);
                if (tempEncryptedData.equals("***Error***")) {
                    encryptedMessage = "";
                } else {
                    encryptedMessage = encryptedMessage + tempEncryptedData;
                    encryptedMessage = encryptedMessage + ";";
                }
            }
            return encryptedMessage;
        } catch (Exception e) {
            throw e;
        }
    }

    private String encryptMessage_1(String text, String path) throws PGPException, IOException {
        String encryptedMessage = "***Error***";
        try {
            PGPLib pgp = new PGPLib();
            boolean armor = false;
            boolean withIntegrityCheck = false;
            PipedInputStream pin = new PipedInputStream();
            OutputStream o = new PipedOutputStream(pin);
            InputStream iStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
            InputStream publicKeyStream = new FileInputStream(path);
            // try (InputStream publicKeyStream = getResourceAsStream(path)) {

            //try (InputStream publicKeyStream = new FileInputStream(path)) {
            pgp.encryptStream(iStream, path, publicKeyStream, o, armor, withIntegrityCheck);
            while (pin.available() <= 0) {}
            byte[] body = new byte[pin.available()];
            pin.read(body);
            encryptedMessage = AppUtils.byte2hex(body);
            /*}catch(Exception e){
                e.printStackTrace();
            } */

        } catch (Exception var11) {
            var11.printStackTrace();
        }
        return encryptedMessage;
    }

    public String decryptFile(String text, String path, String password) throws PGPException, IOException {
        try {
            String decryptedMessage = "";
            for (
                StringTokenizer sToken = new StringTokenizer(text, ";");
                sToken.hasMoreTokens();
                decryptedMessage = decryptedMessage + this.decryptFile_1(sToken.nextToken(), path, password)
            ) {}
            return decryptedMessage;
        } catch (Exception e) {
            throw e;
        }
    }

    private String decryptFile_1(String text, String path, String password) throws PGPException, IOException {
        String decryptedMessage = "";
        byte[] bytText = hex2byte(text);
        //File keyStoreFile = getResourceAsFile(path);
        KeyStore keyStore = new KeyStore(path, "changeit");
        try {
            PGPLib pgp = new PGPLib();
            InputStream iStream = new ByteArrayInputStream(bytText);
            PipedInputStream pin = new PipedInputStream();
            OutputStream oStream = new PipedOutputStream(pin);
            pgp.decryptStream(iStream, keyStore, password, oStream);
            while (pin.available() <= 0) {}
            byte[] body = new byte[pin.available()];
            pin.read(body);
            decryptedMessage = (new String(body)).toString();
        } catch (Exception var11) {
            var11.printStackTrace();
        }
        return decryptedMessage;
    }

    // Method to get InputStream for a resource
    private InputStream getResourceAsStream(String resourcePath) {
        return getClass().getResourceAsStream(resourcePath);
    }

    // Method to get a temporary file for a resource
    private File getResourceAsFile(String resourcePath) throws IOException {
        InputStream in = getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    private String loadResource(String resourcePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            File tempFile = File.createTempFile("temp", ".tmp");
            tempFile.deleteOnExit();
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return tempFile.getAbsolutePath();
        }
    }
}
