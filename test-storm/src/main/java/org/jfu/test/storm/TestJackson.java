package org.jfu.test.storm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;

public class TestJackson {

    public static void main(String[] args) throws JsonProcessingException,
            MalformedURLException, IOException {
        URL url = new URL(
                "http://172.10.10.211:8000/changes?binlog_name="
                + "mysql3.019798"
                + "&binlog_pos=44733989"
                + "&tables=accounts|email_logging|email_opening|email_tracking");

        JsonFactory factory = new JsonFactory();
        factory.enable(Feature.ALLOW_COMMENTS);

        JsonParser parser = factory.createParser(url);

        JsonToken token = parser.nextToken();
        if (token != JsonToken.START_OBJECT) {
            System.out.println("Error, these is not START_OBJECT.");
        }
        token = parser.nextToken();
        if (token != JsonToken.END_OBJECT) {
            token = parser.nextToken();

            if (token != JsonToken.START_ARRAY) {
                System.out.println("Error, these is not START_OBJECT.");
            }
            int count = 0;
            token = parser.nextToken();
            while (token != JsonToken.END_ARRAY) {
                ObjectEntry oe = new ObjectEntry(parser);
                token = parser.nextToken();
                System.out.println(oe);
                count ++;
//                if (count > 2) {
//                    break;
//                }
            }

        }
    }
}

