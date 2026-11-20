package com.backendapi.service.impl;

import com.backendapi.exception.CryptoOperationException;
import com.backendapi.service.TotpService;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Service
public class TotpServiceImpl implements TotpService {
    private static final int DIGITS = 6;
    private static final int MODULO = (int) Math.pow(10, DIGITS);
    private static final Base32 BASE32_CODEC = new Base32();

    @Value("${security.totp.time-step-seconds}")
    private int timeStepSeconds;

    @Value("${security.totp.algorithm}")
    private String hmacAlgorithm;


    @Override
    public String generateTotp(String secretKey, long timeCounter) {
        try {
            byte[] keyBytes = BASE32_CODEC.decode(secretKey.trim());
            byte[] data = ByteBuffer.allocate(8).putLong(timeCounter).array();

            Mac mac = Mac.getInstance(hmacAlgorithm);
            mac.init(new SecretKeySpec(keyBytes, hmacAlgorithm));
            byte[] hash = mac.doFinal(data);

            // Dynamic Truncation (RFC 4226)
            int offset = hash[hash.length - 1] & 0x0F;
            int binaryCode = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);

            int token = binaryCode % MODULO;
            return String.format("%0" + DIGITS + "d", token);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new CryptoOperationException("Lỗi thuật toán mã hóa HMAC-SHA256", e);
        }
    }

    @Override
    public String generateCurrentTotp(String secretKey) {
        long currentCounter = Instant.now().getEpochSecond() / timeStepSeconds;
        return generateTotp(secretKey, currentCounter);
    }

    @Override
    public int getRemainingSeconds() {
        long currentSeconds = Instant.now().getEpochSecond();
        return (int) (timeStepSeconds - (currentSeconds % timeStepSeconds));
    }

    @Override
    public boolean validateTotp(String secretKey, String candidateTotp) {
        if (candidateTotp == null || !candidateTotp.matches("\\d{6}")) {
            return false;
        }

        long currentCounter = Instant.now().getEpochSecond() / timeStepSeconds;
        byte[] candidateBytes = candidateTotp.getBytes(StandardCharsets.UTF_8);
        boolean matched = false;

        for (long offset = -1; offset <= 0; offset++) {
            String validCode = generateTotp(secretKey, currentCounter + offset);
            byte[] validBytes = validCode.getBytes(StandardCharsets.UTF_8);
            matched |= MessageDigest.isEqual(validBytes, candidateBytes);
        }

        return matched;
    }
}
