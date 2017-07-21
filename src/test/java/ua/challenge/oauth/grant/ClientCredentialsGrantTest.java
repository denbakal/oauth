package ua.challenge.oauth.grant;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.OAuthBaseTest;
import ua.challenge.oauth.response.OAuth2Response;
import ua.challenge.oauth.response.OAuth2TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientCredentialsGrantTest extends OAuthBaseTest {
    @Test
    @SneakyThrows
    public void postWithoutParams() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .field("grant_type", "client_credentials")
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @SneakyThrows
    public void postWithClientId() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .field("grant_type", "client_credentials")
                .field("client_id", CLIENT_CREDENTIALS_ID)
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @SneakyThrows
    public void postWithClientIdAndSecret() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .field("grant_type", "client_credentials")
                .field("client_id", CLIENT_CREDENTIALS_ID)
                .field("client_secret", SECRET)
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @SneakyThrows
    public void postWithBasic() {
        HttpResponse<OAuth2TokenResponse> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_CREDENTIALS_ID, SECRET)
                .field("grant_type", "client_credentials")
                .asObject(OAuth2TokenResponse.class);

        OAuth2TokenResponse auth2Response = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(auth2Response.getAccess_token()).isNotEmpty();
        assertThat(auth2Response.getToken_type()).isEqualTo("bearer");
        assertThat(auth2Response.getExpires_in()).isNotZero();
        assertThat(auth2Response.getScope()).isEqualTo("read");
    }

    @Test
    @SneakyThrows
    public void postWithBasicAndBadCredentials() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_CREDENTIALS_ID, "test")
                .field("grant_type", "client_credentials")
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Bad credentials");
    }
}
