package com.github.aesteve.vertx.eventbusbridge;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;

import java.util.UUID;

public class TestServer {

    public static void main(String... args) {
        var vertx = Vertx.vertx();
        vertx.eventBus().consumer("echo-address").handler(msg -> {
            System.out.println("Echoing message back: " + msg);
            var options = new DeliveryOptions();
            msg.headers().forEach(e -> options.addHeader(e.getKey(), e.getValue()));
            msg.reply(msg.body(), options);
        });
        vertx.eventBus().consumer("in-address").handler(msg -> {
            System.out.println("received: " + msg);
        });
        vertx.eventBus().consumer("error-address").handler(msg -> msg.fail(503, "FORBIDDEN"));
        var bridge = TcpEventBusBridge.create(
                vertx,
                new BridgeOptions()
                        .addInboundPermitted(new PermittedOptions().setAddress("echo-address"))
                        .addInboundPermitted(new PermittedOptions().setAddress("in-address"))
                        .addInboundPermitted(new PermittedOptions().setAddress("error-address"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("error-address"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("out-address"))
                        .addOutboundPermitted(new PermittedOptions().setAddress("the-reply-address"))
        );
        bridge.listen(7542, res -> {
            if (res.cause() != null) {
                res.cause().printStackTrace();
                return;
            }
            System.out.println("TCP bridge connected");
            vertx.setPeriodic(
                    1000,
                    _id -> vertx.eventBus()
                            .publish("out-address", new JsonObject().put("value", UUID.randomUUID().toString()))
            );
        });
    }

}
