package org.jfu.test.storm;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class MyPrintBolt extends BaseRichBolt {
    private static final long serialVersionUID = -6781649026901721143L;
    private final static Logger logger = Logger.getLogger(MyPrintBolt.class);

    private OutputCollector _collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        logger.info("--------" + input);
        _collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

}
