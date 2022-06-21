package com.fileUpload.starter;

import com.fileUpload.starter.api.FileManagementRestApi;
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
    //1. Using Vertx Core
    //startHttpServerVertxCoreExample(startPromise);
    //2. Using Vertx Web, recommended
    startHttpServerVertxWebExample(startPromise);
  }

  private void startHttpServerVertxCoreExample(Promise<Void> startPromise) {
    vertx.createHttpServer().requestHandler(req -> {
      if (req.uri().equals("/")) {
        // Serve the index page
        req.response().sendFile("index.html");
      } else if (req.uri().startsWith("/upload")) {
        req.setExpectMultipart(true);
        req.uploadHandler(upload -> {
          upload.exceptionHandler(cause -> {
            req.response().setChunked(true).end("Upload failed");
          });
          upload.endHandler(v -> {
            req.response().setChunked(true).end("Successfully uploaded to " + upload.filename());
          });
          upload.streamToFileSystem("uploads/" +upload.filename());
        });
      } else {
        req.response().setStatusCode(404);
      }
      req.response().end();

    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
  private void startHttpServerVertxWebExample(final Promise<Void> startPromise) {

    final Router restApi = Router.router(vertx);
    restApi.route()
      .handler(BodyHandler.create()
        .setUploadsDirectory("uploads") //Carpeta donde se guardarán los archivos dentro del proyecto
        .setBodyLimit(INT_BODY_LIMIT) // limite del archivo en bytes
        .setHandleFileUploads(true) //flag para activar manejo de archivos
        .setMergeFormAttributes(true) //Por defecto es true pero lo pongo como ejemplo
      )
      .failureHandler(handleFailure());
    FileManagementRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
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
  public static Handler<RoutingContext> handleFailure() {
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
