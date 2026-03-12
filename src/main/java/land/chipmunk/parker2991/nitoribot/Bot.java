package land.chipmunk.parker2991.nitoribot;
import org.geysermc.mcprotocollib.protocol.MinecraftProtocol;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.session.ClientNetworkSession;
import org.geysermc.mcprotocollib.network.factory.ClientNetworkSessionFactory;

public class Bot extends SessionAdapter {
  public ClientNetworkSession session;

  //public final Config.bots options;

  public String host = "opt.chipmunk.land";

  public int port = 25565;

  public String username = "meow";
  
  public final Object obj = new Object();

  public Bot (Config.Options options, Config config) {
    System.out.println(options);

    final MinecraftProtocol protocol = new MinecraftProtocol(options.username);

    session = ClientNetworkSessionFactory.factory()
      .setAddress(
         options.host,
         options.port
      )
      .setProtocol(protocol)
      .create();
      session.addListener(this);
      this.connect();
  };

  public void connect () {
    session.connect();
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
