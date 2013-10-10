import java.net.InetAddress;

public class WordClient {
  public static void showUsage() {
    System.out.println("Usage: WordClient [<RegistryIP>:<RegistryPort>]");
    System.out.println("\tDefault RegistryIP is localhost.");
    System.out.println("\tDefault RegistryPort is 1099.");
  }
  
  public static void main(String[] args) {
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
        String[] RegIPPort = args[0].split(":");
        registryIP = RegIPPort[0];
        registryPort = Integer.valueOf(RegIPPort[1]);
      } catch (Exception e) {
        WordClient.showUsage();
        return;
      }
    } else {
      WordClient.showUsage();
      return;
    }
    
    try {
      Registry registry = Registry.getRegistry(registryIP, registryPort);
      Word word = (Word)registry.lookup("WordService");
      word.setWord("Harry");
      System.out.println(word.getWord());
      
      WordStore wordStore = (WordStore)registry.lookup("WordStoreService");
      word = wordStore.storeWord("Potter", word);
      System.out.println(word.getWord());
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}