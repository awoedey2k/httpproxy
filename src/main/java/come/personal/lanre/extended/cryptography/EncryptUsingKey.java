package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.KeyStore;
import com.didisoft.pgp.PGPLib;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.StringTokenizer;
import org.bouncycastle.openpgp.PGPException;

public class EncryptUsingKey {

    private String encryptMessage_1(String text) {
        return encryptMessage_1(text, "/Users/olanrewajuogunseye/Downloads/cryptography/smartcashpsb_public_key_sit.key");
    }

    private String encryptMessage_1(String text, String publicKeyPath) {
        String encryptedMessage = "***Error***";
        try {
            PGPLib pgp = new PGPLib();
            PipedInputStream pin = new PipedInputStream();
            OutputStream o = new PipedOutputStream(pin);
            InputStream iStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
            InputStream publicKeyStream = new FileInputStream(publicKeyPath);
            pgp.encryptStream(iStream, publicKeyPath, publicKeyStream, o, false, false);
            while (pin.available() <= 0) {}
            byte[] body = new byte[pin.available()];
            pin.read(body);
            encryptedMessage = byte2hex(body);
        } catch (Exception var11) {
            var11.printStackTrace();
        }
        return encryptedMessage;
    }

    public String encryptMessage(String text) {
        return encryptMessage(text, "/Users/olanrewajuogunseye/Downloads/cryptography/smartcashpsb_public_key_sit.key");
    }

    public String encryptMessage(String text, String publicKeyPath) {
        try {
            String encryptedMessage = "";
            int start = 0;
            int end = 1024;
            int intDiv = text.length() / 1024;
            int divRemainder = text.length() % 1024;
            int i = 0;
            while (true) {
                if (i >= intDiv) {
                    break;
                }
                String tempEncryptedData = encryptMessage_1(text.substring(start, end), publicKeyPath);
                if (tempEncryptedData.equals("***Error***")) {
                    encryptedMessage = "";
                    break;
                }
                encryptedMessage = (encryptedMessage + tempEncryptedData) + ";";
                start = end;
                end += 1024;
                i++;
            }
            if (divRemainder > 0) {
                String tempEncryptedData2 = encryptMessage_1(text.substring(1025 * intDiv, divRemainder));
                if (!tempEncryptedData2.equals("***Error***")) {
                    encryptedMessage = encryptedMessage + tempEncryptedData2;
                }
            }
            return encryptedMessage;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            char hi = Character.forDigit((b[i] >> 4) & 15, 16);
            char lo = Character.forDigit(b[i] & 15, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public static byte[] hex2byte(String hex) {
        int len = hex.length();
        if (len % 2 != 0) {
            throw new IllegalArgumentException("Hex string must have even length");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            int high = Character.digit(hex.charAt(i), 16);
            int low = Character.digit(hex.charAt(i + 1), 16);
            if (high == -1 || low == -1) {
                throw new IllegalArgumentException("Invalid   hex character");
            }
            data[i / 2] = (byte) ((high << 4) | low);
        }
        return data;
    }

    public String decryptFile(String text, String password) {
        try {
            String decryptedMessage = "";
            StringTokenizer sToken = new StringTokenizer(text, ";");
            while (sToken.hasMoreTokens()) {
                decryptedMessage = decryptedMessage + decryptFile_1(sToken.nextToken(), password);
            }
            return decryptedMessage;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String decryptFile_1(String text, String password) {
        String decryptedMessage = "";
        byte[] bytText = hex2byte(text);
        try {
            PGPLib pgp = new PGPLib();
            InputStream iStream = new ByteArrayInputStream(bytText);
            PipedInputStream pin = new PipedInputStream();
            OutputStream oStream = new PipedOutputStream(pin);
            KeyStore keyStore = keyStore();
            keyStore.listKeys();
            InputStream privateKeyStream = new FileInputStream(
                "/Users/olanrewajuogunseye/Downloads/cryptography/remitaprivatekey20241006.key"
            );
            new FileInputStream("/Users/olanrewajuogunseye/Downloads/cryptography/remitakeypair20241006.asc");
            pgp.decryptStream(iStream, privateKeyStream, password, oStream);
            while (pin.available() <= 0) {}
            byte[] body = new byte[pin.available()];
            pin.read(body);
            decryptedMessage = new String(body).toString();
        } catch (IOException e) {} catch (PGPException e2) {
            e2.printStackTrace();
        } catch (Exception var11) {
            var11.printStackTrace();
        }
        return decryptedMessage;
    }

    public KeyStore keyStore() throws PGPException, IOException {
        KeyStore keyStore = new KeyStore("/Users/olanrewajuogunseye/Downloads/cryptography/pgp20241006.keystore", "changeit");
        return keyStore;
    }

    public static void main(String[] args) {
        EncryptUsingKey encryptUsingKey = new EncryptUsingKey();
        System.out.println(System.currentTimeMillis());
        String feedBack = encryptUsingKey.encryptMessage("Hello World");
        System.out.println(System.currentTimeMillis());
        System.out.println("Encrpted Text:" + feedBack);
        String feedBack2 = encryptUsingKey.encryptMessage(
            "Hello World",
            "/Users/olanrewajuogunseye/Downloads/cryptography/remitapublickey20241006.key"
        );
        System.out.println("Encrpted Using Remita Public Key:" + feedBack2);
        encryptUsingKey.decryptFile_1(feedBack2, "changeit");
        String feedBack3 = encryptUsingKey.encryptMessage(
            "Hello World",
            "/Users/olanrewajuogunseye/Downloads/cryptography/remitapublickey20241006.key"
        );
        System.out.println("Encrpted Using Remita Public Key:" + feedBack3);
        System.out.println(
            "Decrpted Using Remita Private Key in keystaor:" + encryptUsingKey.decryptFile_1(feedBack3, "rpsl@systemspecs.com.ng")
        );
    }
}
