package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.KeyStore;
import com.didisoft.pgp.PGPLib;
import java.io.*;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.StringTokenizer;
import java.util.StringTokenizer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Component;

@Component
public class CryptographyService {

    public CryptographyService() {}

    public static void main(String[] args) {
        String ftSingleCreditRequest =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n" +
            "<FTSingleCreditRequest>\n" +
            "<SessionID>000001100913103301000000000090</SessionID>\n" +
            "<NameEnquiryRef>000001100913103301000000000089</NameEnquiryRef>\n" +
            "<DestinationInstitutionCode>000002</DestinationInstitutionCode>\n" +
            "<ChannelCode>2</ChannelCode>\n" +
            "<BeneficiaryAccountName>OJO peter </BeneficiaryAccountName>\n" +
            "<BeneficiaryAccountNumber>2222002345</BeneficiaryAccountNumber>\n" +
            "<BeneficiaryBankVerificationNumber>1033000442</BeneficiaryBankVerificationNumber>\n" +
            "<BeneficiaryKYCLevel>1</BeneficiaryKYCLevel>\n" +
            "<OriginatorAccountName>Tolu Agbo</OriginatorAccountName>\n" +
            "<OriginatorAccountNumber>3333002345</OriginatorAccountNumber>\n" +
            "<OriginatorBankVerificationNumber>1033000441</OriginatorBankVerificationNumber>\n" +
            "<OriginatorKYCLevel>1</OriginatorKYCLevel>\n" +
            "<TransactionLocation>6.4300747,3.4110715</TransactionLocation>\n" +
            "<Narration>1000000001</Narration>\n" +
            "<PaymentReference>yyyyyyyyyyyyyy</PaymentReference>\n" +
            "<Amount>1000.00</Amount>\n" +
            "</FTSingleCreditRequest>\n";

        String nameEnquiyRequest =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n" +
            "<NESingleRequest>\n" +
            "<SessionID>000001100913103301000000000001</SessionID>\n" +
            "<DestinationInstitutionCode>000002</DestinationInstitutionCode>\n" +
            "<ChannelCode>1</ChannelCode>\n" +
            "<AccountNumber>2222000000012345</AccountNumber>\n" +
            "</NESingleRequest>\n";

        String tqsQueryRequest =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
            "<TSQuerySingleRequest>\n" +
            "<SourceInstitutionCode>000002</SourceInstitutionCode>\n" +
            "<ChannelCode>1</ChannelCode>\n" +
            "<SessionID>00000110091310330100045600001</SessionID>\n" +
            "</TSQuerySingleRequest>\n";
        CryptographyService cryptographyService = new CryptographyService();

        //String response = cryptographyService.encryptMessage(ftSingleCreditRequest, "/Users/ahmedoseni/source/remitastpinstantv2/tmp/kyez/publicKey.key") ;
        cryptographyService.generateKeyPair(
            "user",
            "password",
            "/Users/ahmedoseni/source/remitastpinstantv2/tmp/kyez/public.key",
            "/Users/ahmedoseni/source/remitastpinstantv2/tmp/kyez/private.key"
        );
        //System.out.println("CryptographyService :: MessageProcessor :: encryptMessage response ..."+response);
    }

    public boolean generateKeyPair(String userId, String password, String publicKeyFileName, String privateKeyFileName) {
        boolean armor = false;
        boolean isGenerated = false;

        try {
            KeyStore keyStore = new KeyStore("pgpStpInsatntEngine.keystore", "changeit");
            keyStore.generateKeyPair(2048, userId, password);
            keyStore.exportPrivateKey(privateKeyFileName, userId, armor);
            keyStore.exportPublicKey(publicKeyFileName, userId, armor);
            System.out.println("CryptographyService :: MessageProcessor :: generateKeyPair () :::generated");

            isGenerated = true;
        } catch (Exception ex) {
            System.out.println("CryptographyService :: MessageProcessor :: generateKeyPair () :: Error Occurred ...");
            ex.printStackTrace();
        }
        return isGenerated;
    }

    public String encryptMessage(String text, String publicKeyFileName) {
        String encryptedMessage = "";
        int start = 0;
        int len = 1024;
        int end = len;
        int intDiv = text.length() / len;
        int divRemainder = text.length() % len;
        for (int i = 0; i < intDiv; i++) {
            String newText = text.substring(start, end);
            String tempEncryptedData = encryptMessage_1(newText, publicKeyFileName);
            if (tempEncryptedData.equals("***Error***")) {
                encryptedMessage = "";
                break;
            }
            encryptedMessage = encryptedMessage + tempEncryptedData;
            encryptedMessage = encryptedMessage + ";";
            start = end;
            end = start + len;
        }
        if (divRemainder != 0) {
            end = start + divRemainder;
            String newText = text.substring(start, end);
            String tempEncryptedData = encryptMessage_1(newText, publicKeyFileName);
            if (tempEncryptedData.equals("***Error***")) {
                encryptedMessage = "";
            } else {
                encryptedMessage = encryptedMessage + tempEncryptedData;
                encryptedMessage = encryptedMessage + ";";
            }
        }
        return encryptedMessage;
    }

    private String encryptMessage_1(String text, String publicKeyFileName) {
        String encryptedMessage = "***Error***";
        try {
            PGPLib pgp = new PGPLib();
            boolean armor = false;
            boolean withIntegrityCheck = false;
            PipedInputStream pin = new PipedInputStream();
            OutputStream o = new PipedOutputStream(pin);
            InputStream iStream = new ByteArrayInputStream(text.getBytes("UTF-8"));
            InputStream publicKeyStream = new FileInputStream(publicKeyFileName);
            pgp.encryptStream(iStream, publicKeyFileName, publicKeyStream, o, armor, withIntegrityCheck);
            do {} while (pin.available() <= 0);
            byte[] body = new byte[pin.available()];
            pin.read(body);
            encryptedMessage = byte2hex(body);
        } catch (Exception ex) {
            System.out.println("CryptographyService :: encryptMessage () :: Error Occurred ...");
            ex.printStackTrace();
        }
        return encryptedMessage;
    }

    public String decryptFile(String text, String password, String privateKeyFileName) {
        String decryptedMessage = "";
        StringTokenizer sToken = new StringTokenizer(text, ";");
        while (sToken.hasMoreTokens()) decryptedMessage =
            decryptedMessage + decryptFile_1(sToken.nextToken(), password, privateKeyFileName);
        return decryptedMessage;
    }

    private String decryptFile_1(String text, String password, String privateKeyFileName) {
        String decryptedMessage = "";
        byte[] bytText = hex2byte(text);
        try {
            PGPLib pgp = new PGPLib();
            InputStream iStream = new ByteArrayInputStream(bytText);
            InputStream privateKeyStream = new FileInputStream(privateKeyFileName);
            PipedInputStream pin = new PipedInputStream();
            OutputStream oStream = new PipedOutputStream(pin);
            pgp.decryptStream(iStream, privateKeyStream, password, oStream);
            do {} while (pin.available() <= 0);
            byte[] body = new byte[pin.available()];
            pin.read(body);
            decryptedMessage = (new String(body)).toString();
        } catch (Exception ex) {
            System.out.println("CryptographyService :: decryptFile () :: Error Occurred ...");
            ex.printStackTrace();
        }
        return decryptedMessage;
    }

    public String byte2hex(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            char hi = Character.forDigit(b[i] >> 4 & 0xF, 16);
            char lo = Character.forDigit(b[i] & 0xF, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; i++) {
            int shift = (i % 2 == 1) ? 0 : 4;
            d[i >> 1] = (byte) (d[i >> 1] | Character.digit((char) b[offset + i], 16) << shift);
        }
        return d;
    }

    public byte[] hex2byte(String s) {
        return hex2byte(s.getBytes(), 0, s.length() >> 1);
    }
}
