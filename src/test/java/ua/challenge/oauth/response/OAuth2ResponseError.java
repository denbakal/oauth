package ua.challenge.oauth.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2ResponseError {
    private String error;
    private String error_description;
}
