package org.exoplatform.addons.linotp.portlet.SecurityManagement.component;

import java.io.Serializable;

/**
 * created on 06/12/2016
 * 
 * @author azaoui@exoplatform.com
 */

public class SpaceQuery implements Serializable {

  private static final long serialVersionUID = 1L;

  private String            spaceName_;

  private String            URL_;

  public boolean isEmpty() {
    return spaceName_ == null && URL_ == null;
  }

  public String getSpaceName() {
    return spaceName_;
  }

  public void setSpaceName(String spaceName) {
    this.spaceName_ = spaceName;
  }

  public String getURL_() {
    return URL_;
  }

  public void setURL_(String uRL_) {
    URL_ = uRL_;
  }
}
