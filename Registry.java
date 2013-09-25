import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Registry {

  private String ip;
  private static final int port = 12345;

  private Registry(String ip) {
    this.ip = ip;
  }

  public static Registry getRegistry(String ip) {
    return new Registry(ip);
  }

  public Remote440 lookup(String name) {
    try {
      Message m = new Message(MessageType.MsgRegistryLookupRequest, name, null);
      Socket socket = new Socket(ip, port);
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      os.writeObject(m);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message resp = (Message) is.readObject();
      if (resp.getType() == MessageType.MsgRegistryLookupResponse
          && ((String) resp.getObj()).equals("OK")) {
        RemoteObjectReference ror = (RemoteObjectReference) resp.getArg();
        Object o = ror.localize();
        return (Remote440) o;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void rebind(String serviceName, RemoteObjectReference ror) {
    try {
      Message m = new Message(MessageType.MsgRegistryBindRequest, ror,
          serviceName);
      Socket socket = new Socket(ip, port);
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      System.out.println("send msg");
      os.writeObject(m);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message resp = (Message) is.readObject();
      if (resp.getType() != MessageType.MsgRegistryBindResponse
          || !((String) resp.getObj()).equals("OK")) {
        throw new RuntimeException("serious");
      } else {
        System.out.println("bind success");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
