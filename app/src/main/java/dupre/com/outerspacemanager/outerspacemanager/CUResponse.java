package dupre.com.outerspacemanager.outerspacemanager;

/**
 * Created by sdupre on 23/01/2018.
 */

public class CUResponse {
    private String gas;
    private String gasModifier;
    private String mineralModifier;
    private String points;
    private String username;
    private String minerals;

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getGasModifier() {
        return gasModifier;
    }

    public void setGasModifier(String gasModifier) {
        this.gasModifier = gasModifier;
    }

    public String getMinerals() {
        return minerals;
    }

    public void setMinerals(String minerals) {
        this.minerals = minerals;
    }

    public String getMineralModifier() {
        return mineralModifier;
    }

    public void setMineralModifier(String mineralModifier) {
        this.mineralModifier = mineralModifier;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CUResponse(String gas, String gasModifier, String minerals, String mineralModifier, String points, String username) {
        this.gas = gas;
        this.gasModifier = gasModifier;
        this.minerals = minerals;
        this.mineralModifier = mineralModifier;
        this.points = points;
        this.username = username;
    }
}
