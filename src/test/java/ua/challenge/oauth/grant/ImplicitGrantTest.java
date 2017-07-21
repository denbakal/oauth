package ua.challenge.oauth.grant;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.OAuthBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ImplicitGrantTest extends OAuthBaseTest {
    @Test
    @SneakyThrows
    public void postWithoutParams() {
        HttpResponse response =
                Unirest.post(APPLICATION_HOST + "/oauth/authorize")
                        .asBinary();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @SneakyThrows
    public void postWithoutClientId() {
        HttpResponse response = Unirest.post(APPLICATION_HOST + "/oauth/authorize")
                .field("response_type", "token")
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(401);
    }

    @Test
    @SneakyThrows
    public void post() {
        /*
        * User must be authenticated with Spring Security before authorization can be completed.
        * */
        HttpResponse response = Unirest.post(APPLICATION_HOST + "/oauth/authorize")
                .field("response_type", "token")
                .field("client_id", CLIENT_ID)
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(403);
    }

    @Test
    @SneakyThrows
    public void postWithBasic() {
        HttpResponse response = Unirest.post(APPLICATION_HOST + "/oauth/authorize")
                .basicAuth(CLIENT_ID, SECRET)
                .field("response_type", "token")
                .field("client_id", CLIENT_ID)
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(403);
    }
}
