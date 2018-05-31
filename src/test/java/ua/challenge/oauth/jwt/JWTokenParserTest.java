package ua.challenge.oauth.jwt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Test;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import ua.challenge.oauth.config.oauth2.jwt.PublicClaims;

public class JWTokenParserTest {
    @Test
    public void parseTokenWithClaims() {


        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IiszIDA2MyA5OTkgNzcgNzciLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwiZXhwIjoxNTA1MjU2NjM1LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6ImM5OTRmNWIzLTQxMTQtNGRkYi1iOWNkLWNmZmNjNTBmMWRkNiIsImVtYWlsIjoidGVzdEBnbWFpbC5jb20iLCJjbGllbnRfaWQiOiJjbGllbnRQYXNzd29yZElkIn0.NSZNjEAKEMuQkYUyZJ9vMOJxGPb42liVx8ZphmbgpXk";

        Jwt jwt = JwtHelper.decode(token);
        System.out.println("given token (only claims) = " + jwt.getClaims());

        Gson gson = new GsonBuilder().create();
        PublicClaims publicClaims = gson.fromJson(jwt.getClaims(), PublicClaims.class);

        Claims defaultClaims = new DefaultClaims();
        defaultClaims.put("phone", publicClaims.getPhone());
        defaultClaims.put("user_name", publicClaims.getUser_name());
        defaultClaims.put("scope", publicClaims.getScope());
        defaultClaims.put("exp", publicClaims.getExp());
        defaultClaims.put("authorities", publicClaims.getAuthorities());
        defaultClaims.put("jti", publicClaims.getJti());
        defaultClaims.put("email", publicClaims.getEmail());
        defaultClaims.put("client_id", publicClaims.getClient_id());
        defaultClaims.put("client_id2", "test!!!!!!!!!!!!!!!!####$$$$$$");

        Jwt newjwt = JwtHelper.encode(gson.toJson(defaultClaims), new MacSigner("1234567890"));
        System.out.println("newjwt = " + newjwt.getEncoded());
        System.out.println("newjwt2 = " + token.equals(newjwt.getEncoded()));

        System.out.println("new token (only claims) = " + JwtHelper.decode(newjwt.getEncoded()).getClaims());
    }

    @Test
    public void parseToken() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwaG9uZSI6IiszIDA2MyA5OTkgNzcgNzciLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiXSwiZXhwIjoxNTA1MjU1Njg2LCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjFjNGQzYmU2LWQ0MzgtNDA1OC1hMTcxLTY5ZTM4MGFkZTZjYyIsImVtYWlsIjoidGVzdEBnbWFpbC5jb20iLCJjbGllbnRfaWQiOiJjbGllbnRQYXNzd29yZElkIn0.FChmulPALKBP_2TZFbZxs7u6iyBP_TOut646I-cggas";

        /*Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("zd0R8X5XcSyk3ZNXvEbBlP2_Z0RdAYV08MhsS1iPNns"))
//                .setSigningKey("1234567890")
                .parsePlaintextJws(token);*/
//        System.out.println("body = a + body);ad

        Jwt jwt = JwtHelper.decode(token);
        System.out.println("given token (only claims) = " + jwt.getClaims());

        Gson gson = new GsonBuilder().create();
        PublicClaims publicClaims = gson.fromJson(jwt.getClaims(), PublicClaims.class);

        Claims defaultClaims = new DefaultClaims();
        defaultClaims.put("phone", publicClaims.getPhone());
        defaultClaims.put("user_name", publicClaims.getUser_name());
        defaultClaims.put("scope", publicClaims.getScope());
        defaultClaims.put("exp", publicClaims.getExp());
        defaultClaims.put("authorities", publicClaims.getAuthorities());
        defaultClaims.put("jti", publicClaims.getJti());
        defaultClaims.put("email", publicClaims.getEmail());
        defaultClaims.put("client_id", publicClaims.getClient_id());
        defaultClaims.put("client_id2", "test");

        Jwt newjwt = JwtHelper.encode(gson.toJson(defaultClaims), new MacSigner("12345678902"));
//        Jwt newjwt = JwtHelper.encode(jwt.getClaims(), new MacSigner("1234567890"));
        System.out.println("newjwt = " + newjwt.getEncoded());
        System.out.println("newjwt = " + token.equals(newjwt.getEncoded()));

        /*===========================================*/

        String newToken = Jwts.builder()
                .setClaims(defaultClaims)
                .signWith(SignatureAlgorithm.HS256, "1234567890")
                .compact();

        System.out.println("Jwts: newToken = " + newToken);

//        System.out.println("jwt = " + jwt);
//        DefaultClaims claims = (DefaultClaims) Jwts.parser().setSigningKey("1234567890").parse(newToken).getBody();
//        System.out.println("Jwts: claims = " + claims);

        /* ----- Generate Token ----- */
        String currentToken = "6ae767ba-4f51-469f-8a05-1072109bb3b8";

        Claims currentClaims = new DefaultClaims();
        currentClaims.put("uat", "4f3afa3f-3a0d-440e-951e-a12131d3cb85");
        currentClaims.put("di", "22");
        currentClaims.put("dt", "UNIQUE");

        /*String uat = Jwts.builder()
                .setSubject("ffi")
                .setClaims(currentClaims)
                .signWith(SignatureAlgorithm.HS256, "1234567890")
                .compact();*/

        Jwt uat = JwtHelper.encode(gson.toJson(currentClaims), new MacSigner("1234567890"));
        System.out.println("uat = " + uat);
        System.out.println("uat (encoded) = " + uat.getEncoded());
    }
}
