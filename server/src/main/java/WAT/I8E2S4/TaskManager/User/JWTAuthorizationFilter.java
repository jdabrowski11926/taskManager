package WAT.I8E2S4.TaskManager.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static WAT.I8E2S4.TaskManager.security.SecurityConstants.*;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        //System.out.println("UPRAWNIENIA - fukcja <FILTER INTERNAL>");
        //System.out.println(""+request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        //System.out.println("RESPONSE: "+ response.toString());
        String header = request.getHeader(HEADER_STRING);
        if(header!=null) header = header.replaceAll("%20"," ");
        //System.out.println("HEADER TO "+header);
        if(header == null || !header.startsWith(TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }
        //System.out.println("UPRAWNIENIA - fukcja <FILTER INTERNAL> - kontynuacja");
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //System.out.println("UPRAWNIENIA - funkcja <GET AUTHENTICATION>");
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            token = token.replaceAll("%20"," ");
            String user = JWT.require(
                    Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            //System.out.println("PODJECIE DECYZJI");
            if (user != null) {
                //System.out.println("JEST UŻYTKOWNIK : "+user.toString());
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            //System.out.println("NIE MA UŻYTKOWNIKA");
            return null;
        }
        return null;
    }

    private String getBody(HttpServletRequest request) throws IOException {
        InputStream inStream = request.getInputStream();
        InputStreamReader reader = new InputStreamReader(inStream);
        BufferedReader bReader = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String inputLine;
        while ((inputLine = bReader.readLine()) != null) {
            sb.append(inputLine);
            sb.append("\n");
        }
        return sb.toString();
    }
}