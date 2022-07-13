package com.zimug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@Service
public class GithubClientService {
    //前面在github中配置时产生的
    private String clientId = "b06abc14ba1cd6d24c0d";
    private String clientSecret = "1a6df8600cd87fe8f4689721db0fe42c112c92ba";
    private String state = "123";
    private String redirectUri = "http://localhost:8888/qriver-admin/login/authorization_code";

    @Autowired
    private RestTemplate restTemplate;

    @Nullable
    private WebApplicationContext webApplicationContext;

    //获取accessToken
    public Map<String, String> queryAccessToken(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("state", state);
        map.put("code", code);
        map.put("redirect_uri", redirectUri);
        Map<String, String> resp = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);
        return resp;
    }

    //获取用户信息
    public Map<String, Object> queryUser(String accessToken) {
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.add("Authorization", "token " + accessToken);
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        System.out.println("exchange.getBody() = " + exchange.getBody());
        return exchange == null ? null : exchange.getBody();
    }
}
