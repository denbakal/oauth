package ua.challenge.oauth.config.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import ua.challenge.oauth.config.oauth2.jwt.CustomEnhancer;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigInMemory extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    /*
     * Defines the security constraints on the token endpoint.
     * */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        /*
        * By default Spring OAuth requires basic HTTP authentication.
        * If you want to switch it off with Java based configuration,
        * you have to allow form authentication for clients like this
        * */
//        oauthServer.allowFormAuthenticationForClients();
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /*
    *  Defines the authorization and token endpoints and the token services.
    *  Notes:
    *  1) In order to use the “password” grant type we need to wire in and use the AuthenticationManager bean
    * */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenServices(tokenServices())
                .authenticationManager(this.authenticationManager)
//                .accessTokenConverter(accessTokenConverter())
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("sampleClientId")
                    .authorizedGrantTypes("implicit")
                    .scopes("read")
                    .autoApprove(true)
                    .secret("secret")
                    .accessTokenValiditySeconds(3600)
                    .and()
                .withClient("clientCredentialsId")
                    .authorizedGrantTypes("client_credentials")
                    .scopes("read")
                    .secret("secret")
                    .accessTokenValiditySeconds(1200)
                    .and()
                .withClient("clientAuthCodeId")
                    .authorizedGrantTypes("authorization_code")
                    .scopes("read")
                    .secret("secret")
                    .accessTokenValiditySeconds(2400)
                    .and()
                .withClient("clientPasswordId")
                    .authorizedGrantTypes("password", "refresh_token")
//                    .secret("secret")
                    .scopes("read")
                    .accessTokenValiditySeconds(120)
                    .refreshTokenValiditySeconds(300)
        ;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("1234567890");
        return converter;
    }

    @Bean
    public TokenEnhancerChain tokenEnhancerChain() {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(new CustomEnhancer(), accessTokenConverter()));
        return tokenEnhancerChain;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain());
        return defaultTokenServices;
    }
}
