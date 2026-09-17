package land.chipmunk.parker2991.nitoribot.boot;

import org.geysermc.mcprotocollib.network.ProxyInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class GetProxiesList {
  public ProxyInfo randomProxyIp () throws IOException {
    Path proxiesPath = Paths.get("proxies.txt");

    int countLines = Math.round(
      Files.lines(proxiesPath).count()
    );

    Random random = new Random();

    int randomIndex = random.nextInt(countLines);

    List<String> lines = Files.lines(proxiesPath).toList();//.toList();

    String[] ip = lines.stream()
      .skip(randomIndex)
      .findAny()
      .get()
      .split(":");

    InetSocketAddress address = new InetSocketAddress(
      ip[0],
      Integer.parseInt(ip[1])
    );

    return new ProxyInfo(
      ProxyInfo.Type.SOCKS5,
      address
    );
  }
}
