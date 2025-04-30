package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;

import java.io.IOException;

public class JSONMapper {

    private final ObjectMapper objectMapper;

    public JSONMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void writeResponseAsJson(ServletResponse resp, Object responseDto) throws IOException {
        resp.setContentType("application/json");
        objectMapper.writeValue(resp.getWriter(), responseDto);
    }
}
