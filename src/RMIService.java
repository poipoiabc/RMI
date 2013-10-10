import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class RMIService {
  private Remote440 realObj;
  private int port;
  private String ip;

  public RMIService() throws RemoteException440 {
    try {
      this.ip = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      throw new RemoteException440();
    }
  }

  public RemoteStub440 export(Remote440 obj, int port) throws RemoteException440 {
    this.realObj = obj;
    this.port = port;
    RemoteObjectReference ror = new RemoteObjectReference(this.ip, this.port, obj.getClass().getName());
    RemoteStub440 remoteStub440 = new RemoteStub440();
    remoteStub440.setROR(ror);
    
    Thread thread = new Thread() {
      public void run() {
        ServerSocket serverSock = null;
        try {
          serverSock = new ServerSocket(RMIService.this.port);
          System.out.println("RMI Service running on " + RMIService.this.ip + ":" + RMIService.this.port);
          while (true) {
            Socket socket = serverSock.accept();
            MsgHandler handler = new MsgHandler(socket);
            Thread t = new Thread(handler);
            t.start();
          }
        } catch (Exception e) {
          System.out.println("RMIService export failed.");
        }
      }
    };
    thread.start();
    
    return remoteStub440;
  }

  public class MsgHandler implements Runnable {
    Socket socket;

    public MsgHandler(Socket socket) {
      this.socket = socket;
    }

    //@Override
    public void run() {
      try {
        ObjectInputStream is = new ObjectInputStream(this.socket.getInputStream());
        Message msg = (Message)is.readObject();
        if (msg.getType() != MessageType.MsgMethodInvokeRequest) {
          System.out.println("Method Invoke failed.");
          return;
        }

        System.out.println("Method Invoke Request received.");
        String methodName = (String)msg.getObj();
        Object[] args = (Object[])msg.getArg();
        System.out.println("Method name is " + methodName);

        Method method = null;
        if (args != null) {
          Class[] types = new Class[args.length];
          for (int i = 0; i < types.length; i++) {
            if (args[i] instanceof RemoteObjectReference) { // Argument is RemoteObjectReference of Remote440 object
              Class className = Class.forName(((RemoteObjectReference)args[i]).getClassName());
              types[i] = (className.getInterfaces())[0];  // Get interface class
              args[i] = ((RemoteObjectReference)args[i]).localize();  // Get stub from RemoteObjectReference
            } else {  // Argument is Serializable object
              types[i] = args[i].getClass();
            }
          }
          method = realObj.getClass().getMethod(methodName, types);
        } else {  // Argument is null
          method = realObj.getClass().getMethod(methodName, (Class[])null);
        }
        Object returnVal = method.invoke(realObj, args);

        Message retMsg = null;
        if (returnVal != null && returnVal.getClass().getSuperclass().getName().equals("RemoteStub440")) {  // Return value is Remote440 object
          retMsg = new Message(MessageType.MsgMethodInvokeResponse, ((RemoteStub440)returnVal).getROR(), null);
        } else {  // Return value is null or Serilizable object
          retMsg = new Message(MessageType.MsgMethodInvokeResponse, returnVal, null);
        }

        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Method Invoke Response sent.");
        os.writeObject(retMsg);
        os.flush();
        os.close();
        socket.close();
      } catch (Exception e) {
        System.out.println("Method Invoke failed.");
      }
    }
  }

}
