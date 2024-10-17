package com.example.Gateway.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.ObjectInputFilter;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
        @Autowired
        private RouteValidator validator;



        public AuthenticationFilter() {
            super(Config.class);
        }

        public static class Config{}

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange,chain)->{
            if(validator.isSecured.test(exchange.getRequest())){


            //to checkif exchange request header contains the Authorization header
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new RuntimeException("Missing authorization Header");
            }
            //take out just the header
            String authHeaderToken = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if(authHeaderToken != null && authHeaderToken.startsWith("Bearer")){
                authHeaderToken = authHeaderToken.substring(7);
            }
            try{
                //now consume /api/auth/validate/token
                RestClient restClient = RestClient.create();
                restClient.get()
                        .uri("http://localhost:8090/api/auth/validate/token?token="+authHeaderToken)
                        .retrieve()
                        .body(Boolean.class);
            }catch(Exception e){
                System.out.println(e.getMessage());
                throw new RuntimeException("Unauthorized access:" + e.getMessage());
            }
            }
            return chain.filter(exchange);

        });
    }

}


