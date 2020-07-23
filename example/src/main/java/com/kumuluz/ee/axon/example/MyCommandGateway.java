package com.kumuluz.ee.axon.example;

import com.kumuluz.ee.axon.example.api.IssueCmd;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.Timeout;
import org.axonframework.messaging.annotation.MetaDataValue;

import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface MyCommandGateway extends CommandGateway {

    // fire and forget
    CompletableFuture<String> sendCommand(IssueCmd command);

    // method that attaches metadata and will wait for a result for 10 seconds
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    Response sendCommandAndWaitForAResult(IssueCmd command,
                                          @MetaDataValue("userId") String userId);

    // alternative that throws exceptions on timeout
    @Timeout(value = 20, unit = TimeUnit.SECONDS)
    Response sendCommandAndWaitForAResult(IssueCmd command)
            throws TimeoutException, InterruptedException;

    // this method will also wait, caller decides how long
    void sendCommandAndWait(IssueCmd command, long timeout, TimeUnit unit)
            throws TimeoutException, InterruptedException;


}