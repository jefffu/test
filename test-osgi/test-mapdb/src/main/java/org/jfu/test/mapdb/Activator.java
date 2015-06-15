package org.jfu.test.mapdb;

import java.util.NavigableSet;

import org.mapdb.BTreeKeySerializer;
import org.mapdb.BTreeMap;
import org.mapdb.Bind;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Fun;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        DB db = DBMaker.memoryDB().make();

        BTreeMap<Integer, Person> friends = db.treeMap("friends");

        NavigableSet<Object[]> id2friends = db.treeSetCreate("id2friends")
                .serializer(BTreeKeySerializer.ARRAY2).makeOrGet();

        Bind.secondaryValues(friends, id2friends, new Fun.Function2<String[], Integer, Person>() {

            @Override
            public String[] run(Integer a, Person b) {
                return b.friends.split(",");
            }
        });

        context.registerService(DB.class, db, null);

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        ServiceReference<DB> dbRef = context.getServiceReference(DB.class);
        DB db = context.getService(dbRef);
        db.close();

    }

}
