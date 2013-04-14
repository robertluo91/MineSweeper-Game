package autograder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import minesweeper.server.MinesweeperServer;

public class TestUtil {

    // ...
    // ...
    // ...
    // ...
    // ...

    /**
     * Return the absolute path of the specified file resource on the current Java classpath.
     * Throw an exception if a valid path to an existing file cannot be produced for any reason.
     */
    public static String getResourcePathName(String fileName) throws IOException {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null)
          throw new IOException("Failed to get classloader");
      URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
      if (url == null)
          throw new IOException("Failed to locate resource " + fileName);
      File file;
      try {
    	  file = new File(url.toURI());
      } catch (URISyntaxException e) {
          throw new IOException("Invalid resource URI type: " + e);
      }
      String ret = file.getAbsolutePath();
      if (!new File(ret).exists())
          throw new IOException("File " + ret + " does not exist");
      return ret;
    }

    // ...
    // ...
    // ...
    // ...
    // ...
    // ...
    // ...

    public static void startServer(boolean debug, String boardFileResource) {
        String resourcePathName;
        try {
            resourcePathName = getResourcePathName("autograder/resources/" + boardFileResource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        startServer(new String[] { debug ? "true" : "false", "-f", resourcePathName });
    }

    // ...
    // ...
    // ...

    public static Socket connect() throws IOException {
        int port = 4444;
        String portProp = System.getProperty("minesweeper.customport");
        if (portProp != null) {
            port = Integer.parseInt(portProp);
        }
        Socket ret = null;
        final int MAX_ATTEMPTS = 50;
        int attempts = 0;
        do {
            try {
                ret = new Socket("127.0.0.1", port);
            } catch (ConnectException ce) {
                try {
                    if (++attempts > MAX_ATTEMPTS)
                        throw new IOException("Exceeded max connection attempts", ce);
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    throw new IOException("Unexpected InterruptedException", ie);
                }
            }
        } while (ret == null);
        ret.setSoTimeout(3000);
        return ret;
    }

    private static void startServer(String args[]) {
        final String myArgs[] = args;
        new Thread(new Runnable() {
            public void run() {
                try {
                    MinesweeperServer.main(myArgs);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    public static boolean eqNoSpace(String s1, String s2) {
        //System.out.println("ex "+s1);
        //System.out.println("ac "+s2);
        return s1.replaceAll("\\s+", "").equals(s2.replaceAll("\\s+", ""));
    }
    public static String nextNonEmptyLine(BufferedReader in) throws IOException {
        while (true) {
            String ret = in.readLine();
            if (ret == null || !ret.equals(""))
                return ret;
        }
    }
}