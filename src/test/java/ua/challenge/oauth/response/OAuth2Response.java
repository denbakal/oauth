package ua.challenge.oauth.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuth2Response {
    private long timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
