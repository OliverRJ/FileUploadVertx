package com.fileUpload.starter.api;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class FileRestApi {

  public static void attach(Router parent) {
    final String path = "/form";
    /*parent.post("/upload").handler(BodyHandler.create()
      // .setUploadsDirectory(FILE_LOCATION));*/
    //parent.post(path).handler(new PostFileUpload());
    //parent.post(path).handler(MultipartBodyHandler.create().setMergeFormAttributes(true));
    parent.post(path).handler(BodyHandler.create().setMergeFormAttributes(true));
    parent.post(path).handler(new PostFileUpload());
  }

}
