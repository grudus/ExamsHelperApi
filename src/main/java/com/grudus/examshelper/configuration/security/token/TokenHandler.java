package com.grudus.examshelper.configuration.security.token;


import com.grudus.examshelper.users.User;

import java.math.BigInteger;
import java.security.SecureRandom;

// TODO: 11.02.17 CHANGE ALGORITHM
public class TokenHandler {

    private SecureRandom secureRandom;

    public TokenHandler(byte[] key) {
        secureRandom = new SecureRandom(key);
    }

    public String createTokenForUser(User user)  {
        return new BigInteger(130, secureRandom).toString(32);
    }


}