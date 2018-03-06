package com.twg.logic;

public interface IParameterParser extends AutoCloseable {
    String parse(String name, String age);
}
