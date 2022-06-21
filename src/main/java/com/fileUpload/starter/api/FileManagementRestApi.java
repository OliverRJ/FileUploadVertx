package com.fileUpload.starter.api;

import io.vertx.ext.web.Router;

public class FileManagement {
  private static final int INT_BODY_LIMIT = 1024 * 1024 * 2; //2 MB
  public static void attach(Router parent) {
    final String path = "/upload";
    /*parent.post("/upload").handler(BodyHandler.create()
      // .setUploadsDirectory(FILE_LOCATION));*/
    //parent.post(path).handler(BodyHandler.create());
   /* parent.route()
      .handler(BodyHandler.create()
        .setUploadsDirectory("uploads")
        .setBodyLimit(INT_BODY_LIMIT)
        .setHandleFileUploads(true)
        .setMergeFormAttributes(true)
      )
      .failureHandler(RestApiVerticle.handleFailure());*/
    parent.post(path).handler(new PostFileUpload());
  }
}
