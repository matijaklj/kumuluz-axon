package com.kumuluz.ee.axon.example.api;

import com.kumuluz.ee.kumuluzee.axon.properties.AxonServerProperties;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.config.Configuration;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
    private Configuration configuration;

    @Inject
    private CommandGateway commandGateway;

    /*@Inject
    @Named("eventSerializer")
    private Serializer eventSerializer;

     */
    @Inject
    private AxonServerProperties axonServerProperties;

    @POST
    public Response issueGiftCard(IssueCmd issueCmd) {
        String cardId = UUID.randomUUID().toString();


        //MyCommandGateway myCommandGateway = (MyCommandGateway) this.commandGateway;

        CompletableFuture<String> futureResult = this.commandGateway
                .send(issueCmd);

        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/redeem/{id}/{amount}")
    public Response issueGiftCard(@PathParam("id") String giftCardId, @PathParam("amount") int amount) {

        CompletableFuture<String> futureResult = this.configuration.commandGateway()
                .send(new RedeemCmd(giftCardId, amount));

        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}
