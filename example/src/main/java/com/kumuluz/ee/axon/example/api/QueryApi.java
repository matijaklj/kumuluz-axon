package com.kumuluz.ee.axon.example.api;

import com.kumuluz.ee.axon.example.command.GiftCard;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("queries")
public class QueryApi {

    @PersistenceContext
    private EntityManager em;

    @GET
    @Path("{giftcardId}")
    public Response getGiftcards(@PathParam("giftcardId") String id) throws ExecutionException, InterruptedException {
        //SubscriptionQueryResult<GiftCardRecord, GiftCardRecord> queryResult = axonConfig.getConfig().queryGateway()
        //        .subscriptionQuery(new FindGiftCardQry(id), ResponseTypes.instanceOf(GiftCardRecord.class), ResponseTypes.instanceOf(GiftCardRecord.class));

        //GiftCardRecord giftCardRecordUpdate = queryResult.updates().blockFirst();

        //GiftCardRecord giftCardRecord = axonConfig.getConfig().queryGateway()
        //        .query(new FindGiftCardQry(id), GiftCardRecord.class).getNow(new GiftCardRecord("0", Integer.MIN_VALUE, Integer.MIN_VALUE));

        FindGiftCardQry q = new FindGiftCardQry(id);

        em.find(GiftCard.class, id);
        // (1) create a query message
        //GenericQueryMessage<FindGiftCardQry, GiftCardRecord> query =
        //        new GenericQueryMessage<>(q, ResponseTypes.instanceOf(GiftCardRecord.class));
        // (2) send a query message and print query response
        //GiftCardRecord result = this.configuration.queryGateway().query(q, GiftCardRecord.class).get();

        //return Response.ok(result).build();
        return null;
    }


}
