package git.doomshade.semstralka.martin.gui;

import git.doomshade.semstralka.Main;
import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.martin.DayData;
import git.doomshade.semstralka.martin.Simulation;
import git.doomshade.semstralka.martin.SimulationData;
import git.doomshade.semstralka.smrha.save.DataSaver;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * GUI prvky simulace
 *
 * @author Martin Jakubašek
 */
public class SimulationWindow {

    private final JFrame frame;
    private Simulation simulation;

    private String consoleText;
    private int offset = 0;

    public SimulationWindow(Simulation simulation) {
        this.simulation = simulation;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame = create();
    }

    /**
     * Vrátí GUI okno
     *
     * @return GUI okno
     */
    public JFrame getFrame() {
        return this.frame;
    }

    //-- FRAME components

    //-- nacteni souboru

    /**
     * Načte složku
     */
    private JPanel getFileLoader() {
        JPanel panel = new JPanel();

        JButton openFCH = new JButton("Vybrat soubor");
        openFCH.addActionListener(actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    Storage storage = Main.read(selectedFile);
                    this.simulation = new Simulation(storage);
                    isFileLoaded(true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        panel.add(openFCH);
        return panel;
    }

    /**
     * Načte složku
     *
     * @param is složka
     */
    private void isFileLoaded(boolean is) {
        enableBtns(is);
        if (is) {
            consoleText = "Složka úspěšně načtena. \n\n" +
                    "Simulaci zahájíte stisknutím tlačítka 'simuluj další den', nebo 'simuluj zbylé/všechny dny'";
            console.setText(consoleText);
            simBasicInfo[0].setText("Počet supermarketů: " + simulation.storage.pocetSupermarketu);
            simBasicInfo[1].setText("Počet továren: " + simulation.storage.pocetTovaren);
            simBasicInfo[2].setText("Počet druhů zboží: " + simulation.storage.pocetDruhuZbozi);
            simBasicInfo[3].setText("Počet dní simulace: " + simulation.storage.pocetDni);
        }
    }

    // -- ovládání

    private JLabel currentDay;
    private JLabel[] simBasicInfo;
    private JTextArea console;
    private JButton[] controlBtns;
    private JButton fileSaver;

    /**
     * Změní ukazatel dne
     *
     * @param day ukazatel dne
     */
    private void changeCurrentDay(int day) {
        currentDay.setText("Den simulace: " + (day + offset));
        currentDay.paintImmediately(currentDay.getVisibleRect());
    }

    /**
     * Zapne/Vypne tlačítka
     *
     * @param isEnabled Zapne/Vypne tlačítka
     */
    private void enableBtns(boolean isEnabled) {
        for (JButton btn : controlBtns) {
            btn.setEnabled(isEnabled);
            controlBtns[3].setEnabled(false);
            controlBtns[1].setEnabled(false);
        }
    }

    /**
     * Vytvoří UI
     *
     * @return UI
     */
    private JFrame create() {
        JFrame jFrame = new JFrame();

        String APP_TITLE = "Simulace přepravy zboží";
        jFrame.setTitle(APP_TITLE);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        jFrame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.add(getFileLoader());
        panel.add(getFinalBtns());
        jFrame.add(panel, BorderLayout.NORTH);

        jFrame.add(createSouthPanel(), BorderLayout.SOUTH);

        jFrame.add(createMiddlePanel(), BorderLayout.CENTER);

        return jFrame;
    }

    /**
     * Vrátí info o dni
     *
     * @return info o dni
     */
    private JPanel getSimInfo() {
        JPanel root = new JPanel();
        root.setLayout(new GridLayout(0, 2));

        JLabel supermarkets = new JLabel("");
        JLabel factories = new JLabel("");
        JLabel goods = new JLabel("");
        JLabel days = new JLabel("");

        simBasicInfo = new JLabel[]{supermarkets, factories, goods, days};

        for (JLabel label : simBasicInfo) {
            root.add(label);
        }

        return root;
    }

    /**
     * Vrátí info o dni
     *
     * @return info o dni
     */
    private JPanel getCurrDayInfo() {
        JPanel root = new JPanel();

        console = new JTextArea();
        console.setEditable(false);
        console.setFont(new Font("Arial", Font.ITALIC, 12));
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        consoleText = "Pro zahájení simulace vyberte soubor s daty";
        console.setText(consoleText);

        JScrollPane areaScrollPane = new JScrollPane(console);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane.setPreferredSize(new Dimension(400, 300));

        root.add(areaScrollPane);

        root.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        return root;
    }

    /**
     * Vytvoří prostřední panel
     *
     * @return prostřední panel
     */
    private JPanel createMiddlePanel() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        root.add(getSimInfo());
        root.add(getCurrDayInfo());

        return root;
    }

    /**
     * Vytvoří dolní panel
     *
     * @return dolní panel
     */
    private JPanel createSouthPanel() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        JPanel cd = new JPanel();
        cd.setAlignmentX(Component.CENTER_ALIGNMENT);
        currentDay = new JLabel("simulace nebyla spuštěna");
        cd.add(currentDay);

        JPanel btns = getBtns();

        root.add(cd);
        root.add(btns);

        return root;
    }

    /**
     * Vytvoří tlačítko pro sobour
     *
     * @return tlačitko pro vytváření souboru$
     */
    private JPanel getFinalBtns() {
        JPanel root = new JPanel();

        fileSaver = new JButton("Vytvoř výstupní soubor");
        root.add(fileSaver);
        fileSaver.setEnabled(false);

        fileSaver.addActionListener(
                actionEvent -> {
                    DataSaver dataSaver = new DataSaver();
                    SimulationData simulationData = new SimulationData(simulation);
                    try {
                        File file = dataSaver.save(simulationData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        return root;
    }

    /**
     * Vytvoří tlačítka
     *
     * @return tlačítka
     */
    private JPanel getBtns() {

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        JButton nextDayBtn = new JButton("simuluj další den");
        nextDayBtn.addActionListener(actionEvent -> {
            if (offset == 0) {
                simulation.simulateNextDay();
                changeCurrentDay(simulation.getCurrentDay());
                chageConsoleOutput();
                controlBtns[1].setEnabled(true);
                if (simulation.simEnd) {
                    controlBtns[0].setEnabled(true);
                    controlBtns[2].setEnabled(false);
                    controlBtns[3].setEnabled(false);
                    controlBtns[4].setEnabled(false);
                    fileSaver.setEnabled(true);
                }
            } else {
                offset++;
                changeCurrentDay(simulation.getCurrentDay());
                chageConsoleOutput();
            }
        });
        JButton prevDayBtn = new JButton("předchozí den");
        prevDayBtn.addActionListener(actionEvent -> {
            if (!(simulation.getCurrentDay() - 1 + (offset - 1) < 0)) {
                offset--;
                changeCurrentDay(simulation.getCurrentDay());
                chageConsoleOutput();
            } else {
                controlBtns[1].setEnabled(false);
            }
        });

        //-- rest of days
        JButton restOfDaysBtn = new JButton("simuluj všechny dny");
        Timer timer = new Timer(1000, e -> {
            simulation.simulateNextDay();
            changeCurrentDay(simulation.getCurrentDay());
            chageConsoleOutput();
            controlBtns[0].setEnabled(false);
            controlBtns[1].setEnabled(false);
            controlBtns[2].setEnabled(false);
            controlBtns[3].setEnabled(true);
            controlBtns[4].setEnabled(false);
            if (simulation.simEnd) {
                ((Timer) e.getSource()).stop();
                controlBtns[1].setEnabled(true);
                controlBtns[3].setEnabled(false);
                fileSaver.setEnabled(true);
            }
        });
        timer.setRepeats(true);
        restOfDaysBtn.addActionListener(actionEvent -> {
            timer.start();
        });
        //--

        JButton stopSim = new JButton("\u23EF");
        stopSim.addActionListener(actionEvent -> {
            timer.stop();
            controlBtns[0].setEnabled(true);
            controlBtns[1].setEnabled(true);
            controlBtns[2].setEnabled(true);
            controlBtns[3].setEnabled(false);
            controlBtns[4].setEnabled(true);
        });

        JButton skipToEndBtn = new JButton("\u23ED");
        skipToEndBtn.addActionListener(actionEvent -> {

            synchronized (LOCK) {
                simulation.simulateRestOfDays();
                changeCurrentDay(simulation.getCurrentDay());
                chageConsoleOutput();
                controlBtns[0].setEnabled(false);
                controlBtns[1].setEnabled(true);
                controlBtns[4].setEnabled(false);
                controlBtns[3].setEnabled(false);
                controlBtns[2].setEnabled(false);
                fileSaver.setEnabled(true);
            }
        });

        controlBtns = new JButton[]{nextDayBtn, prevDayBtn, restOfDaysBtn, stopSim, skipToEndBtn};
        for (JButton btn : controlBtns) {
            btn.setEnabled(false);
        }

        buttons.add(prevDayBtn);
        buttons.add(nextDayBtn);
        buttons.add(restOfDaysBtn);
        buttons.add(stopSim);
        buttons.add(skipToEndBtn);

        return buttons;
    }

    //-- text

    /**
     * Update konzole
     */
    private void chageConsoleOutput() {
        String output = getPrice();

        if (!simulation.simSuccessful) {
            output += " \n\n Simulace selhala! Nedostatek zboží pro uzásobení supermarketů pro " + simulation.getCurrentDay() + " den simulace \n";
            output += getNegativeStocks();
        }
        if (simulation.simEnd) {
            output += " \n\n Simulace dokončena!";
            if (!simulation.simSuccessful) {
                output += " Simulace neúspěšná";
            } else {
                output += " Simulace úspěšná";
            }


        }

        output += "\n\n" + getTransportRoutes();
        output += "\n\n" + getStocks() + "\n";

        console.setText(output);
        console.paintImmediately(currentDay.getVisibleRect());
    }

    /**
     * Vrátí cenu
     *
     * @return cena
     */
    private String getPrice() {
        StringBuilder output = new StringBuilder("--Cena--");
        DayData currDay = simulation.daysData[simulation.getCurrentDay() - 1 + offset];
        if (currDay == null) {
            return "" + simulation.getCurrentDay();
        }

        int currPrice = Arrays.stream(currDay.optimalPrice).sum();
        output.append("\n cena přepravy pro aktuální den: ").append(currPrice);

        int priceOfSim = 0;
        for (DayData data : simulation.daysData) {
            if (data == null) {
                continue;
            }
            priceOfSim += Arrays.stream(data.optimalPrice).sum();
        }

        if (!simulation.simEnd) {
            output.append("\n Celková cena přepravy pro simulované dny: ").append(priceOfSim);
        } else {
            output.append("\n Celková cena přepravy: ").append(priceOfSim);
        }

        if (simulation.simEnd) {
            int i = 0;
            for (DayData data : simulation.daysData) {
                i++;
                if (data == null) {
                    continue;
                }
                output.append("\n Cena přepravy pro den ").append(i).append(": ").append(Arrays.stream(data.optimalPrice).sum());
            }
        }

        return output.toString();
    }

    /**
     * Vrátí přepravu
     *
     * @return přeprava
     */
    private String getTransportRoutes() {
        DayData currDay = simulation.daysData[simulation.getCurrentDay() - 1 + offset];
        if (currDay == null) {
            return "" + simulation.getCurrentDay();
        }

        StringBuilder output = new StringBuilder("Přepravované zboží: \n");
        int[][][] routes = currDay.getTransportationMatrices(); //z,d,s
        for (int d = 0; d < routes[0].length; d++) {
            for (int s = 0; s < routes[0][0].length; s++) {
                for (int z = 0; z < routes.length; z++) {
                    if (routes[z][d][s] <= 0) {
                        continue;
                    }
                    String vehicle = "na kole";
                    if (routes[z][d][s] >= 5) {
                        vehicle = "autem";
                    }
                    if (routes[z][d][s] >= 10) {
                        vehicle = "vlakem";
                    }
                    output.append("\n továrna").append(d).append("---[").append(routes[z][d][s]).append("ks zboží").append(z).append("]---> supermarket").append(s).append(" (").append(vehicle).append(")");
                }
            }
        }

        return String.valueOf(output);
    }

    /**
     * Vrátí zásoby
     *
     * @return zásoby
     */
    private String getStocks() {
        DayData currDay = simulation.daysData[simulation.getCurrentDay() - 1 + offset];
        if (currDay == null) {
            return "" + simulation.getCurrentDay();
        }

        StringBuilder output = new StringBuilder("Zásoby supermarketů: \n");
        int[][] stocks = currDay.getStocks();
        for (int s = 0; s < stocks[0].length; s++) {
            for (int z = 0; z < stocks.length; z++) {
                output.append("\n supermarket").append(s).append(" zásoby zboží").append(z).append(": ").append(stocks[z][s]);
            }
        }

        return String.valueOf(output);
    }

    /**
     * Vrátí zásoby negativní
     *
     * @return zásoby negativní
     */
    private String getNegativeStocks() {
        DayData currDay = simulation.daysData[simulation.getCurrentDay() - 1 - offset];
        if (currDay == null) {
            return "" + simulation.getCurrentDay();
        }

        StringBuilder output = new StringBuilder("Nedostatečné zásoby: \n");
        int[][] stocks = currDay.getStocks();
        for (int s = 0; s < stocks[0].length; s++) {
            for (int z = 0; z < stocks.length; z++) {
                if (stocks[z][s] < 0) {
                    output.append("\n supermarket").append(s).append(" zásoby zboží").append(z).append(": ").append(stocks[z][s]);
                }
            }
        }

        return String.valueOf(output);
    }

    private static final Object LOCK = new Object();
}
