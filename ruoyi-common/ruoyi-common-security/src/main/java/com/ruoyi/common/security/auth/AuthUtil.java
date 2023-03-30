package com.ruoyi.common.security.auth;

public class AuthUtil {
    public static AuthLogic authLogic = new AuthLogic();

    public static void logoutByToken(String token) {
        authLogic.logoutByToken(token);
    }
}
