package org.jfu.test.mapdb.command;

import java.util.Arrays;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.jfu.test.mapdb.Commit;
import org.jfu.test.mapdb.Person;
import org.mapdb.BTreeMap;
import org.mapdb.DB;

@Command(scope = "db", name = "add")
@Service
public class DbAddCommand implements Action {

    @Reference
    private DB db;

    @Argument(index=0, name="id", required=true, multiValued=false)
    String id;

    @Argument(index=1, name="lastmodified", required=true, multiValued=false)
    long lastmodified;

    @Argument(index=2, name="anyobjects", required=true, multiValued=false)
    String anyobjects;

    @Override
    public Object execute() throws Exception {


        BTreeMap<String, Commit> map = db.treeMap("commits");
        map.put(id, new Commit(id, lastmodified, Arrays.asList(anyobjects.split(","))));

        return null;
    }
    /*
    @Argument(index=0, name="id", required=true, multiValued=false)
    Integer id;

    @Argument(index=1, name="name", required=true, multiValued=false)
    String name;

    @Argument(index=2, name="friends", required=true, multiValued=false)
    String friends;

    @Override
    public Object execute() throws Exception {
        BTreeMap<Integer, Person> map = db.treeMap("friends");
        map.put(id, new Person(id, name, friends));

        return null;
    }
    */

}
