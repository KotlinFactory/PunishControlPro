package org.mineacademy.punishcontrol.core.converter;

public interface Converter<Source, Target> {

  Target convert(Source src);

}