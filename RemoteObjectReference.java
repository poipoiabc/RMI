import java.io.Serializable;

//@SuppressWarnings("serial")
public class RemoteObjectReference implements Serializable {
  private static final long serialVersionUID = 1L;
  private String ip;
  private int port;
  private String className;

  public RemoteObjectReference(String ip, int port, String className) {
    this.ip = ip;
    this.port = port;
    this.className = className;
  }
  
  public String getIP() {
    return this.ip;
  }

  public int getPort() {
    return this.port;
  }

  public String getClassName() {
    return this.className;
  }

  public Object localize() {
    RemoteStub440 stub = null;
    // Determines if class exists
    try {
      String stubName = this.className + "Stub";
      //@SuppressWarnings("rawtypes")
      Class c = Class.forName(stubName);
      stub = (RemoteStub440)c.newInstance();
      stub.setROR(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return stub;
  }
}
