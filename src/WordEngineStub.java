import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WordEngineStub extends RemoteStub440 implements Word {
  public String getWord() throws RemoteException440 {
    String word = null;
    try {
      Message msg = this.buildMsg("getWord", (Object[])null);
      word = (String)this.invokeMethod(msg);
    } catch (Exception e) {
      throw new RemoteException440();
    }
    return word;
  }
  
  public void setWord(String word) throws RemoteException440 {
    try {
      Message msg = this.buildMsg("setWord", word);
      this.invokeMethod(msg);
    } catch (Exception e) {
      throw new RemoteException440();
    }  
  }
  
  public Message buildMsg(String methodName, Object... methodArgs) {
    Message msg = new Message(MessageType.MsgMethodInvokeRequest, methodName, methodArgs);
    return msg;
  }
  
  public Object invokeMethod(Message msg) throws RemoteException440 {
    Object obj = null;
    try {
      Socket socket = new Socket(this.ror.getIP(), this.ror.getPort());
      ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
      os.writeObject(msg);
      ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
      Message response = (Message)is.readObject();
      obj = response.getObj();
      os.close();
      is.close();
      socket.close();
    } catch (Exception e) {
      throw new RemoteException440();
    }
    return obj;
  }
}