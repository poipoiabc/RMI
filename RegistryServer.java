import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class RegistryServer {

  public RegistryServer() {
    map = new HashMap<String, RemoteObjectReference>();
  }

  private static final int port = 12345;
  private HashMap<String, RemoteObjectReference> map;

  public static void main(String[] args) {

    RegistryServer server = new RegistryServer();
    server.serve();

  }

  public void serve() {
    ServerSocket serverSock = null;
    try {
      System.out.println(InetAddress.getLocalHost().getHostAddress());
      serverSock = new ServerSocket(port);
      while (true) {

        Socket socket = serverSock.accept();
        System.out.println("Get msg");
        MsgHandler handler = new MsgHandler(socket);
        Thread t = new Thread(handler);
        t.start();

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public class MsgHandler implements Runnable {

    Socket socket;

    public MsgHandler(Socket socket) {
      this.socket = socket;
    }

    public void processMessage(Socket socket) {

      ObjectInputStream is;
      try {
        is = new ObjectInputStream(socket.getInputStream());

        Message msg = (Message) is.readObject();

        MessageType type = msg.getType();
        Message response = null;
        if (type == MessageType.MsgRegistryBindRequest) {
          System.out.println("BIND");
          RemoteObjectReference ror = (RemoteObjectReference) msg.getObj();
          String servicename = (String) msg.getArg();
          System.out.println("serviceName=" + servicename);
          map.put(servicename, ror);
          response = new Message(MessageType.MsgRegistryBindResponse, "OK",
              null);
        }
        if (type == MessageType.MsgRegistryLookupRequest) {
          System.out.println("LOOKUP");
          String service = (String) msg.getObj();
          RemoteObjectReference ror = map.get(service);
          if (ror == null) {
            response = new Message(MessageType.MsgRegistryLookupResponse,
                "ERROR", null);
          } else {
            response = new Message(MessageType.MsgRegistryLookupResponse, "OK",
                ror);
          }
        }
        ObjectOutputStream out = new ObjectOutputStream(
            socket.getOutputStream());
        out.writeObject(response);
        out.flush();
        out.close();
        socket.close();

      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    @Override
    public void run() {
      processMessage(socket);
    }
  }

}
