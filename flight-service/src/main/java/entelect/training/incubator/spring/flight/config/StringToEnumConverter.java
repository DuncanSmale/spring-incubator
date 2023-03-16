package entelect.training.incubator.spring.flight.config;

import entelect.training.incubator.spring.flight.model.SearchType;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, SearchType> {
    @Override
    public SearchType convert(String source) {
        return SearchType.valueOf(source.toUpperCase());
    }
}