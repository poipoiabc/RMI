public interface RemoteStub440 extends Remote440 {
  public void setROR(RemoteObjectReference o);

  public RemoteObjectReference getROR();

  public Object callMethod(Message m);
}
