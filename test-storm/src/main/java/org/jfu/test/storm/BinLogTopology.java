package org.jfu.test.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class BinLogTopology {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {

        // Build topoloty
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("binLogParserSpout", new BinLogParserSpout(), 1);
        builder.setBolt("printBinLogBolt", new MyPrintBolt(), 1).allGrouping("binLogParserSpout");

        // Setting configurations
        Config config = new Config();
        config.setNumWorkers(2);
//        config.setDebug(true);

        // Run topology remotely
        StormSubmitter.submitTopology("binLogTopology", config, builder.createTopology());
    }
}
