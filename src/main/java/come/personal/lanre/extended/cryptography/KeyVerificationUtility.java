package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.KeyAlgorithm;
import com.didisoft.pgp.KeyPairInformation;
import com.didisoft.pgp.KeyStore;
import java.io.File;

public class KeyVerificationUtility {

    public static void main(String[] args) {
        try {
            // Initialize keystore
            final String REMITA_KEYSTORE = "/keys/pgp20241006.keystore";
            RemitaSmartCashCryptoUtil remitaSmartCashCryptoUtil = new RemitaSmartCashCryptoUtil();
            File keyStoreFile = remitaSmartCashCryptoUtil.getResourceAsFile(REMITA_KEYSTORE);
            KeyStore keyStore = new KeyStore(keyStoreFile.getAbsolutePath(), "changeit");

            // Print available keys
            System.out.println("Available keys in keystore:");
            KeyPairInformation[] keys = keyStore.getKeys();
            //String[] keys = keyStore.getKeys();
            for (KeyPairInformation key : keys) {
                System.out.println(
                    "Key: " +
                    key.getAlgorithm() +
                    "|" +
                    key.getFingerprint() +
                    "|" +
                    key.getCreationTime() +
                    "|" +
                    key.getKeyIDHex() +
                    "|" +
                    key.getUserIDs()
                );
            }

            // Verify specific user ID
            String userId = "rpsl@systemspecs.com.ng";
            if (keyStore.containsKey(userId)) {
                System.out.println("Found key for user: " + userId);
            } else {
                System.out.println("Key not found for user: " + userId);
            }
        } catch (Exception e) {
            System.err.println("Verification failed: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            KeyStore ks = new KeyStore("keys/pgp20241006.keystore", "changeit");
            String userId = "rpsl@systemspecs.com.ng";

            // Generate key pair
            ks.generateKeyPair(
                2048,
                userId,
                KeyAlgorithm.RSA,
                "changeit",
                "ZIP,ZLIB,UNCOMPRESSED",
                "SHA384,SHA512,MD5",
                "CAST5,AES_128,AES_192,AES_256,TWOFISH"
            );

            // Export keys
            ks.exportPublicKey("keys/remitapublickey20241006.key", userId, false);
            ks.exportPrivateKey("keys/remitaprivatekey20241006.key", userId, false);

            System.out.println("Keys generated and exported successfully!");
        } catch (Exception e) {
            System.err.println("Key generation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
