package lagsit.bangsamoroconflictviewer.model;

/**
 * Created by Richard on 9/17/2016.
 */
public class GraphData {
    String level,name, ref;

    public GraphData() {
    }

    public GraphData(String level, String name, String ref) {
        this.level = level;
        this.name = name;
        this.ref = ref;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
