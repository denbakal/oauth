package ua.challenge.oauth;

import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class OAuthBaseTest {
    protected static final String APPLICATION_HOST = "http://localhost:8284/demo";
    protected static final String CLIENT_ID = "sampleClientId";
    protected static final String CLIENT_CREDENTIALS_ID = "clientCredentialsId";
    protected static final String CLIENT_AUTHORIZATION_CODE_ID = "clientAuthCodeId";
    protected static final String CLIENT_PASSWORD_CODE_ID = "clientPasswordId";
    protected static final String SECRET = "secret";

    @Before
    public void init() {
        Unirest.setObjectMapper(new com.mashape.unirest.http.ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            @Override
            @SneakyThrows
            public <T> T readValue(String value, Class<T> valueType) {
                return jacksonObjectMapper.readValue(value, valueType);
            }

            @Override
            @SneakyThrows
            public String writeValue(Object value) {
                return jacksonObjectMapper.writeValueAsString(value);
            }
        });
    }
}
