package org.jfu.test.storm;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class ObjectEntry implements Serializable {
    private static final long serialVersionUID = -7553221498683939968L;

    private Map<String, Object> keyValues;

    public ObjectEntry(JsonParser parser) throws JsonParseException,
            IOException {
        keyValues = new HashMap<String, Object>();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = parser.getCurrentName();
            JsonToken token = parser.nextToken();
            if (token != JsonToken.START_OBJECT
                    && token != JsonToken.START_ARRAY) {
                String fieldValue = parser.getValueAsString();
                keyValues.put(fieldName, fieldValue);
            } else if (token == JsonToken.START_ARRAY) {
                keyValues.put(fieldName, new ArrayEntry(parser));
            } else {
                keyValues.put(fieldName, new ObjectEntry(parser));
            }
        }
    }

    public Map<String, Object> getKeyValues() {
        return keyValues;
    }

    @Override
    public String toString() {
        return "ObjectEntry [keyValues=" + keyValues + "]";
    }

}
