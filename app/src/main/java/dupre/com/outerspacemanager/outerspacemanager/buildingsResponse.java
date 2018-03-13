package dupre.com.outerspacemanager.outerspacemanager;

import java.util.List;

/**
 * Created by sdupre on 23/01/2018.
 */

public class buildingsResponse {
    private String size;
    private List<Building> buildings;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public buildingsResponse(String size, List<Building> buildings) {
        this.size = size;
        this.buildings = buildings;
    }
}
