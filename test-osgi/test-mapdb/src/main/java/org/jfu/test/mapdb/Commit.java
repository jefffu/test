package org.jfu.test.mapdb;

import java.io.Serializable;
import java.util.List;

public class Commit implements Serializable {

    private static final long serialVersionUID = -2025108680736238241L;

    final private String id;
    final private long lastmodified;
    final private List<String> anyobjects;

    public Commit(String id, long lastmodified, List<String> anyobjects) {
        super();
        this.id = id;
        this.lastmodified = lastmodified;
        this.anyobjects = anyobjects;
    }

    public String getId() {
        return id;
    }

    public long getLastmodified() {
        return lastmodified;
    }

    public List<String> getAnyobjects() {
        return anyobjects;
    }

    @Override
    public String toString() {
        return "Commit [id=" + id + ", lastmodified=" + lastmodified
                + ", anyobjects=" + anyobjects + "]";
    }


}
