package land.chipmunk.parker2991.debugbridge;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;

public class Main {
  String host = "localhost";

  Number port = 22;

  public static void main(String[] args) {
    System.out.println("Meow");
  }

  public void createServer() {

    HttpsServer server = new create(host, port)
  }
}