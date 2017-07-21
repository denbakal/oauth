package ua.challenge.oauth.grant;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.OAuthBaseTest;
import ua.challenge.oauth.response.OAuth2Response;
import ua.challenge.oauth.response.OAuth2ResponseError;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorizationCodeGrantTest extends OAuthBaseTest {
    /* Authorization Request (part 1) */
    @Test
    @SneakyThrows
    public void postWithBasic() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/authorize")
//                .basicAuth(CLIENT_AUTHORIZATION_CODE_ID, SECRET)
                .field("response_type", "code")
                .field("client_id", CLIENT_AUTHORIZATION_CODE_ID)
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(403);
        assertThat(auth2Response.getError()).isEqualTo("Forbidden");
        assertThat(auth2Response.getMessage()).isEqualTo("Access Denied");
    }

    /* Token Request (part 2) */
    @Test
    @SneakyThrows
    public void postWithoutParams() {
        HttpResponse<OAuth2Response> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .field("grant_type", "authorization_code")
                .asObject(OAuth2Response.class);

        OAuth2Response auth2Response = response.getBody();
        assertThat(auth2Response.getStatus()).isEqualTo(401);
        assertThat(auth2Response.getError()).isEqualTo("Unauthorized");
        assertThat(auth2Response.getMessage()).isEqualTo("Full authentication is required to access this resource");
    }

    @Test
    @SneakyThrows
    public void postWithBasicWithoutCode() {
        HttpResponse<OAuth2ResponseError> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_AUTHORIZATION_CODE_ID, SECRET)
                .field("grant_type", "authorization_code")
                .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(auth2Response.getError()).isEqualTo("invalid_request");
        assertThat(auth2Response.getError_description()).isEqualTo("An authorization code must be supplied.");
    }

    @Test
    @SneakyThrows
    public void postWithBasicInvalidCode() {
        HttpResponse<OAuth2ResponseError> response = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_AUTHORIZATION_CODE_ID, SECRET)
                .field("grant_type", "authorization_code")
                .field("code", "code")
                .field("client_id", CLIENT_AUTHORIZATION_CODE_ID)
                .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2Response = response.getBody();
        assertThat(auth2Response.getError()).isEqualTo("invalid_grant");
        assertThat(auth2Response.getError_description()).isEqualTo("Invalid authorization code: code");
    }
}
