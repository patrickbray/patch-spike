package com.pat.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class JacksonConfiguration {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * Map empty string to null.
     * @return SimpleModule with a String deserializer which maps empty string to null
     */
    @Bean
    public SimpleModule simpleModule() {

        final SimpleModule module = new SimpleModule();

        module.addDeserializer(String.class, new StdDeserializer<String>(String.class) {

            @Override
            public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
                final String result = StringDeserializer.instance.deserialize(p, context);
                return StringUtils.isEmpty(result) ? null : result;
            }
        });

        objectMapper.registerModule(module);

        return module;
    }
}
