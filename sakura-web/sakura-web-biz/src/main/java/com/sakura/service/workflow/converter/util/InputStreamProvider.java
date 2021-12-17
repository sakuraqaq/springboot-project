package com.sakura.service.workflow.converter.util;

import java.io.InputStream;

public interface InputStreamProvider {

  /**
   * Creates a <b>NEW</b> {@link InputStream} to the provided resource.
   */
  InputStream getInputStream();

}
