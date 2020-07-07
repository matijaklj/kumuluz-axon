package com.kumuluz.ee.samples.jpa.api;

import com.kumuluz.ee.samples.jpa.AxonConfig;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.GenericQueryMessage;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("queries")
public class QueryApi {

    @Inject
    private QueryGateway queryGateway;

    @GET
    @Path("{giftcardId}")
    public Response getGiftcards(@PathParam("giftcardId") String id) throws ExecutionException, InterruptedException {
        //SubscriptionQueryResult<GiftCardRecord, GiftCardRecord> queryResult = axonConfig.getConfig().queryGateway()
        //        .subscriptionQuery(new FindGiftCardQry(id), ResponseTypes.instanceOf(GiftCardRecord.class), ResponseTypes.instanceOf(GiftCardRecord.class));

        //GiftCardRecord giftCardRecordUpdate = queryResult.updates().blockFirst();

        //GiftCardRecord giftCardRecord = axonConfig.getConfig().queryGateway()
        //        .query(new FindGiftCardQry(id), GiftCardRecord.class).getNow(new GiftCardRecord("0", Integer.MIN_VALUE, Integer.MIN_VALUE));

        FindGiftCardQry q = new FindGiftCardQry(id);
        // (1) create a query message
        //GenericQueryMessage<FindGiftCardQry, GiftCardRecord> query =
        //        new GenericQueryMessage<>(q, ResponseTypes.instanceOf(GiftCardRecord.class));
        // (2) send a query message and print query response
        GiftCardRecord result = this.queryGateway.query(q, GiftCardRecord.class).get();

        return Response.ok(result).build();
    }


}
