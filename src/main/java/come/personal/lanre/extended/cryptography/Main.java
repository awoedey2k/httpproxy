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
}
