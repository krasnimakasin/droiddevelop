package kn.utils;

public class ConsoleProxy
{
  native private String readAgain(int process);
  native private int createProcess(String exePath);
  native private void freeObject(int obj);
  
  private int process;
  
  public ConsoleProxy(String exePath)
  {
    System.loadLibrary("pasdev");
    process = createProcess(exePath);
  }
  
  public void release()
  {
    //freeObject(process);
  }
  
  public String read()
  {
    return readAgain(process);
  }
}