package BusinessMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import particleswarmnanobots.NanobotPSOUpdateThread;

public class BotJChart extends ApplicationFrame {

    private static final String TITLE = "NanoBots Tracker";
    private static final String START = "Start";
    private static final String STOP = "Stop";
    private static final float MINMAX = 150;
    private static final int COUNT = 2 * 25;
    private static final int COUNT1 = 2 * 60;
    private static final int FAST = 200;
    private static final int SLOW = FAST * 5;
    private static final Random random = new Random();
    private Timer timer;
    private double[] initData;
    NanobotPSOUpdateThread npu;

    public BotJChart(final String title) {
        super(title);
        npu = new NanobotPSOUpdateThread();
        double[] x = new double[2];
        x[0] = 1;
        x[1] = 7;

        double[] y = new double[2];
        y[0] = -1;
        y[1] = 3;

        double[] z = new double[2];
        z[0] = 2;
        z[1] = 6;

        npu.setxCoordinates(x);
        npu.setyCoordinates(y);
        npu.setzCoordinates(z);

        npu.initializeBots();

        initData = npu.getInitXlocation();

        final DynamicTimeSeriesCollection dataset
                = new DynamicTimeSeriesCollection(1, 50, new Second());

        dataset.setTimeBase(new Second(0, 0, 0, 1, 1, 2017));
        dataset.addSeries(gaussianData(), 0, "PSO Data");
        JFreeChart chart = createChart(dataset);
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000, Color.blue));
        chart.getPlot().setBackgroundPaint(Color.BLACK);
        final JButton run = new JButton(STOP);

        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String cmd = e.getActionCommand();
                if (STOP.equals(cmd)) {
                    timer.stop();
                    run.setText(START);
                } else {
                    timer.start();
                    run.setText(STOP);
                }
            }
        });

        final JComboBox combo = new JComboBox();
        combo.addItem("Fast");
        combo.addItem("Slow");
        combo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if ("Fast".equals(combo.getSelectedItem())) {
                    timer.setDelay(FAST);
                } else {
                    timer.setDelay(SLOW);
                }
            }
        });

        this.add(new ChartPanel(chart), BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(run);
        btnPanel.add(combo);
        btnPanel.setBackground(Color.black);
        this.add(btnPanel, BorderLayout.SOUTH);

        timer = new Timer(FAST, new ActionListener() {

            float[] newData = new float[1];
            int i = 0;

            // The PSO Parameters for the NanoBots are executed inside the thread
            // for giving iteration time
            @Override
            public void actionPerformed(ActionEvent e) {
                npu.psoMethod();
                newData[0] = npu.getpBestXlocation()[0];
                System.out.println("The value of the pBest  is " + npu.getpBestXlocation()[0]);
                dataset.advanceTime();
                dataset.appendData(newData);
                i++;
            }

        });
    }

    private float randomValue() {
        return (float) (random.nextGaussian() * MINMAX / 3);
    }

    //for getting the initial values after initializing all the bots
    private float[] gaussianData() {
        float[] a = new float[50];

        for (int i = 0; i < initData.length; i++) {
            a[i] = (float) initData[i];
        }
        return a;
    }

    //Creating the chart with the required values
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
                TITLE, "Time Elapsed", "BOT Location Coordiante", dataset, true, true, false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis domain = plot.getDomainAxis();
        domain.setAutoRange(true);
        ValueAxis range = plot.getRangeAxis();
        range.setRange(-MINMAX, MINMAX);
        return result;
    }

    public void start() {
        timer.start();
    }

    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                BotJChart bjc = new BotJChart(TITLE);
                bjc.pack();
                RefineryUtilities.centerFrameOnScreen(bjc);
                bjc.setVisible(true);
                bjc.start();
            }
        });
    }
}
