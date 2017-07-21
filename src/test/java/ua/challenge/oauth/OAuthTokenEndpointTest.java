package ua.challenge.oauth;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.response.OAuth2Response;
import ua.challenge.oauth.response.OAuth2ResponseError;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuthTokenEndpointTest extends OAuthBaseTest {
    @Test
    @SneakyThrows
    public void getOauthTokenWithoutBasic() {
        HttpResponse<OAuth2Response> response =
                Unirest.get(APPLICATION_HOST + "/oauth/token")
                        .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @SneakyThrows
    public void getOauthTokenWithBasic() {
        HttpResponse<OAuth2ResponseError> response =
                Unirest.get(APPLICATION_HOST + "/oauth/token")
                        .basicAuth(CLIENT_ID, SECRET)
                        .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(auth2Response.getError()).isEqualTo("method_not_allowed");
        assertThat(auth2Response.getError_description()).isEqualTo("Request method &#39;GET&#39; not supported");
    }

    @Test
    @SneakyThrows
    public void postOauthTokenWithBasic() {
        HttpResponse<OAuth2ResponseError> response =
                Unirest.post(APPLICATION_HOST + "/oauth/token")
                        .basicAuth(CLIENT_ID, SECRET)
                        .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(auth2Response.getError()).isEqualTo("invalid_request");
        assertThat(auth2Response.getError_description()).isEqualTo("Missing grant type");
    }
}
