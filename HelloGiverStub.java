import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class HelloGiverStub implements Hello, RemoteStub440 {

  private RemoteObjectReference ror;
  private Class cls = Hello.class;

  public void setROR(RemoteObjectReference o) {
    ror = o;
  }

  public RemoteObjectReference getROR() {
    return ror;
  }

  public Object callMethod(Message m) {

    Object o = null;
    try {
      Socket socket = new Socket(ror.getIP(), ror.getPort());
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      os.writeObject(m);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message resp = (Message) is.readObject();
      o = resp.getObj();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return o;
  }

  public Message buildMsg(String name, Object... args)
      throws NoSuchMethodException {
    Message m = new Message(MessageType.MsgMethodInvokeRequest, name, args);
    return m;
  }

  public String hello(Integer arg) {

    String ret = null;

    try {
      Message m = buildMsg("hello", arg);
      ret = (String) callMethod(m);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return ret;

  }

}
