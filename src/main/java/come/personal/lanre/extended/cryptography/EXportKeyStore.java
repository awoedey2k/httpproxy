package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.CompressionAlgorithm;
import com.didisoft.pgp.CypherAlgorithm;
import com.didisoft.pgp.HashAlgorithm;
import com.didisoft.pgp.KeyAlgorithm;
import com.didisoft.pgp.KeyStore;
import java.io.IOException;
import org.bouncycastle.openpgp.PGPException;

public class EXportKeyStore {

    public static void main(String[] args) throws PGPException, IOException {
        int size = 32;
        int index = 50 & (size - 1);
        System.out.println("HashIndex::" + index);
        /*  System.out.println(isPalindrome("abdxcxdba"));
       int[] list = {1, 9, 4, 6};// 5,-4};

       mergeSort(list);
    System.out.println(Arrays.toString(list));
    */
        float ft = 1909.0f;
        int ints = Float.floatToIntBits(ft);
        System.out.println("ints::" + ints);
        long lg = 78900L;
        int lng = (int) (lg ^ (lg >> 32));
        System.out.println("lng::" + lng); // initialize the KeyStore where the key will be generated
        KeyStore ks = new KeyStore("pgp.keystore", "password");
        String keyAlgorithm = KeyAlgorithm.RSA; // user Id for the key pair
        String userId = "userId"; // preferred hashing algorithms
        String[] hashingAlgorithms = new String[] {
            HashAlgorithm.SHA1,
            HashAlgorithm.SHA256,
            HashAlgorithm.SHA384,
            HashAlgorithm.SHA512,
            HashAlgorithm.MD5,
        }; // preferred compression algorithms
        String[] compressions = new String[] { CompressionAlgorithm.ZIP, CompressionAlgorithm.ZLIB, CompressionAlgorithm.UNCOMPRESSED }; // preferred symmetric key algorithms
        String[] cyphers = new String[] {
            CypherAlgorithm.CAST5,
            CypherAlgorithm.AES_128,
            CypherAlgorithm.AES_192,
            CypherAlgorithm.AES_256,
            CypherAlgorithm.TWOFISH,
        };
        String privateKeyPassword = "password";
        int keySizeInBytes = 2048;
        ks.generateKeyPair(
            keySizeInBytes,
            userId,
            keyAlgorithm,
            privateKeyPassword,
            CompressionAlgorithm.UNCOMPRESSED,
            HashAlgorithm.SHA256,
            CypherAlgorithm.AES_128
        ); //export public key
        boolean asciiArmor = false; // export public key
        ks.exportPublicKey("your_public_key_sit.key", userId, asciiArmor); // export private key
        ks.exportPrivateKey("your_private_key_sit.key", userId, asciiArmor); // export both keys in one file (in this case    // only ASCII armored output is accepted)
        ks.exportKeyRing("your_keypair.asc", userId);
        //System.out.println("Hello world!");

    }
}
