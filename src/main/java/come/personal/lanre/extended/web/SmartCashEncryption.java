package come.personal.lanre.extended.web;

import come.personal.lanre.extended.cryptography.RemitaSmartCashCryptoUtil;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "smartcash")
public class SmartCashEncryption {

    private final RemitaSmartCashCryptoUtil cryptoUtil;

    public SmartCashEncryption(RemitaSmartCashCryptoUtil cryptoUtil) {
        this.cryptoUtil = cryptoUtil;
    }

    @PostMapping("/encrypforremita")
    public ResponseEntity<String> encryptForRemita(@Valid @RequestBody String request) {
        String response = cryptoUtil.encryptRequestToRemita(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/encrypforsmartcash")
    public ResponseEntity<String> encryptForSmartCash(@Valid @RequestBody String request) {
        String response = cryptoUtil.encryptRequestToSmartCash(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decryptforsmartcash")
    public ResponseEntity<String> decryptForRemita(@Valid @RequestBody String request) {
        String response = cryptoUtil.decryptResponseFromSmartCash(request);
        return ResponseEntity.ok(response);
    }

    public static void main(String[] args) {
        RemitaSmartCashCryptoUtil cryptoUtil = new RemitaSmartCashCryptoUtil();
        String request = "Just Test";
        String decrypt = cryptoUtil.encryptRequestToRemita(request);
        System.out.println(decrypt);
        decrypt = cryptoUtil.decryptResponseFromSmartCash(decrypt);
        System.out.println(decrypt);
    }
}
