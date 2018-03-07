package com.twg.logic;

public class ParameterParser implements IParameterParser {

    @Override
    public String parse(String name, String age) {
        return String.format("name: %s, age: %s", name, age);
    }

    @Override
    public void close() throws Exception {

    }
}
