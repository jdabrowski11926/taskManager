package WAT.I8E2S4.TaskManager.security;

public class SecurityConstants {
    public static final String SECRET = "316E1EF39F54A5ECA4EDDA6B96568";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/sign-up";

    public static final String MAIL_HOST = "smtp.mailtrap.io";
    public static final int MAIL_PORT = 587;
    public static final String MAIL_USERNAME = "8c8a4871d74236";
    public static final String MAIL_PASSWORD = "c3414a0b57f18d";
}