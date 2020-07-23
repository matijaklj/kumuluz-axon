package com.kumuluz.ee.axon.example.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import java.io.Serializable

data class RedeemCmd (@TargetAggregateIdentifier val id: String, val amount: Int)

data class RedeemedEvt(val id: String, val amount: Int)