package ua.challenge.oauth.grant;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.OAuthBaseTest;
import ua.challenge.oauth.response.OAuth2ResponseError;
import ua.challenge.oauth.response.OAuth2TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordGrantTest extends OAuthBaseTest {
    @Test
    @SneakyThrows
    public void postWithBasicAndBadCredentials() {
        HttpResponse<OAuth2ResponseError> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "password")
                .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(auth2Response.getError()).isEqualTo("invalid_grant");
        assertThat(auth2Response.getError_description()).isEqualTo("Bad credentials");
    }

    @Test
    @SneakyThrows
    public void postWithBasic() {
        HttpResponse<OAuth2TokenResponse> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "password")
                .field("username", "admin")
                .field("password", "123456")
                .asObject(OAuth2TokenResponse.class);

        OAuth2TokenResponse auth2Response = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(auth2Response.getAccess_token()).isNotEmpty();
        assertThat(auth2Response.getToken_type()).isEqualTo("bearer");
        assertThat(auth2Response.getRefresh_token()).isNotEmpty();
        assertThat(auth2Response.getExpires_in()).isNotZero();
        assertThat(auth2Response.getScope()).isEqualTo("read");
    }

    @Test
    @SneakyThrows
    public void postWithBasicAndRefreshToken() {
        HttpResponse<OAuth2TokenResponse> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "password")
                .field("username", "admin")
                .field("password", "123456")
                .asObject(OAuth2TokenResponse.class);

        OAuth2TokenResponse passwordResponse = response.getBody();
        assertThat(response.getStatus()).isEqualTo(200);

        String currentAccessToken = passwordResponse.getAccess_token();
        String currentRefreshToken = passwordResponse.getRefresh_token();

        response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "refresh_token")
                .field("refresh_token", currentRefreshToken)
                .asObject(OAuth2TokenResponse.class);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody().getAccess_token()).isNotEqualTo(currentAccessToken);
    }

    @Test
    @SneakyThrows
    public void postWithBasicAndInvalidRefreshToken() {
        final String DUMMY_REFRESH_TOKEN = "094b7d23-973f-4cc1-83ad-8ffd43de1845";

        HttpResponse<OAuth2ResponseError> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "refresh_token")
                .field("refresh_token", DUMMY_REFRESH_TOKEN)
                .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(auth2Response.getError()).isEqualTo("invalid_grant");
        assertThat(auth2Response.getError_description()).isEqualTo("Invalid refresh token: " + DUMMY_REFRESH_TOKEN);
    }
}
