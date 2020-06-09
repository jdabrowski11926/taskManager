package WAT.I8E2S4.TaskManager.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static WAT.I8E2S4.TaskManager.security.SecurityConstants.*;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        System.out.println("UWIRZYTELNIANIE - funkcja <ATTEMPT>");
        //System.out.println("UWIRZYTELNIANIE - HEADER początkowy : "+request.getHeader(HEADER_STRING));
        //System.out.println("UWIRZYTELNIANIE - REQUEST : "+request.getUserPrincipal());
        //System.out.println("UWIRZYTELNIANIE - RESPONSE : "+response.toString());
        try{
            User creds = new ObjectMapper().readValue(request.getInputStream(), User.class);
            System.out.println("UWIRZYTELNIANIE - CREDS - Username:"+creds.getUsername()+" Password:"+creds.getPassword());
            if(creds.getUsername()==null){ throw new BadCredentialsException("1000"); }
            if(creds.getPassword()==null){ throw new BadCredentialsException("1000"); }
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    creds.getUsername(),
                    creds.getPassword(),
                    new ArrayList<>()
            ));
        }catch(IOException e){
            System.out.println("Błąd!");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        System.out.println("UWIRZYTELNIANIE - funkcja <SUCCESSFUL>");
        String token = JWT.create()
                .withSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        System.out.println("TOKEN TO "+ token);
    }
}