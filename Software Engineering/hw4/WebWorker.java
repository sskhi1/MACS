import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {
    private String urlString;
    private int threadNum;
    private WebFrame webFrame;
    private String status;

    public WebWorker(String url, int idx, WebFrame frame) {
        this.status = "";
        this.urlString = url;
        this.threadNum = idx;
        this.webFrame = frame;
    }

    @Override
    public void run() {
        webFrame.runningCount.incrementAndGet();
        webFrame.launcher.updateRunning();
        download();
        webFrame.runningCount.decrementAndGet();
        webFrame.completedCount.incrementAndGet();
        webFrame.update(status, threadNum);
        webFrame.checkDone();
    }


    void download() {
        InputStream input = null;
        StringBuilder contents = null;
        long startTime = System.currentTimeMillis();
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.
            connection.setConnectTimeout(5000);

            connection.connect();
            input = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            long currentTime = System.currentTimeMillis();
            status = new SimpleDateFormat("HH:mm:ss").format(new Date(currentTime)) + " " + (currentTime - startTime) + " ms " + contents.length() + " bytes";
        }
        // Otherwise control jumps to a catch...
        catch (MalformedURLException ignored) {
            status = "err";
        } catch (InterruptedException exception) {
            // deal with interruption
            status = "interrupted";
        } catch (IOException ignored) {
            status = "err";
        }
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try {
                if (input != null) input.close();
            } catch (IOException ignored) {
            }
        }
    }
}
