package org.jfu.test.storm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser.Feature;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class BinLogParserSpout extends BaseRichSpout {
    private final static Logger logger = Logger.getLogger(BinLogParserSpout.class);
    private static final long serialVersionUID = -5500484309937888154L;

    private SpoutOutputCollector _collector;
    private LinkedBlockingQueue<ObjectEntry> queue;

    private URL url = null;

    @Override
    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) {
        _collector = collector;

        queue = new LinkedBlockingQueue<ObjectEntry>(2);
        try {
            url = new URL(
                    "http://172.10.10.211:8000/changes?binlog_name="
                            + "mysql3.019798"
                            + "&binlog_pos=44733989"
                            + "&tables=accounts|email_logging|email_opening|email_tracking");

            JsonFactory factory = new JsonFactory();
            factory.enable(Feature.ALLOW_COMMENTS);

            final JsonParser parser = factory.createParser(url);

            JsonToken token = parser.nextToken();
            if (token != JsonToken.START_OBJECT) {
                logger.info("Error, these is not START_OBJECT.");
            }
            token = parser.nextToken();
            if (token != JsonToken.END_OBJECT) {
                token = parser.nextToken();

                if (token != JsonToken.START_ARRAY) {
                    logger.info("Error, these is not START_OBJECT.");
                }
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            JsonToken token = parser.nextToken();
                            while (token != JsonToken.END_ARRAY) {
                                ObjectEntry oe = new ObjectEntry(parser);
                                while (!queue.offer(oe)) {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                token = parser.nextToken();

                                // TODO delete it
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        try {
            ObjectEntry oe = queue.poll();
            if (oe == null) {
                Thread.sleep(100);
            } else {
                _collector.emit(new Values(oe));
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("entry"));
    }

}
