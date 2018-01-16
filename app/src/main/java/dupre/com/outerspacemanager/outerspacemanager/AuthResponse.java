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

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    private String token;
    private int expires;


    public AuthResponse(String token, int expires) {
        this.token = token;
        this.expires = expires;
    }
}
