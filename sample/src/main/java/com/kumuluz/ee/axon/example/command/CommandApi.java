/*
 *  Copyright (c) 2014-2017 Kumuluz and/or its affiliates
 *  and other contributors as indicated by the @author tags and
 *  the contributor list.
 *
 *  Licensed under the MIT License (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://opensource.org/licenses/MIT
 *
 *  The software is provided "AS IS", WITHOUT WARRANTY OF ANY KIND, express or
 *  implied, including but not limited to the warranties of merchantability,
 *  fitness for a particular purpose and noninfringement. in no event shall the
 *  authors or copyright holders be liable for any claim, damages or other
 *  liability, whether in an action of contract, tort or otherwise, arising from,
 *  out of or in connection with the software or the use or other dealings in the
 *  software. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kumuluz.ee.axon.example.command;

import com.kumuluz.ee.axon.example.api.commands.IssueCommand;
import com.kumuluz.ee.axon.example.api.commands.RedeemCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

/**
 * Rest api for command side.
 *
 * @author Matija Kljun
 */
@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("commands")
public class CommandApi {

    @Inject
    private CommandGateway commandGateway;


    @POST
    public Response issueGiftCard(IssueCommand issueCommand) {

        CompletableFuture futureResult = this.commandGateway.send(issueCommand);
        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @POST
    @Path("/redeem/{id}/{amount}")
    public Response redeemGiftCard(@PathParam("id") String giftCardId, @PathParam("amount") int amount) {

        CompletableFuture<String> futureResult = commandGateway
                .send(new RedeemCommand(giftCardId, amount));

        try {
            return Response.ok(futureResult.get()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }



}
