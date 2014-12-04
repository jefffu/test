package org.jfu.test.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class MyTopology {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        // Build topoloty
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("myNameSpout", new MyNameSpout(), 1);
        builder.setBolt("myCountBolt", new MyCountBolt(), 1).allGrouping("myNameSpout");
        builder.setBolt("printNameBolt", new MyPrintBolt(), 1).allGrouping("myNameSpout");
        builder.setBolt("printCountBolt", new MyPrintBolt(), 1).globalGrouping("myCountBolt");

        // Setting configurations
        Config config = new Config();
        config.setNumWorkers(2);
//        config.setDebug(true);

        // Run topology remotely
        StormSubmitter.submitTopology("mytopology", config, builder.createTopology());
    }
}
