package dupre.com.outerspacemanager.outerspacemanager;

/**
 * Created by sdupre on 05/03/2018.
 */

public class SimpleResponse {

    private String code;

    public void setCode(String code){ this.code = code; }
    public String getCode(){return this.code;}

    public SimpleResponse(String code){
        setCode(code);
    }
}
