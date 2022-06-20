package com.fileUpload.starter;

import io.vertx.core.*;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(halfProcessors())
      )
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }
  /*
  private Future<String> deployRestApiVerticle(final Promise<Void> startPromise) {
    return vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(halfProcessors())
      )
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }
*/
  private int halfProcessors() {
    return Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
  }
/*
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
   final Router restApi = Router.router(vertx);
    restApi.get("/fileUpload/:file").handler(context -> {
      //context.request().response().sendFile("index.html");
      String productType = ctx.pathParam("file");
        System.out.println("file:" + productType);
      final JsonArray response = new JsonArray();
      response
        .add(new JsonObject().put("symbol", "prueba"));
      context.response().end(response.toBuffer());
    });
    restApi
      .route(HttpMethod.POST, "/fileUpload")
      .consumes("application/json")
      .produces("application/json")
      .handler(routingContext -> {
        routingContext.request().
          bodyHandler(body -> {
          System.out.println(body.toString());
        });
        final JsonArray response = new JsonArray();
        response
          .add(new JsonObject().put("symbol", "prueba"));
        routingContext.response().end(response.toBuffer());
      });
          vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> System.out.println("Http server error:" + error))
      .listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

 */

}
