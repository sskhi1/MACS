import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WebFrame extends JFrame {
    private JPanel panel;
    private JTable table;
    private DefaultTableModel model;
    private JButton singleThreadButton;
    private JButton concurrentThreadButton;
    private JTextField threadNum;
    private JLabel running;
    private JLabel completed;
    private JLabel elapsed;
    private JProgressBar progressBar;
    private JButton stopButton;
    private ArrayList<String> urls;
    private ArrayList<WebWorker> threads;
    private long time;
    public Launcher launcher;
    public AtomicInteger runningCount;
    public AtomicInteger completedCount;
    private Semaphore sem;
    private Lock lock;

    public WebFrame() throws IOException {
        super("WebLoader");
        this.time = 0;
        lock = new ReentrantLock();
        threads = new ArrayList<>();

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        model = new DefaultTableModel(new String[]{"url", "status"}, 0);
        urls = new ArrayList<>();
        readUrls();
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane);

        singleThreadButton = new JButton("Single Thread Fetch");
        concurrentThreadButton = new JButton("Concurrent Fetch");
        panel.add(singleThreadButton);
        panel.add(concurrentThreadButton);

        singleThreadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runThreads(1);
            }
        });

        concurrentThreadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numThreads = Integer.parseInt(threadNum.getText());
                runThreads(numThreads);
            }
        });

        threadNum = new JTextField("4", 4);
        threadNum.setMaximumSize(new Dimension(40, 20));
        panel.add(threadNum);

        running = new JLabel("Running: 0");
        completed = new JLabel("Completed: 0");
        elapsed = new JLabel("Elapsed: 0.0");
        panel.add(running);
        panel.add(completed);
        panel.add(elapsed);

        progressBar = new JProgressBar();
        panel.add(progressBar);

        stopButton = new JButton("Stop");
        stopButton.setEnabled(false);
        panel.add(stopButton);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lock.lock();
                launcher.interrupt();
                for (WebWorker w : threads) {
                    w.interrupt();
                }
                lock.unlock();

                singleThreadButton.setEnabled(true);
                concurrentThreadButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });

        add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void runThreads(int numThreads) {
        for (int i = 0; i < urls.size(); i++) {
            model.setValueAt("", i, 1);
        }
        SwingUtilities.invokeLater(() -> {
            stopButton.setEnabled(true);
            singleThreadButton.setEnabled(false);
            concurrentThreadButton.setEnabled(false);
            progressBar.setMaximum(urls.size());
            running.setText("Running: 0");
            completed.setText("Completed: 0");
            elapsed.setText("Elapsed: 0.0");
            progressBar.setValue(0);
        });

        launcher = new Launcher(numThreads);
        time = System.currentTimeMillis();
        launcher.start();
    }

    public void update(String status, int idx) {
        launcher.updateRunning();
        SwingUtilities.invokeLater(() -> {
            completed.setText("Completed: " + completedCount);
            progressBar.setValue(completedCount.get());
            model.setValueAt(status, idx, 1);
        });
        sem.release();
    }

    public void checkDone() {
        long elapsedTime = System.currentTimeMillis() - time;
        double totalTime = (double) elapsedTime / 1000;
        if (runningCount.get() == 0) {
            SwingUtilities.invokeLater(() -> {
                singleThreadButton.setEnabled(true);
                concurrentThreadButton.setEnabled(true);
                stopButton.setEnabled(false);
                elapsed.setText("Elapsed: " + totalTime);
            });
        }
    }

    public class Launcher extends Thread {
        private WebFrame frame;
        private int numThreads;

        public Launcher(int numThreads) {
            this.numThreads = numThreads;
            threads = new ArrayList<>();
            runningCount = new AtomicInteger(0);
            completedCount = new AtomicInteger(0);
            sem = new Semaphore(numThreads);
            frame = WebFrame.this;
        }

        @Override
        public void run() {
            runningCount.incrementAndGet();
            updateRunning();
            for (int i = 0; i < urls.size(); i++) {
                try {
                    sem.acquire();
                    lock.lock();

                    WebWorker w = new WebWorker(urls.get(i), i, frame);
                    threads.add(w);
                    w.start();

                    lock.unlock();
                    if (isInterrupted()) {
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            runningCount.decrementAndGet();
            updateRunning();
            checkDone();
        }

        public synchronized void updateRunning() {
            if(runningCount.get() > 1){
                SwingUtilities.invokeLater(() -> running.setText("Running: " + (runningCount.get() - 1)));
            }else{
                SwingUtilities.invokeLater(() -> running.setText("Running: " + runningCount));
            }
        }
    }

    private void readUrls() throws IOException {
        BufferedReader br;
        br = new BufferedReader(new FileReader("links.txt"));
        String line;
        while (true) {
            line = br.readLine();
            if (line == null) break;
            urls.add(line);
            model.addRow(new String[]{line, ""});
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        new WebFrame();
    }
}
