package dupre.com.outerspacemanager.outerspacemanager;

import java.util.List;

public class SearchesResponse {
    private String size;
    private List<Search> searches;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Search> getSearches() {
        return searches;
    }

    public void setSearches(List<Search> searches) {
        this.searches = searches;
    }

    public SearchesResponse(String size, List<Search> searches) {
        this.size = size;
        this.searches = searches;
    }
}
