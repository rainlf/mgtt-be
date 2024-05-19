package com.rainlf.weixinmpserver.wx;

import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Base64;

/**
 * @author rain
 * @date 5/19/2024 10:38 AM
 */
public class RSA_Verify {
    public static boolean checkSignature(JsonObject ctx,JsonObject resp){
        String signature = null;
        boolean result = false;
        // 开发者本地信息
        String local_appid = ctx.get("local_appid").getAsString();
        String url_path = ctx.get("url_path").getAsString();
        String local_sym_sn = ctx.get("local_sym_sn").getAsString();
        String local_certificate = ctx.get("local_certificate").getAsString();

        long respTs = resp.get("resp_ts").getAsLong();
        String respAppid = resp.get("resp_appid").getAsString();
        String respSn = resp.get("resp_sn").getAsString();
        String respSig = resp.get("resp_sig").getAsString();
        String respDeprecatedSn = resp.get("resp_deprecated_sn").getAsString();
        String respDeprecatedSig = resp.get("resp_deprecated_sig").getAsString();
        String respData = resp.get("resp_data").getAsString();

        long localTs = System.currentTimeMillis() / 1000;
        // 安全检查，根据业务实际需求判断
        if (respAppid != local_appid || // 回包appid不正确
                localTs - respTs > 300){  // 回包时间超过5分钟
            System.out.println("安全字段校验失败");
            return result;
        }
        if(local_sym_sn ==  respSn){
            signature = respSig;
        }else if(local_sym_sn == respDeprecatedSn){
            System.out.println("平台证书即将过期，请及时更换"); // 本地证书编号与即将过期编号一致，需及时更换
            signature = respDeprecatedSig;
        }else{
            System.out.println("sn不匹配");
            return result;
        }
        String payload = url_path + "\n" + local_appid + "\n" + respTs + "\n" + respData;
        byte[] dataBuffer = payload.getBytes(StandardCharsets.UTF_8);
        try{
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(local_certificate.getBytes());
            X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            Signature verifier = Signature.getInstance("RSASSA-PSS");
            PSSParameterSpec pssParameterSpec = new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
            verifier.setParameter(pssParameterSpec);
            verifier.initVerify(certificate);
            verifier.update(dataBuffer);
            byte[] sig_buffer = Base64.getDecoder().decode(signature);
            result = verifier.verify(sig_buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private static JsonObject getCtx(){
        JsonObject ctx = new JsonObject();
        // 仅做演示，敏感信息请勿硬编码
        String localCertificate = "-----BEGIN CERTIFICATE-----\n" +
                "MIID0jCCArqgAwIBAgIUeE+Yy7vM/o+eHHsfM+1bGJJEZTQwDQYJKoZIhvcNAQEL\n" +
                "BQAwXjELMAkGA1UEBhMCQ04xEzARBgNVBAoTClRlbnBheS5jb20xHTAbBgNVBAsT\n" +
                "FFRlbnBheS5jb20gQ0EgQ2VudGVyMRswGQYDVQQDExJUZW5wYXkuY29tIFJvb3Qg\n" +
                "Q0EwHhcNMjIwOTA1MDgzOTIyWhcNMjcwOTA0MDgzOTIyWjBkMRswGQYDVQQDDBJ3\n" +
                "eGQ5MzBlYTVkNWEyNThmNGYxFTATBgNVBAoMDFRlbmNlbnQgSW5jLjEOMAwGA1UE\n" +
                "CwwFV3hnTXAxCzAJBgNVBAYMAkNOMREwDwYDVQQHDAhTaGVuWmhlbjCCASIwDQYJ\n" +
                "KoZIhvcNAQEBBQADggEPADCCAQoCggEBAM5D9qlkCmk1kr3FpF0e9pc3kGsvz5RA\n" +
                "0/YRny9xPKIyV2UVMDZvRQ+mDHsiQQFE6etg457KFYSxTDKtItbdl6hJQVGeAvg0\n" +
                "mqPYE9SkHRGTfL/AnXRbKBG2GC2OcaPSAprsLOersjay2me+9pF8VHybV8aox78A\n" +
                "NsU75G/OO3V1iEE0s5Pmglqk8DEiw9gB/dGJzsNfXwzvyJyiUP9ZujYexyjsS+/Z\n" +
                "GdSOUkqL/th+16yHj8alcdyga6YGfWEDyWkt/i/B28cwx4nzwk8xgrurifPaLuMk\n" +
                "0+9wJQLCfAn/f7zyHrC8PcD1XvvRt9VBNMBASXs3710ODyyVf2lkMgkCAwEAAaOB\n" +
                "gTB/MAkGA1UdEwQCMAAwCwYDVR0PBAQDAgTwMGUGA1UdHwReMFwwWqBYoFaGVGh0\n" +
                "dHA6Ly9ldmNhLml0cnVzLmNvbS5jbi9wdWJsaWMvaXRydXNjcmw/Q0E9MUJENDIy\n" +
                "MEU1MERCQzA0QjA2QUQzOTc1NDk4NDZDMDFDM0U4RUJEMjANBgkqhkiG9w0BAQsF\n" +
                "AAOCAQEAL2MK9tYu+ljLVBlSbfEeaKyF07TN+G31Ya5NBzeS1ZCx4joUEIyACWmG\n" +
                "fUkKNKiKV+EMzxeEhKRso1Qif3E7Ipl+PQBoQw6OSR/jFHciYurnGR9CLkL03Zo1\n" +
                "qw1Xetv9OipsvlpA0SOWc207e/XpGdm8C7FMXM6bzvVp8I/STTjC1vqjIZu9WavI\n" +
                "RgGM4jyAPz2XogUq0BNijef8BXbbav9fAsXjHSwn5BQv4iLms3fiLm/eoyQ6dZ2R\n" +
                "oTudrlcyr1bG4vwETLmHF+3yfVp9dpvJ+lyfiviwDwyfa8t2WlJm27DuF4vWoxir\n" +
                "mjgj9tDutIFqxLIovLyg3uiAYtSQ/Q==\n" +
                "-----END CERTIFICATE-----";
        ctx.addProperty("local_certificate",localCertificate);
        ctx.addProperty("local_sym_sn","79ba700ea147819f640941bceb38b1d1");
        ctx.addProperty("local_appid","wxba6223c06417af7b");
        ctx.addProperty("url_path","https://api.weixin.qq.com/wxa/getuserriskrank");
        return ctx;
    }
    private static JsonObject getResp(){
        JsonObject resp = new JsonObject();
        resp.addProperty("resp_appid","wxba6223c06417af7b");
        resp.addProperty("resp_ts",1635927956);
        resp.addProperty("resp_sn","79ba700ea147819f640941bceb38b1d1");
        resp.addProperty("resp_sig","Ht0VfQkkEweJ4hU266C14Aj64H9AXfkwNi5zxUZETCvR2svU1ZYdosDhFX/voLj1TyszqKsVxAlENGt7PPZZ8RQX7jnA4SKhiPUhW4LTbyTenisHJ+ohSfDjYnXavjQsBHspFS+BlPHuSSJ2xyQzw1+HuC6nid09ZL4FnGSYo4OI5MJrSb9xLzIVZMIDuUQchGKi/KaB1KzxECLEZcfjqbAgmxC7qOmuBLyO1WkHYDM95NJrHJWba5xv4wrwPru9yYTJSNRnlM+zrW5w9pOubC4Jtj3szTAEuOz9AcqUmgaAvMLNAIa8hfODLRe3n/cu4SgYlN/ZkNRU4QXVNbPGMg==");
        resp.addProperty("resp_deprecated_sn","2171af9cdf1d7404423852e7e183d852");
        resp.addProperty("resp_deprecated_sig","ZP1OODikAOePc+YJUMLxunF6xV05kextO/T1fy5lWv/CwV6OCsPBRM2xRRCi+B4lYXbbfYDdjzCz5BIAWEwIdjMlg/IHcJVHhRNAlKt5A3zvzfaJa5IJQel7xuUEXk/B6KVyEb41PbzrptjUGqWyTFMrjxQ4ThJfCuYocnUng7OuDU95enMqK2hZpO8o7kFW638BAwKDSiFNEwEJDWYkLz0kEw7ma3keezm4YHYKfJmjChK39tmZld7Rw/yrV1U9RiL/DO5ayP9VmrQkT/vYrPKyqI4/xKrIaTq44jFYTPIJKdU2OnLt6kjqwp2hvCzMuJdjRcrvzhWJ2A8xZ5hI2w==");
        resp.addProperty("resp_data","{\"iv\":\"r2WDQt56rEAmMuoR\",\"data\":\"HExs66Ik3el+iM4IpeQ7SMEN934FRLFYOd3EmeaIrpP4EPTHckoco6O+PaoRZRa3lqaPRZT7r52f7LUok6gLxc6cdR8C4vpIIfh4xfLC4L7FNy9GbuMK1hcoi8b7gkWJcwZMkuCFNEDmqn3T49oWzAQOrY4LZnnnykv6oUJotdAsnKvmoJkLK7hRh7M2B1d2UnTnRuoIyarXc5Iojwoghx4BOvnV\",\"authtag\":\"z2BFD8QctKXTuBlhICGOjQ==\"}");


        return resp;
    }
    public static void main(String[] args) {
        JsonObject resp = getResp();
        JsonObject ctx = getCtx();
        boolean res =  checkSignature(ctx,resp);
        System.out.println(res);
    }
}
