package uptackomsai.chatupt.network;

import java.io.PrintWriter;

public interface ServerModule {
    void handleRequest(String type, String content, PrintWriter out);
}
