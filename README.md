# oauth

### Upgrade to 2.1.0.RELEASE

#### Request with grant types

##### client_credentials

POST http://localhost:8087/demo/oauth/token?grant_type=client_credentials
Accept: */*
Cache-Control: no-cache
Authorization: Basic Y2xpZW50Q3JlZGVudGlhbHNJZDpzZWNyZXQ=

##### password
POST http://localhost:8087/demo/oauth/token?grant_type=password&client_id=clientPasswordId&client_secret=secret&username=admin&password=123456
Accept: */*
Cache-Control: no-cache

###

GET http://localhost:8087/spring-security-oauth/users
Accept: */*
Cache-Control: no-cache