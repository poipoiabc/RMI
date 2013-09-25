public class Server {

  public static void main(String[] args) throws ClassNotFoundException {
    // TODO Auto-generated method stub

    HelloGiver hellogiver = new HelloGiver();

    RMIService service = new RMIService();
    RemoteObjectReference ror = service.export(hellogiver, 10035);
    Thread ser = new Thread(service);
    ser.start();
    Registry registry = Registry.getRegistry("192.168.1.107");
    registry.rebind("testService", ror);
  }

}
