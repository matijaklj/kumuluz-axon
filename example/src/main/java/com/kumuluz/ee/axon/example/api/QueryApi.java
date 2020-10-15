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

package com.kumuluz.ee.axon.example.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.ExecutionException;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("queries")
public class QueryApi {

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
        //GiftCardRecord result = this.configuration.queryGateway().query(q, GiftCardRecord.class).get();

        //return Response.ok(result).build();
        return null;
    }


}
