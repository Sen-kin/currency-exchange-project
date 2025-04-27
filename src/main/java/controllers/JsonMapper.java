package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonMapper {

    private static final JsonMapper INSTANCE = new JsonMapper();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonMapper() {}

    public void responseToJson(HttpServletResponse resp, Object responseDto) throws IOException {
        MAPPER.writeValue(resp.getWriter(), responseDto);
    }

    public static JsonMapper getInstance() {
        return INSTANCE;
    }
}
