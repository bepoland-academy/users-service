package pl.betse.beontime.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.betse.beontime.bo.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String ROLES_CLAIM = "roles";
    private static final String ROLES_DELIMITER = ",";

    @Value("${jwt.config.expirationTime}")
    String expirationTime;

    @Value("${jwt.config.secret}")
    String secret;


    Authentication getAuthentication(HttpServletRequest request) {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(jwt)) {
            LOGGER.error("Missing authentication token");
            return null;
        }
        String jwtToken = retrieveJwt(jwt);
        Jwt<JwsHeader, Claims> jwtObject;
        try {
            jwtObject = parseJwt(jwtToken);
        } catch (AuthorizationServiceException e) {
            return null;
        }
        String user = jwtObject.getBody().getSubject();
        String roles = (String) jwtObject.getBody().get(ROLES_CLAIM);
        List<SimpleGrantedAuthority> authorities = getRoles(roles);
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    private Jwt<JwsHeader, Claims> parseJwt(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException e) {
            String message = "Invalid token";
            LOGGER.error(message, e);
            throw new AuthorizationServiceException("Invalid token");
        }
    }

    private String retrieveJwt(String jwt) {
        return jwt.replace(TOKEN_PREFIX, StringUtils.EMPTY);
    }


    private List<SimpleGrantedAuthority> getRoles(String roles) {
        List<String> stringRoles = Arrays.asList(roles.split(ROLES_DELIMITER));
        return stringRoles.stream()
                .filter(StringUtils::isNotBlank)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

}
