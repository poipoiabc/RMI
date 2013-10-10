public interface WordStore extends Remote440 {
  public Word storeWord(String newWord, Word word) throws RemoteException440;
}