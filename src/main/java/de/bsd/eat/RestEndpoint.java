package de.bsd.eat;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Path("/")
public class RestEndpoint {

    List<String> messages = new ArrayList<>();

    @SuppressWarnings("unused")
    @Incoming("mytopic")
    void receiveFromKafka(String message) {
        synchronized (messages) {
            messages.add(message);
        }
    }

    @GET
    @Produces("application/json")
    public Response getError() {

        List<String> buffer;

        if (messages.isEmpty()) {
            return Response.noContent().build();
        }
        synchronized (messages) {
            buffer = new ArrayList<>(messages);
            messages.clear();
        }
        return Response.ok(buffer).build();
    }
}
