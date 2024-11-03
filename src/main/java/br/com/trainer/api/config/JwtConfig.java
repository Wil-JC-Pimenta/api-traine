package br.com.trainer.api.config;

<<<<<<< HEAD
public class JwtConfig {
=======
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;

@Configuration
@Getter
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;
>>>>>>> c43ab81ec6747deba4e186f4426d6bcc662b0c94
}
