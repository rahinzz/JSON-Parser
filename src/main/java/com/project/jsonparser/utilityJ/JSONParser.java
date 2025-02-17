package com.project.jsonparser.utilityJ;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONParser {
	

    private int index = 0;
    private String json;

    public JSONParser(String json) {
        this.json = json.trim();
    }

    public Object parse() throws Exception {
        char firstChar = json.charAt(index);
        if (firstChar == '{') {
            return parseObject();
        } else if (firstChar == '[') {
            return parseArray();
        } else {
            throw new Exception("Invalid JSON format");
        }
    }

    private Map<String, Object> parseObject() throws Exception {
    	Map<String, Object> map = new LinkedHashMap<>();
        index++; // Skip '{'

        while (index < json.length()) {
            skipWhitespace();
            if (json.charAt(index) == '}') {
                index++; // Skip '}'
                return map;
            }

            String key = parseString();
            skipWhitespace();
            if (json.charAt(index) != ':') {
                throw new Exception("Expected ':' after key");
            }
            index++; // Skip ':'
            skipWhitespace();

            Object value = parseValue();
            map.put(key, value);

            skipWhitespace();
            if (json.charAt(index) == '}') {
                index++; // Skip '}'
                return map;
            }
            if (json.charAt(index) != ',') {
                throw new Exception("Expected ',' or '}'");
            }
            index++; // Skip ','
        }
        throw new Exception("Unclosed JSON object");
    }

    private List<Object> parseArray() throws Exception {
        List<Object> list = new ArrayList<>();
        index++; // Skip '['

        while (index < json.length()) {
            skipWhitespace();
            if (json.charAt(index) == ']') {
                index++; // Skip ']'
                return list;
            }

            list.add(parseValue());

            skipWhitespace();
            if (json.charAt(index) == ']') {
                index++; // Skip ']'
                return list;
            }
            if (json.charAt(index) != ',') {
                throw new Exception("Expected ',' or ']'");
            }
            index++; // Skip ','
        }
        throw new Exception("Unclosed JSON array");
    }

    private Object parseValue() throws Exception {
        skipWhitespace();
        char c = json.charAt(index);

        if (c == '"') return parseString();
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (Character.isDigit(c) || c == '-') return parseNumber();
        if (json.startsWith("true", index)) return consumeLiteral("true", true);
        if (json.startsWith("false", index)) return consumeLiteral("false", false);
        if (json.startsWith("null", index)) return consumeLiteral("null", null);

        throw new Exception("Unexpected character in JSON");
    }

    private String parseString() throws Exception {
        if (json.charAt(index) != '"') throw new Exception("Expected '\"' at start of string");
        index++; // Skip '"'
        StringBuilder sb = new StringBuilder();

        while (index < json.length()) {
            char c = json.charAt(index++);
            if (c == '"') return sb.toString();
            sb.append(c);
        }
        throw new Exception("Unclosed string");
    }

    private Number parseNumber() {
        int start = index;
        while (index < json.length() && (Character.isDigit(json.charAt(index)) || json.charAt(index) == '.' || json.charAt(index) == '-')) {
            index++;
        }
        return Double.parseDouble(json.substring(start, index));
    }

    private Object consumeLiteral(String literal, Object value) throws Exception {
        if (!json.startsWith(literal, index)) throw new Exception("Invalid literal: " + literal);
        index += literal.length();
        return value;
    }

    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

}