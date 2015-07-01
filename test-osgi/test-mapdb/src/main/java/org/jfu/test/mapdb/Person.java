package org.jfu.test.mapdb;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = -941560668355891589L;

    final int id;
    final String name;
    final String friends;
    public Person(int id, String name, String friends) {
        super();
        this.id = id;
        this.name = name;
        this.friends = friends;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getFriends() {
        return friends;
    }
    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", friends=" + friends
                + "]";
    }
}
