import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RMIService implements Runnable {

  private Remote440 realObj;

  private int port;

  private String ip;

  public RMIService() {
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public RemoteObjectReference export(Remote440 obj, int port) {
    this.realObj = obj;
    this.port = port;
    return new RemoteObjectReference(ip, port, obj.getClass().getName());
  }

  @Override
  public void run() {
    ServerSocket serverSock = null;
    try {
      serverSock = new ServerSocket(port);
      System.out.println(ip);
      while (true) {

        Socket socket = serverSock.accept();
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

    @Override
    public void run() {
      try {
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        Message msg = (Message) is.readObject();

        if (msg.getType() != MessageType.MsgMethodInvokeRequest)
          throw new RuntimeException("serious!");

        System.out.println("get request");
        String methodName = (String) msg.getObj();
        Object[] args = (Object[]) msg.getArg();

        @SuppressWarnings("rawtypes")
        Class[] types = new Class[args.length];
        for (int i = 0; i < types.length; i++) {
          types[i] = args[i].getClass();
        }
        Method method = realObj.getClass().getMethod(methodName, types);

        Object returnval = method.invoke(realObj, args);

        Message retmsg = new Message(MessageType.MsgMethodInvokeResponse,
            returnval, null);

        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("give response");
        os.writeObject(retmsg);
        os.flush();
        os.close();
        socket.close();

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}
