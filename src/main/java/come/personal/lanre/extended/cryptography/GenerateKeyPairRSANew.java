package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.*;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class GenerateKeyPairRSANew {
    static {
        // Remove any existing BouncyCastle provider to avoid conflicts
        Security.removeProvider("BC");
        // Add the BouncyCastle provider
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) {
        try {
            // initialize the KeyStore where the key will be generated
            KeyStore ks = new KeyStore("pgp20241006.keystore", "changeit");

            // key primary user Id
            String userId = "rpsl@systemspecs.com.ng";

            // Set up algorithm preferences
            String hashingAlgorithmsString = "SHA384,SHA512,MD5";
            String compressionsString = "ZIP,ZLIB,UNCOMPRESSED";
            String cyphersString = "CAST5,AES_128,AES_192,AES_256,TWOFISH";

            String privateKeyPassword = "changeit";
            int keySizeInBytes = 2048;

            System.out.println("Starting key pair generation...");

            // Generate the key pair
            ks.generateKeyPair(
                keySizeInBytes,
                userId,
                KeyAlgorithm.RSA,
                privateKeyPassword,
                compressionsString,
                hashingAlgorithmsString,
                cyphersString
            );

            System.out.println("Key pair generated, exporting keys...");

            // Export the keys
            boolean asciiArmor = false;
            ks.exportPublicKey("remitapublickey20241006.key", userId, asciiArmor);
            ks.exportPrivateKey("remitaprivatekey20241006.key", userId, asciiArmor);
            ks.exportKeyRing("remitakeypair20241006.asc", userId);

            System.out.println("Keys exported successfully!");
        } catch (Exception e) {
            System.err.println("Error in key generation process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
