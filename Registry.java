import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Registry {
  private String ip;
  private int port;
  
  private Registry() throws RemoteException440 {
    try {
      this.ip = InetAddress.getLocalHost().getHostAddress();
      this.port = 1099;
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }
  
  private Registry(String ip) {
    this.ip = ip;
    this.port = 1099;
  }
  
  private Registry(int port) throws RemoteException440 {
    try {
      this.ip = InetAddress.getLocalHost().getHostAddress();
      this.port = port;
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }
  
  private Registry(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  public static Registry getRegistry() throws RemoteException440 {
    return new Registry();
  }
  
  public static Registry getRegistry(String ip) {
    return new Registry(ip);
  }
  
  public static Registry getRegistry(int port) throws RemoteException440 {
    return new Registry(port);
  }
  
  public static Registry getRegistry(String ip, int port) {
    return new Registry(ip, port);
  }

  public void rebind(String serviceName, RemoteStub440 remoteStub440) throws RemoteException440 {
    try {
      RemoteObjectReference ror = remoteStub440.getROR();
      Message msg = new Message(MessageType.MsgRegistryBindRequest, ror, serviceName);
      Socket socket = new Socket(this.ip, this.port);
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      os.writeObject(msg);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message resp = (Message)is.readObject();
      if (!((String)resp.getArg()).equals("OK")) {
        throw new RemoteException440();
      }
      os.close();
      is.close();
      socket.close();
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }
  
  public Remote440 lookup(String serviceName) throws RemoteException440 {
    try {
      Message msg = new Message(MessageType.MsgRegistryLookupRequest, null, serviceName);
      Socket socket = new Socket(this.ip, this.port);
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      os.writeObject(msg);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message response = (Message)is.readObject();
      os.close();
      is.close();
      socket.close();
      if (((String)response.getArg()).equals("OK")) {
        RemoteObjectReference ror = (RemoteObjectReference)response.getObj();
        return (Remote440)ror.localize();
      } else {
        throw new RemoteException440();
      }
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }
}
