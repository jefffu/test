package org.jfu.test.storm;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.Constants;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MyCountBolt extends BaseRichBolt {
    private OutputCollector _collector;

    private Map<String, Long> counts = new HashMap<String, Long>();

    private final static Logger logger = Logger.getLogger(MyCountBolt.class);

    @Override
    public void prepare(Map stormConf, TopologyContext context,
            OutputCollector collector) {
        _collector = collector;
    }

    private void emit() {
        logger.info(">>>>>>>> emit: " + counts);
        for (Map.Entry<String, Long> entry : counts.entrySet()) {
            _collector.emit(new Values(entry.getKey(), entry.getValue()));
        }
    }

    private void countObjAndAck(Tuple input) {
        String name = input.getString(0);
        Long count = counts.get(name);
        if (count == null) {
            counts.put(name, 1L);
        } else {
            counts.put(name, count+1);
        }
        _collector.ack(input);
    }

    @Override
    public void execute(Tuple input) {
        logger.info("======== " + input);
        // It's a tick tuple
        if (input.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID)
                && input.getSourceStreamId().equals(
                        Constants.SYSTEM_TICK_STREAM_ID)) {
            emit();
        } else {
            countObjAndAck(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("name", "count"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Map<String, Object> conf = new HashMap<String, Object>();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 10);
        return conf;
    }

}
