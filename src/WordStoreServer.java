import java.net.InetAddress;

public class WordStoreServer {
  public static void showUsage() {
    System.out.println("Usage: WordStoreServer [<ExportPort>] [<RegistryIP>:<RegistryPort>]");
    System.out.println("\tDefault ExportPort is l5640.");
    System.out.println("\tDefault RegistryIP is localhost.");
    System.out.println("\tDefault RegistryPort is 1099.");
  }
  
  public static void main(String[] args) {
    int exportPort = 15640;
    String registryIP = null;
    int registryPort = 1099;
    
    if (args.length == 0) {
      try {
        registryIP = InetAddress.getLocalHost().getHostAddress();
      } catch (Exception e) {
        System.out.println("Cannot find Registry Server on localhost:1099.");
        return;
      }
    } else if (args.length == 1) {
      try {
        if (args[0].contains(":")) {  // <RegistryIP>:<RegistryPort>
          String[] RegIPPort = args[0].split(":");
          registryIP = RegIPPort[0];
          registryPort = Integer.valueOf(RegIPPort[1]);
        } else {  // <ExportPort>
          exportPort = Integer.valueOf(args[0]);
        }
      } catch (Exception e) {
        WordStoreServer.showUsage();
        return;
      }
    } else if (args.length == 2) {
      try {
        exportPort = Integer.valueOf(args[0]);
        String[] RegIPPort = args[1].split(":");
        registryIP = RegIPPort[0];
        registryPort = Integer.valueOf(RegIPPort[1]);
      } catch (Exception e) {
        WordStoreServer.showUsage();
        return;
      }
    } else {
      WordStoreServer.showUsage();
      return;
    }
    
    try {
      WordStoreEngine wordStoreEngine = new WordStoreEngine();
      RMIService service = new RMIService();
      RemoteStub440 wordStoreStub = service.export(wordStoreEngine, exportPort);
      Registry registry = Registry.getRegistry(registryIP, registryPort);
      registry.rebind("WordStoreService", wordStoreStub);
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}
