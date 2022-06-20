package com.fileUpload.starter.api;

import io.vertx.core.Handler;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PostFileUpload  implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(PostFileUpload.class);


  @Override
  public void handle(final RoutingContext routingContext) {
    LOG.info("Respuesta: {}",routingContext);

    List<FileUpload> uploads = routingContext.fileUploads();
    FileSystem fileSystem = routingContext.vertx().fileSystem();
    // multipart form field: file upload section.
    for(FileUpload fileUpload : uploads) {
      // file upload field name of form.
      String fieldName = fileUpload.fileName();
      LOG.info("fieldName: {} ",fieldName);
      LOG.info("fielsize:  {}",fileUpload.size());
      String dataContentType = fileUpload.contentType();


    }

    /*final JsonArray response = new JsonArray();
    response
      .add(new JsonObject().put("symbol",  routingContext.body().asJsonObject()));
    routingContext.response().end(response.toBuffer());*/

    routingContext.response().end();
  }

}
