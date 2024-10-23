package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.*;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;

public class GenerateKeyPairRSAKeyOnly {

    public static void main(String[] args) throws PGPException {
        String keyAlgorithm = KeyAlgorithm.RSA;
        // user Id for the key pair
        String userId = "demo2@didisoft.com";
        // preferred hashing algorithms

        String[] hashingAlgorithms = new String[] {
            HashAlgorithm.SHA1,
            HashAlgorithm.SHA256,
            HashAlgorithm.SHA384,
            HashAlgorithm.SHA512,
            HashAlgorithm.MD5,
        };
        String hashingAlgorithmsString = "SHA384,SHA512,MD5";
        // preferred compression algorithms
        String[] compressions = new String[] { CompressionAlgorithm.ZIP, CompressionAlgorithm.ZLIB, CompressionAlgorithm.UNCOMPRESSED };
        String compressionsString = "ZIP,ZLIB,UNCOMPRESSED";
        // preferred symmetric key algorithms
        String[] cyphers = new String[] {
            CypherAlgorithm.CAST5,
            CypherAlgorithm.AES_128,
            CypherAlgorithm.AES_192,
            CypherAlgorithm.AES_256,
            CypherAlgorithm.TWOFISH,
        };
        String cyphersString = "CAST5,AES_128,AES_192,AES_256,TWOFISH";
        String privateKeyPassword = "changeit";
        int keySizeInBytes = 2048;
        // expiration date, pass 0 for no expiration
        long expiresAfterDays = 365;
        /*PGPKeyPair keypair = PGPKeyPair.generateKeyPair(keySizeInBytes,
                userId,
                keyAlgorithm,
                privateKeyPassword,
                compressions,
                hashingAlgorithms,
                cyphers,
                expiresAfterDays);
                */
        /* PGPKeyPair keypairks= PGPKeyPair.generateKeyPair(keySizeInBytes,
                userId,
                keyAlgorithm , //KeyAlgorithm.RSA,
                privateKeyPassword,
                compressionsString,
                hashingAlgorithmsString,
                cyphersString,
                expiresAfterDays);*/
        // keypair.export...
    }
}
