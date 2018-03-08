package com.twg.logic.handler;

public interface IParameterParser extends AutoCloseable {
    public String parse(String name, String age);
}
