package come.personal.lanre.extended.cryptography;

import com.didisoft.pgp.*;

public class GenerateKeyPairRSA {

    public static void main(String[] args) throws Exception {
        // initialize the KeyStore where the key will be generated
        KeyStore ks = new KeyStore("keys/pgp20241006.keystore", "changeit");
        // key primary user Id
        String userId = "demo2@didisoft.com";
        userId = "rpsl@systemspecs.com.ng";
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
        /*ks.generateKeyPair(keySizeInBytes,
                userId,
                KeyAlgorithm.RSA,
                privateKeyPassword,
                compressions,
                hashingAlgorithms,
                cyphers);
                */
        ks.generateKeyPair(
            keySizeInBytes,
            userId,
            KeyAlgorithm.RSA,
            privateKeyPassword,
            compressionsString,
            hashingAlgorithmsString,
            cyphersString
        );

        boolean asciiArmor = false; // export public key
        ks.exportPublicKey("keys/remitapublickey20241006.key", userId, asciiArmor); // export private key
        ks.exportPrivateKey("keys/remitaprivatekey20241006.key", userId, asciiArmor); // export both keys in one file (in this case    // only ASCII armored output is accepted)
        ks.exportKeyRing("keys/remitakeypair20241006.asc", userId);
    }
}
