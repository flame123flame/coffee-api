package coffee.buy.service;

import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import coffee.buy.vo.req.SubmitYeeKeeReq;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CheckSecertDummyService {

    @Value("${cummon-secert-key.submit-dummy}")
    private String secertKeyDummy;

    public boolean iskSecertDummy(SubmitYeeKeeReq req, String secert) {
        return secert.equals(genarateSignature(req));
    }

    public String genarateSignature(SubmitYeeKeeReq req) {
        /**
         * Object "Must" String Only
         */
        ObjectMapper mapper = new ObjectMapper();
        String signature = null;

        try {
            signature = mapper.writeValueAsString(req);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        try {
            signature = getHMACSHA1Signature(signature, secertKeyDummy);
        } catch (Exception e) {
            log.error("Error Genarate Signature => ", e);
        }
        return signature;
    }

    private String getHMACSHA1Signature(String rawData, String secretKey) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] hashedValue = mac.doFinal(rawData.getBytes());
        return Base64.getEncoder().encodeToString(hashedValue);
    }
}