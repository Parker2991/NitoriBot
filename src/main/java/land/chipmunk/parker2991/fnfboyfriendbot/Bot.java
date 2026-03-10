package land.chipmunk.parker2991.fnfboyfriendbot;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;

public class Bot extends SessionAdapter {
  public ClientNetworkSession session;

  public String host = "opt.chipmunk.land";

  public int port = 25565;

  public String username = "meow";
  
  public final Object obj = new Object();

  public void Bot () {
    System.out.println("meow");
    final MinecraftProtocol protocol = new MinecraftProtocol(this.username);

    session = ClientNetworkSessionFactory.factory()
      .setAddress(
         this.host,
         this.port
      )
      .setProtocol(protocol)
      .create();
    session.addListener(this);
    session.connect();
    try {
      synchronized(obj) {
        obj.wait();
      };
    } catch (final InterruptedException ignored) {

    }
  };

  public void connect () {
    try {
      synchronized(obj) {
        obj.wait();
      };
    } catch (final InterruptedException ignored) {

    }
  }

  public void disconnect (DisconnectedEvent event) {
    System.out.println(event);
  }
}