package ua.challenge.oauth.config.oauth2.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PublicClaims {
    private String phone;
    private String user_name;
    private List<String> scope;
    private long exp;
    private List<String> authorities;
    private String jti;
    private String email;
    private String client_id;
}
