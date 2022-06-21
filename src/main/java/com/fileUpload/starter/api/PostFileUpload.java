package com.fileUpload.starter.api;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class PostFileUpload  implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(PostFileUpload.class);

  @Override
  public void handle(final RoutingContext routingContext) {
    FileSystem fileSystem = routingContext.vertx().fileSystem();
    List<FileUpload> uploads = routingContext.fileUploads();
    // multipart form field: file upload section.
    for(FileUpload fileUpload : uploads) {
      // we actually upload to a file with a generated filename
      try {
        LOG.info("uploaded file detected for field name {}, fielsize {}, dataContentType {}", fileUpload.fileName(), fileUpload.size(),fileUpload.contentType());
        String relativePath = handleOneFile(fileSystem, fileUpload);
        LOG.info("relativePath {} ",relativePath);
      } catch (Exception e) {
        routingContext.fail(e);
      }
    }

    /*final JsonArray response = new JsonArray();
    response
      .add(new JsonObject().put("symbol",  routingContext.body().asJsonObject()));
    routingContext.response().end(response.toBuffer());*/

    routingContext.response().end();
  }

  private String handleOneFile(FileSystem fs, FileUpload upload) {
    String uploadedFile = upload.uploadedFileName();
    String root = uploadedFile.substring(0,uploadedFile.lastIndexOf("\\") );
    String newDestination = root + "/" + upload.fileName();
    fs.moveBlocking(uploadedFile, newDestination);
    LOG.info("uploadedFile: {} and newDestination: {}", uploadedFile, newDestination);
    return newDestination;
  }

  private boolean isHandleUpload(FileUpload upload, String startKey) {
    LOG.info(
      "CHECKING: " + upload.uploadedFileName() + " | fileName: " + upload.fileName() + " | name: " + upload.name());
    String fieldName = upload.name().toLowerCase();
    if (upload.size() <= 0) {
      LOG.info("NOT HANDLED: upload size is zero: " + upload.uploadedFileName() + " | fileName" + upload.fileName()
        + " | " + upload.name());
      return false;
    }
    if (!fieldName.startsWith(startKey)) {
      LOG.info("NOT HANDLED: fieldname does not start with:" + startKey + " | " + fieldName);
      return false;
    }
    return true;
  }



}
