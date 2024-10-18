package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.CompressionAlgorithm;
import com.didisoft.pgp.CypherAlgorithm;
import com.didisoft.pgp.HashAlgorithm;
import com.didisoft.pgp.KeyAlgorithm;
import com.didisoft.pgp.KeyStore;
import java.io.IOException;
import java.security.Security;
import java.util.Scanner;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;

public class Main {
    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
            //System.out.println("Addding BouncyCastleProvider .........................................");
        }
    }

    public static void mainOld(String[] args) throws PGPException, IOException {
        String encrypted = null;
        String decrypted = null;
        RemitaSmartCashCryptoUtil remitaSmartCashCryptoUtil = new RemitaSmartCashCryptoUtil();
        String test = "Text to SmartCash cash";
        encrypted = remitaSmartCashCryptoUtil.encryptRequestToSmartCash(test);
        System.out.println("TEXT TO SmartCash: " + encrypted);
        test = "Text from SmartCash cash";
        encrypted = remitaSmartCashCryptoUtil.encryptRequestToRemita(test);
        System.out.println("TEXT FROM SmartCash: " + encrypted);
        decrypted = remitaSmartCashCryptoUtil.decryptResponseFromSmartCash(encrypted);
        System.out.println("DECRYPTED TEXT FROM SmartCash: " + decrypted);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RemitaSmartCashCryptoUtil cryptoUtil = new RemitaSmartCashCryptoUtil();
        String action, input, result;

        while (true) {
            System.out.println("\nEnter the action you want to perform:");
            System.out.println("1. encryptRequestToSmartCash");
            System.out.println("2. encryptRequestToRemita");
            System.out.println("3. decryptResponseFromSmartCash");
            System.out.println("Type 'END' to exit the program");

            action = scanner.nextLine().trim();

            if (action.equalsIgnoreCase("END")) {
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            System.out.println("Enter the text to process:");
            input = scanner.nextLine();

            try {
                switch (action) {
                    case "1":
                        result = cryptoUtil.encryptRequestToSmartCash(input);
                        System.out.println("Encrypted text to SmartCash: " + result);
                        break;
                    case "2":
                        result = cryptoUtil.encryptRequestToRemita(input);
                        System.out.println("Encrypted text to Remita: " + result);
                        break;
                    case "3":
                        result = cryptoUtil.decryptResponseFromSmartCash(input);
                        System.out.println("Decrypted text from SmartCash: " + result);
                        break;
                    default:
                        System.out.println("Invalid action. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }
    /*
    public static void main(String[] args) throws PGPException, IOException {

        // initialize the KeyStore where the key will be generated
        KeyStore ks = new KeyStore("pgp.keystore", "passwords");

        String keyAlgorithm = KeyAlgorithm.RSA;

        // user Id for the key pair
        String userId = "userId";

        // preferred hashing algorithms
        String[] hashingAlgorithms = new String[]
                {HashAlgorithm.SHA1,
                        HashAlgorithm.SHA256,
                        HashAlgorithm.SHA384,
                        HashAlgorithm.SHA512,
                        HashAlgorithm.MD5};

        // preferred compression algorithms
        String[] compressions = new String[]
                {CompressionAlgorithm.ZIP,
                        CompressionAlgorithm.ZLIB,
                        CompressionAlgorithm.UNCOMPRESSED};

        // preferred symmetric key algorithms
        String[] cyphers = new String[]
                {CypherAlgorithm.CAST5,
                        CypherAlgorithm.AES_128,
                        CypherAlgorithm.AES_192,
                        CypherAlgorithm.AES_256,
                        CypherAlgorithm.TWOFISH};

        String privateKeyPassword = "passwords";

        int keySizeInBytes = 1024;
        ks.generateKeyPair(keySizeInBytes,
                userId,
                keyAlgorithm,
                privateKeyPassword,
                CompressionAlgorithm.UNCOMPRESSED,
                HashAlgorithm.SHA256,
                CypherAlgorithm.AES_128);

        //export public key
        boolean asciiArmor = false;
        // export public key
        ks.exportPublicKey("smartcash_public_key_sit.key", userId, asciiArmor);
        // export private key
        ks.exportPrivateKey("smartcash_private_key_sit.key", userId, asciiArmor);
        // export both keys in one file (in this case
        // only ASCII armored output is accepted)
        ks.exportKeyRing("smartcash_keypair.asc", userId);

        //System.out.println("Hello world!");
    }
     */

}
