package org.jfu.test.storm;

import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MyNameSpout extends BaseRichSpout {
    private final static Logger logger = Logger.getLogger(MyNameSpout.class);
    private static final long serialVersionUID = -5500484309937888154L;

    private SpoutOutputCollector _collector;

    private final String[] names = new String[] {"Jeff Fu", "Mike", "Guo"};

    private final Random random = new Random();


    public MyNameSpout() {
        random.setSeed(System.nanoTime());
    }

    @Override
    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void nextTuple() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        String name = names[random.nextInt(names.length)];
        logger.info("======== " + name);
        random.setSeed(System.nanoTime());
        _collector.emit(new Values(name));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("name"));
    }

}
