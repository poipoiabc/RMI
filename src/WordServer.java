import java.net.InetAddress;

public class WordServer {
  public static void showUsage() {
    System.out.println("Usage: WordServer [RegistryIP]");
    System.out.println("\tDefault RegistryIP is localhost.");
  }
  
  public static void main(String[] args) {
    String registryIP = null;
    if (args.length == 0) {
      try {
        registryIP = InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else if (args.length == 1) {
      registryIP = args[0];
    } else {
      WordServer.showUsage();
      return;
    }
    
    try {
      WordEngine wordEngine = new WordEngine();
      RMIService service = new RMIService();
      RemoteStub440 wordStub = service.export(wordEngine, 15440);
      Registry registry = Registry.getRegistry(registryIP);
      registry.rebind("WordService", wordStub);
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}
