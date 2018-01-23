package dupre.com.outerspacemanager.outerspacemanager;

/**
 * Created by sdupre on 16/01/2018.
 */

public class AuthResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    private String token;
    private long expires;


    public AuthResponse(String token, long expires) {
        this.token = token;
        this.expires = expires;
    }
}
