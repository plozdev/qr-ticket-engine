package com.backendapi.service;

public interface TotpService {
    String generateTotp(String secretKey, long timeCounter);
    String generateCurrentTotp(String secretKey);
    int getRemainingSeconds();
    boolean validateTotp(String secretKey, String candidateTotp);
}
