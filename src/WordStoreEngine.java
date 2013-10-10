public class WordStoreEngine implements WordStore {
  public Word storeWord(String newWord, Word word) throws RemoteException440 {
    word.setWord(newWord);
    return word;
  }
}