package com.kumuluz.ee.samples.jpa.api;

import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfig;
import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfigurationCdiExtension;
import com.kumuluz.ee.samples.kumuluzee.axon.AxonConfigurationInitializer;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("commands")
public class CommandApi {

    @Inject
    private AxonConfig axonConfig;

    @Inject
    private Configuration configuration;

    @Inject
    private CommandGateway commandGateway;

    @POST
    public Response addNewCustomer(IssueCmd issueCmd) {
        String cardId = UUID.randomUUID().toString();

        CompletableFuture<String> futureResult = axonConfig.getConfiguration().commandGateway()
                .send(new IssueCmd(cardId, 100));

        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception ignored) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}
