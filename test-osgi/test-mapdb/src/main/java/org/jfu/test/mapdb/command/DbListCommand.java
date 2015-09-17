package org.jfu.test.mapdb.command;

import java.util.Iterator;
import java.util.NavigableSet;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.mapdb.DB;
import org.mapdb.Fun;

@Command(scope = "db", name = "list")
@Service
public class DbListCommand implements Action {

    @Reference
    private DB db;

    @Argument(index=0, name="id", required=false, multiValued=false)
    String id;

    @Override
    public Object execute() throws Exception {
//        NavigableSet<Object[]> anyobjects = db.treeSet("anyobjects");
//
//        if (id == null) {
//            Iterator<Object[]> iter = anyobjects.iterator();
//            while (iter.hasNext()) {
//                Object[] objects = iter.next();
//                for (Object obj : objects) {
//                    System.out.print(obj + " ");
//                }
//                System.out.println("");
//                System.out.println("------");
//            }
//        } else {
//            for (Object[] k : Fun.filter(anyobjects, id)) {
//                String name = (String)k[1];
//                System.out.println(name);
//            }
//        }

        return null;
    }

}
