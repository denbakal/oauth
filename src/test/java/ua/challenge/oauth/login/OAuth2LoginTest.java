package ua.challenge.oauth.login;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.junit.Test;
import ua.challenge.oauth.OAuthBaseTest;
import ua.challenge.oauth.response.OAuth2ResponseError;
import ua.challenge.oauth.response.OAuth2TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class OAuth2LoginTest extends OAuthBaseTest {
    @Test
    @SneakyThrows
    public void login() {
        HttpResponse<OAuth2ResponseError> responseError = Unirest.get(APPLICATION_HOST + "/users")
                .asObject(OAuth2ResponseError.class);

        OAuth2ResponseError auth2ResponseError = responseError.getBody();
        assertThat(responseError.getStatus()).isEqualTo(401);
        assertThat(auth2ResponseError.getError()).isEqualTo("unauthorized");
        assertThat(auth2ResponseError.getError_description()).isEqualTo("Full authentication is required to access this resource");

        HttpResponse<OAuth2TokenResponse> loginResponse = Unirest.post(APPLICATION_HOST + "/oauth/token")
                .basicAuth(CLIENT_PASSWORD_CODE_ID, SECRET)
                .field("grant_type", "password")
                .field("username", "admin")
                .field("password", "123456")
                .asObject(OAuth2TokenResponse.class);

        OAuth2TokenResponse auth2LoginResponse = loginResponse.getBody();
        assertThat(loginResponse.getStatus()).isEqualTo(200);
        assertThat(auth2LoginResponse.getAccess_token()).isNotEmpty();
        assertThat(auth2LoginResponse.getToken_type()).isEqualTo("bearer");
        assertThat(auth2LoginResponse.getScope()).isEqualTo("read");

        String accessToken = "Bearer " + auth2LoginResponse.getAccess_token();

        HttpResponse response = Unirest.get(APPLICATION_HOST + "/users")
                .header("Authorization", accessToken)
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @SneakyThrows
    public void loginFail() {
        HttpResponse response = Unirest.get(APPLICATION_HOST + "/")
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(200);

        response = Unirest.get(APPLICATION_HOST + "/users")
                .asBinary();

        assertThat(response.getStatus()).isEqualTo(401);
    }
}
