package com.twg.logic;

public interface IParameterParser extends AutoCloseable {
    public String parse(String name, String age);
}
