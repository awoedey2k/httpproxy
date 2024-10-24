package come.personal.lanre.extended.cryptography;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptographyApplication {

    public static void main(String[] args) {
        //System.out.println("901676919675446".substring(0, 2));
        //SpringApplication.run(CryptographyApplication.class, args);
        CryptographyService cryptographyService = new CryptographyService();
        //cryptographyService.generateKeyPair("user", "password", "/Users/olanrewajuogunseye/Downloads/cryptography/kyez/public.key", "/Users/olanrewajuogunseye/Downloads/cryptography/kyez/private.key");
        cryptographyService.generateKeyPair("user", "password", "./kyez/remitaStpInstantpublic.key", "./kyez/remitaStpInstantprivate.key");
        //System.out.println(cryptographyService.decryptFile("85010C038BA788F3949180AA0107FC0D97450DC53119238FCB88C25081B76FBFD43315A3AD8CE552A894D0B600FCA8980B536A55057E4376C254B862FB022EDF01548D0D23359485C4BE99D3EAF8861CA5F83DA94DB07DAF1C97D5807B3C6912C17C852E30E20D1C76564BE6D7E58682CFCA283B1287FFF16EF72159909FB54D332C708E121C2C177907D2B5058CD6D58EC4991933480F18BEA268B5D6CC3876E6738440D6808B0AD69540973340C47E20A9DE27CFD13EF575E31BC63300D93CA59E2FBBAAC1C8E8ABF8170A799C5B734F72F10DD1AA90814E3AE7D624FABA1702EDED7F9A2C45BE6D53AAE757E154F4D776C3008003AF27178F1D474F7BF12AEB3E6840B54A41C0E4655AF1985E2EC9C0FDA071FE58BE8A75EA410B4E1C4F8F272BF02266421DCBFC41E9F9BEF77355F932BE71C94EE2E745F26E5A985CD0DA9481F2F7A133D5D7E46910544151B35A8E9B28F0AC83A7FBA1558D63BB73CB9C2EB4C697274E1C720DBF8DD0232326FDF28E8EBC8A5A77F538B0E5EF4D3D8B315518519B0C87A24CB8ECB7E9F0CC863180D7863C3C1ACE8447B279DB50BBB3B019863C26D03E082734ABFEF6F7A408EF310CCAAD2E13305CBFFFAA5870FD8D93E1333D2170A70442C61B9E43BD7A92D347CD5B2AFA4D4B7F6744C00F6CC3CCBFAE938EEC42ED4DE44D72D18FBEE60E0566A7924439903A4666B55AC53C422F52CDF08C5BD0CC820355D14F2DC4F9F98FF57DB80E5C907E619FD5815FB50B0A096E01B6C2B5C7D958F0496F80A1A635D61CF20272461FC33279485D47D13BC31B53B2119EF438F1B4D70893B12DCA206427436890F0BBBF6DFAF0264BA0FE0679184633FA2CB4A0BAC1F1031624C0F036EAA442B67E587C315DAC1B3571B06B57985FCEF5B0E1FE9511DB5AA41274AEEE5CD0FE0A861575A8F38EED644E5C469C3215D74DBEDCAB1B89D41FA9A43CFFFB43CE40C6CA0A0B62E09F3559163F049566D09093B7464BB9B7E20CA832A46A;",
        // "password", "/Users/ahmedoseni/source/remitastpinstantv2/tmp/keys/private.key"));

    }
}
