package WAT.I8E2S4.TaskManager.User;

import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(final ContainerRequestContext requestContext,
                       final ContainerResponseContext cres) throws IOException {
        /*cres.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000/*");
        //cres.getHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        cres.getHeaders().add("Access-Control-Allow-Headers", "Authorization, X-Content-Type-Options, " +
                "X-XSS-Protection, Cache-Control, Pragma, Expires, X-Frame-Options, Content-Length, Date");
        cres.getHeaders().add("Access-Control-Allow-Credentials", "true");
        cres.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");*/
    }

}