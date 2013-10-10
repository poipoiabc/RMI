public class WordEngine implements Word {
  private String word;
  
  public String getWord() throws RemoteException440 {
    return this.word;
  }
  
  public void setWord(String word) throws RemoteException440 {
    this.word = word;
  }
}
