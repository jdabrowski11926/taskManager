package WAT.I8E2S4.TaskManager.security;

import WAT.I8E2S4.TaskManager.User.CORSFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import WAT.I8E2S4.TaskManager.User.JWTAuthenticationFilter;
import WAT.I8E2S4.TaskManager.User.JWTAuthorizationFilter;
import WAT.I8E2S4.TaskManager.User.UserDetailsServiceImpl;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static WAT.I8E2S4.TaskManager.security.SecurityConstants.SIGN_UP_URL;

@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private Http401 http401;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/users").permitAll()
                .anyRequest().authenticated()
                .and()
                .authorizeRequests().antMatchers("/user/{username}/**")
                .access("@guard.checkUsername(authentication, #username)")
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(http401);
    }

    /*@Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST, DELETE, PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        /*UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;*/

        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT","OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.setExposedHeaders(Arrays.asList("Authorization, X-Content-Type-Options, " +
                        "X-XSS-Protection, Cache-Control, Pragma, Expires, X-Frame-Options, Content-Length, Date"));
        //configuration.setAllowedHeaders(Arrays.asList("Authorization, X-Content-Type-Options, " +
        //        "X-XSS-Protection, Cache-Control, Pragma, Expires, X-Frame-Options, Content-Length, Content-Type, Date, Access-Control-Allow-Origin"));
        System.out.println("LISTA DOZWOLONYCH HEADERÓW : "+configuration.getAllowedHeaders().toString());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}