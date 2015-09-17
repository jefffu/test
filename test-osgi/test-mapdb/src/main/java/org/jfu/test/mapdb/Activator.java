package org.jfu.test.mapdb;

import java.io.File;
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
        DB db = DBMaker.newFileDB(new File("/tmp/git-index/commit.idx")).make();

        /*
        BTreeMap<Integer, Person> friends = db.treeMap("friends");

        NavigableSet<Object[]> id2friends = db.treeSetCreate("id2friends")
                .serializer(BTreeKeySerializer.ARRAY2).makeOrGet();

        Bind.secondaryValues(friends, id2friends, new Fun.Function2<String[], Integer, Person>() {

            @Override
            public String[] run(Integer a, Person b) {
                return b.friends.split(",");
            }
        });

        */
        BTreeMap<String, Commit> commits = db.createTreeMap("commits").make();
        NavigableSet<Object> values = db.createTreeSet("anyobjects").serializer(BTreeKeySerializer.TUPLE2).makeOrGet();

//        Bind.secondaryKeys(new Fun.Tuple2(a, b), String, Commit);
//        Bind.secondaryKeys(commits, values, new Fun.Function2<String[], String, Commit>() {
//
//            @Override
//            public String[] run(String a, Commit b) {
//                return b.getAnyobjects().toArray(new String[0]);
//            }
//
//        });
//
        context.registerService(DB.class, db, null);

    }

    @Override
    public void stop(BundleContext context) throws Exception {
        ServiceReference<DB> dbRef = context.getServiceReference(DB.class);
        DB db = context.getService(dbRef);
        db.close();

    }

}
