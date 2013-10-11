import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServer {
  private int port;
  private ConcurrentHashMap<String, RemoteObjectReference> map;
  
  public RegistryServer() {
    this.port = 1099;
    this.map = new ConcurrentHashMap<String, RemoteObjectReference>();
  }
  
  public RegistryServer(int port) {
    this.port = port;
    this.map = new ConcurrentHashMap<String, RemoteObjectReference>();
  }

  public void serve() throws RemoteException440 {
    ServerSocket serverSock = null;
    try {
      System.out.println("Registry Server started on " + InetAddress.getLocalHost().getHostAddress() + ":" + this.port);
      serverSock = new ServerSocket(this.port);
      while (true) {
        Socket socket = serverSock.accept();
        System.out.println("New message received:");
        Thread thread = new Thread(new MsgHandler(socket));
        thread.start();
      }
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }
  
  public static void showUsage() {
    System.out.println("Usage: RegistryServer [<PortNumber>]");
    System.out.println("\tDefault PortNumber is 1099.");
  }
  
  public static void main(String[] args) {
    RegistryServer server = null;
    if (args.length == 0) {
      server = new RegistryServer();
    } else if (args.length == 1) {
      int inputNum = 0;
      try {
        inputNum = Integer.parseInt(args[0]);
      } catch (Exception e) {
        RegistryServer.showUsage();
        return;
      }
      server = new RegistryServer(inputNum);
    } else {
      RegistryServer.showUsage();
      return;
    }
    try {
      server.serve();
    } catch (RemoteException440 e) {
      System.out.println("Registry Sever failed to start.");
    }
  }

  public class MsgHandler implements Runnable {
    Socket socket;
    
    public MsgHandler(Socket socket) {
      this.socket = socket;
    }

    public void processRequest() {
      try {
        ObjectInputStream is = new ObjectInputStream(this.socket.getInputStream());
        Message msg = (Message)is.readObject();
        MessageType type = msg.getType();
        Message response = null;
        if (type == MessageType.MsgRegistryBindRequest) {
          System.out.println("Bind request received...");
          RemoteObjectReference ror = (RemoteObjectReference)msg.getObj();
          String serviceName = (String)msg.getArg();
          System.out.println("Service Name is " + serviceName);
          if(RegistryServer.this.map.containsKey(serviceName)) {
            response = new Message(MessageType.MsgRegistryBindResponse, null, "ERROR");
          } else {
            RegistryServer.this.map.put(serviceName, ror);
            response = new Message(MessageType.MsgRegistryBindResponse, null, "OK");
          }
        } else if (type == MessageType.MsgRegistryLookupRequest) {
          System.out.println("Lookup request received...");
          String service = (String)msg.getArg();
          RemoteObjectReference ror = map.get(service);
          if (ror == null) {
            response = new Message(MessageType.MsgRegistryLookupResponse, null, "ERROR");
          } else {
            response = new Message(MessageType.MsgRegistryLookupResponse, ror, "OK");
          }
        }
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(response);
        out.flush();
        out.close();
        socket.close();
        System.out.println("Request processed.");
      } catch (Exception e) {
        System.out.println("Process request failed.");
      }
    }

    //@Override
    public void run() {
      this.processRequest();
    }
  }
}
