import java.net.InetAddress;

public class WordClient {
  public static void showUsage() {
    System.out.println("Usage: WordClient [RegistryIP]");
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
      WordClient.showUsage();
      return;
    }
    
    try {
      Registry registry = Registry.getRegistry(registryIP);
      Word wordEngineStub = (Word)registry.lookup("WordService");
      wordEngineStub.setWord("Harry");
      System.out.println(wordEngineStub.getWord());
      
      WordStore wordStoreEngineStub = (WordStore)registry.lookup("WordStoreService");
      wordEngineStub = wordStoreEngineStub.storeWord("Potter", wordEngineStub);
      System.out.println(wordEngineStub.getWord());
    } catch (RemoteException440 e) {
      System.out.println("RemoteException440 occured.");
    }
  }
}