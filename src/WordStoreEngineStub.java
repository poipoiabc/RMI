import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WordStoreEngineStub extends RemoteStub440 implements WordStore {
  public Word storeWord(String newWord, Word word) throws RemoteException440 {
    Word retWord = null;
    RemoteObjectReference ror = ((RemoteStub440)word).getROR();
    try {
      Message msg = this.buildMsg("storeWord", newWord, ror);
      retWord = (Word)((RemoteObjectReference)this.invokeMethod(msg)).localize();
    } catch (Exception e) {
      throw new RemoteException440();
    }
    return retWord;
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