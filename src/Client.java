public class Client {

  public static void main(String[] args) {
    // TODO Auto-generated method stub

    // RemoteObjectReference ror = new
    // RemoteObjectReference("192.168.1.107",10010,"HelloGiver");
    // Hello giver = (Hello)ror.localize();

    Registry registry = Registry.getRegistry("192.168.1.107");

    Hello giver = (Hello) registry.lookup("testService");

    System.out.println(giver.hello(1234455));
  }

}
