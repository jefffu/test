package org.jfu.test.storm;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class ArrayEntry implements Serializable {
    private static final long serialVersionUID = 5831786603567687182L;

    private String[] values;

    public ArrayEntry(JsonParser parser) throws JsonParseException, IOException {
        List<String> list = new LinkedList<String>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            String value = parser.nextTextValue();
            list.add(value);
        }
        values = list.toArray(new String[0]);
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "ArrayEntry [values=" + Arrays.toString(values) + "]";
    }

}
