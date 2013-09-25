import java.io.Serializable;

@SuppressWarnings("serial")
public class RemoteObjectReference implements Serializable {

  private String ipAddr;
  private int port;
  private String serviceName;

  public RemoteObjectReference(String ip, int port, String serviceName) {
    this.ipAddr = ip;
    this.port = port;
    this.serviceName = serviceName;
  }

  public RemoteObjectReference(String ip, int port) {
    this.ipAddr = ip;
    this.port = port;
  }

  public void setServiceName(String name) {
    this.serviceName = name;
  }

  public Object localize() {

    RemoteStub440 stub = null;
    // Determines if class exists
    try {
      String stubName = serviceName + "Stub";
      @SuppressWarnings("rawtypes")
      Class c = Class.forName(stubName);
      stub = (RemoteStub440) c.newInstance();
      stub.setROR(this);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return stub;
  }

  public String getIP() {
    return this.ipAddr;
  }

  public int getPort() {
    return port;
  }

  public String getServiceName() {
    return serviceName;
  }
}
