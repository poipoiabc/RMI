import java.net.InetAddress;

public class WordStoreServer {
  public static void showUsage() {
    System.out.println("Usage: WordStoreServer [RegistryIP]");
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
      WordStoreServer.showUsage();
      return;
    }
    
    try {
      WordStoreEngine wordStoreEngine = new WordStoreEngine();
      RMIService service = new RMIService();
      RemoteStub440 wordStoreStub = service.export(wordStoreEngine, 15640);
      Registry registry = Registry.getRegistry(registryIP);
      registry.rebind("WordStoreService", wordStoreStub);
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}
