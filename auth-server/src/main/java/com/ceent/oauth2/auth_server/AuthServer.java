package com.ceent.oauth2.auth_server;

import com.ceent.oauth2.auth_server.entity.MongoAccessToken;
import com.ceent.oauth2.auth_server.entity.MongoAuthorizationCode;
import com.ceent.oauth2.auth_server.entity.MongoClientDetails;
import com.ceent.oauth2.auth_server.entity.MongoUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
public class AuthServer {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AuthServer.class, args);

        if (args .length > 0 && "init".equalsIgnoreCase(args[0])) {

            MongoTemplate mongoTemplate = context.getBean(MongoTemplate.class);

            PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
            System.out.println(passwordEncoder.encode("user"));
            System.out.println(passwordEncoder.encode("web-client"));
            System.out.println(passwordEncoder.encode("web-client-secret"));

            mongoTemplate.dropCollection(MongoUser.class);
            mongoTemplate.dropCollection(MongoClientDetails.class);
            mongoTemplate.dropCollection(MongoAccessToken.class);
            mongoTemplate.dropCollection(MongoAuthorizationCode.class);

            // init the users
            MongoUser mongoUser = new MongoUser();
            mongoUser.setUsername("user");
            mongoUser.setPassword(passwordEncoder.encode("user"));
            mongoUser.setRoles(new HashSet<>(Collections.singleton("ROLE_USER")));
            mongoTemplate.save(mongoUser);

            // init the client details
            MongoClientDetails clientDetails = new MongoClientDetails();
            clientDetails.setClientId("web-client");
            clientDetails.setClientSecret(passwordEncoder.encode("web-client-secret"));
            clientDetails.setSecretRequired(true);
            clientDetails.setResourceIds(new HashSet<>(Collections.singleton("foo")));
            clientDetails.setScope(new HashSet<>(Collections.singleton("read-foo")));
            clientDetails.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList("authorization_code", "refresh_token", "password", "client_credentials")));
            clientDetails.setRegisteredRedirectUri(new HashSet<>(Collections.singleton("http://localhost:8086/resource-service")));
            clientDetails.setAuthorities(AuthorityUtils.createAuthorityList("ROLE_USER"));
            clientDetails.setAccessTokenValiditySeconds(60);
            clientDetails.setRefreshTokenValiditySeconds(14400);
            clientDetails.setAutoApprove(false);
            mongoTemplate.save(clientDetails);

        }
    }

}
