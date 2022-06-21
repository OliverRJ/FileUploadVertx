package com.fileUpload.starter.api;

import io.vertx.ext.web.Router;

public class FileManagementRestApi {
  private static final int INT_BODY_LIMIT = 1024 * 1024 * 2; //2 MB
  public static void attach(Router parent) {
    final String path = "/upload";
    parent.post(path).handler(new PostFileManagmentHandler());
  }
}
