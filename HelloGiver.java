public class HelloGiver implements Hello {
  private int count = 0;

  public String hello(Integer arg) {
    count++;
    return "Hello world! NO=" + count + " ARG=" + arg;
  }
}
