import java.net.InetAddress;

public class WordServer {
  public static void showUsage() {
    System.out.println("Usage: WordServer [<ExportPort>] [<RegistryIP>:<RegistryPort>]");
    System.out.println("\tDefault ExportPort is l5440.");
    System.out.println("\tDefault RegistryIP is localhost.");
    System.out.println("\tDefault RegistryPort is 1099.");
  }
  
  public static void main(String[] args) {
    int exportPort = 15440;
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
        WordServer.showUsage();
        return;
      }
    } else if (args.length == 2) {
      try {
        exportPort = Integer.valueOf(args[0]);
        String[] RegIPPort = args[1].split(":");
        registryIP = RegIPPort[0];
        registryPort = Integer.valueOf(RegIPPort[1]);
      } catch (Exception e) {
        WordServer.showUsage();
        return;
      }
    } else {
      WordServer.showUsage();
      return;
    }
    
    try {
      WordEngine wordEngine = new WordEngine();
      RMIService service = new RMIService();
      RemoteStub440 wordStub = service.export(wordEngine, exportPort);
      Registry registry = Registry.getRegistry(registryIP, registryPort);
      registry.rebind("WordService", wordStub);
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}
