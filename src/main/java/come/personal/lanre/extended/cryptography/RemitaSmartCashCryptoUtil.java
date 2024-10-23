package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.KeyStore;
import com.didisoft.pgp.PGPLib;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Component;

@Component
public class RemitaSmartCashCryptoUtil {

    private static final String SMARTCASH_PUBLIC_KEY = "/keys/smartcashpsb_public_key_sit.key";
    private static final String REMITA_PUBLIC_KEY = "/keys/remitapublickey20241006.key";
    private static final String REMITA_KEYSTORE = "/keys/pgp20241006.keystore";
    private static final String KEYSTORE_PASSWORD = "changeit";

    private static final String USER_ID = "rpsl@systemspecs.com.ng"; //

    private final PGPLib pgp;
    private KeyStore keyStore;

    public RemitaSmartCashCryptoUtil() {
        this.pgp = new PGPLib();
        initializeKeyStore();
    }

    private void initializeKeyStore() {
        try {
            File keyStoreFile = getResourceAsFile(REMITA_KEYSTORE);
            this.keyStore = new KeyStore(keyStoreFile.getAbsolutePath(), KEYSTORE_PASSWORD);

            // Verify the keystore contains the necessary keys
            if (!keyStore.containsKey(USER_ID)) {
                throw new IllegalStateException("Keystore does not contain key for user: " + USER_ID);
            }
            System.out.println("Keystore initialized successfully with user: " + USER_ID);
        } catch (Exception e) {
            System.err.println("Error initializing keystore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*public String encryptRequestToSmartCash(String request) {
        try {
            return encryptMessage(request, SMARTCASH_PUBLIC_KEY);
        } catch (Exception e) {
            System.err.println("Error encrypting request to SmartCash: " + e.getMessage());
            return null;
        }
    }*/

    public String encryptRequestToSmartCash(String request) {
        try {
            File publicKeyFile = getResourceAsFile(SMARTCASH_PUBLIC_KEY);
            return encryptMessage(request, publicKeyFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error encrypting request to SmartCash: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String encryptRequestToRemita(String request) {
        try {
            File publicKeyFile = getResourceAsFile(REMITA_PUBLIC_KEY);
            return encryptMessage(request, publicKeyFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error encrypting request to Remita: " + e.getMessage());
            return null;
        }
    }

    public String decryptResponseFromSmartCash(String response) {
        try {
            if (response == null || response.trim().isEmpty()) {
                throw new IllegalArgumentException("Response cannot be null or empty");
            }
            return decryptFile(response);
        } catch (Exception e) {
            System.err.println("Error decrypting response from SmartCash: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String encryptMessage(String text, String publicKeyPath) throws Exception {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }

        StringBuilder encryptedMessage = new StringBuilder();
        int chunkSize = 2048;

        for (int i = 0; i < text.length(); i += chunkSize) {
            int end = Math.min(i + chunkSize, text.length());
            String chunk = text.substring(i, end);
            String encryptedChunk = encryptChunk(chunk, publicKeyPath);

            if (encryptedChunk == null || encryptedChunk.equals("***Error***")) {
                throw new Exception("Error encrypting chunk");
            }

            encryptedMessage.append(encryptedChunk).append(";");
        }

        return encryptedMessage.toString();
    }

    private String encryptChunkOld(String chunk, String publicKeyPath) throws Exception {
        try (
            InputStream publicKeyStream = getClass().getResourceAsStream(publicKeyPath);
            ByteArrayInputStream textStream = new ByteArrayInputStream(chunk.getBytes(StandardCharsets.UTF_8));
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream(pin)
        ) {
            if (publicKeyStream == null) {
                throw new FileNotFoundException("Public key file not found: " + publicKeyPath);
            }

            // Encrypt the chunk
            pgp.encryptStream(textStream, publicKeyPath, publicKeyStream, pout, false, false);

            // Read the encrypted data
            byte[] encryptedData = new byte[pin.available()];
            pin.read(encryptedData);
            return AppUtils.byte2hex(encryptedData);
        }
    }

    private String encryptChunk(String chunk, String publicKeyPath) throws Exception {
        try (
            FileInputStream publicKeyStream = new FileInputStream(publicKeyPath);
            ByteArrayInputStream textStream = new ByteArrayInputStream(chunk.getBytes(StandardCharsets.UTF_8));
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream(pin)
        ) {
            pgp.encryptStream(textStream, USER_ID, publicKeyStream, pout, false, false);

            // Read the encrypted data
            byte[] encryptedData = new byte[pin.available()];
            pin.read(encryptedData);
            return AppUtils.byte2hex(encryptedData);
        }
    }

    private String decryptFile(String text) throws Exception {
        if (keyStore == null) {
            throw new IllegalStateException("KeyStore not initialized");
        }

        StringBuilder decryptedMessage = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(text, ";");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String decryptedChunk = decryptChunk(token);
            decryptedMessage.append(decryptedChunk);
        }

        return decryptedMessage.toString();
    }

    private String decryptChunkOld(String encryptedHex) throws Exception {
        byte[] encryptedData = AppUtils.hex2byte(encryptedHex);

        try (
            ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedData);
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream(pin)
        ) {
            pgp.decryptStream(inputStream, keyStore, KEYSTORE_PASSWORD, pout);
            pout.close(); // Ensure all data is written

            byte[] decryptedData = new byte[pin.available()];
            pin.read(decryptedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
    }

    private String decryptChunk(String encryptedHex) throws Exception {
        byte[] encryptedData = AppUtils.hex2byte(encryptedHex);

        try (
            ByteArrayInputStream inputStream = new ByteArrayInputStream(encryptedData);
            PipedInputStream pin = new PipedInputStream();
            PipedOutputStream pout = new PipedOutputStream(pin)
        ) {
            // Print debug information
            System.out.println("Attempting to decrypt with keystore password: " + KEYSTORE_PASSWORD);
            System.out.println("Available keys in keystore: " + keyStore.getKeys());

            pgp.decryptStream(inputStream, keyStore, KEYSTORE_PASSWORD, pout);
            pout.close();

            byte[] decryptedData = new byte[pin.available()];
            pin.read(decryptedData);
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
    }

    public File getResourceAsFile(String resourcePath) throws IOException {
        InputStream in = getClass().getResourceAsStream(resourcePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        File tempFile = File.createTempFile("pgp_", ".tmp");
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

    public static void main(String[] args) {
        RemitaSmartCashCryptoUtil cryptoUtil = new RemitaSmartCashCryptoUtil();

        try {
            // Test encryption and decryption
            String originalText = "Test message";
            System.out.println("Original text: " + originalText);

            // Encrypt
            String encrypted = cryptoUtil.encryptRequestToSmartCash(originalText);
            System.out.println("Encrypted: " + encrypted);

            encrypted = cryptoUtil.encryptRequestToRemita(originalText);
            System.out.println("Encrypted to Remita: " + encrypted);

            // Decrypt
            String decrypted = cryptoUtil.decryptResponseFromSmartCash(encrypted);
            System.out.println("Decrypted: " + decrypted);

            // Verify
            if (originalText.equals(decrypted)) {
                System.out.println("Success: Encryption/Decryption cycle completed successfully!");
            } else {
                System.out.println("Error: Decrypted text doesn't match original!");
            }
        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
