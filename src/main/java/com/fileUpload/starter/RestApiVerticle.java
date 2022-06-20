package com.fileUpload.starter;

import com.fileUpload.starter.api.FileRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestApiVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(RestApiVerticle.class);
  private static final int INT_BODY_LIMIT = 1024 * 1024 * 2; //2 MB

  @Override
  public void start(final Promise<Void> startPromise) throws Exception {
    startHttpServerAndAttachRoutes(startPromise);
  }

  private void startHttpServerAndAttachRoutes(final Promise<Void> startPromise) {

    final Router restApi = Router.router(vertx);
    restApi.route().handler(BodyHandler.create()
      .setUploadsDirectory("uploads")
      .setBodyLimit(INT_BODY_LIMIT)
      .setHandleFileUploads(true));
    restApi.route("/").handler( routingContext -> {
      routingContext.response().putHeader("content-type", "text/html").end("<form action=\"/form\" method=\"post\" enctype=\"multipart/form-data\">\n" + "    <div>\n" + "        <label for=\"name\">Select a file:</label>\n" + "        <input type=\"file\" name=\"file\" />\n" + "    </div>\n" + "    <div class=\"button\">\n" + "        <button type=\"submit\">Send</button>\n" + "    </div>" + "</form>");
    });

    restApi.route()
      .handler(BodyHandler.create())
      .failureHandler(handleFailure());
    FileRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
     /* .requestHandler(request ->{
        request.setExpectMultipart(true);
        request.uploadHandler(upload -> {
          LOG.info("file name:",upload.name());
        });
      })*/
      .exceptionHandler(error -> LOG.error("Http server error:" + error))
      .listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          LOG.info("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
  private Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        // Ignore completed response
        return;
      }
      LOG.error("Route Error:", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
    };
  }
}
