package git.doomshade.semstralka.martin.gui;

import git.doomshade.semstralka.martin.Simulation;

/**
 * Vytvoří GUI
 *
 * @author Martin Jakubašek
 */
public class App {

    private Simulation simulation;

    /**
     * Zahájí GUI
     */
    public void startApp() {
        initGUI();
    }

    /**
     * Init GUI
     */
    private void initGUI() {
        SimulationWindow sw = new SimulationWindow(simulation);


        sw.getFrame().setVisible(true);
        sw.getFrame().pack();
        sw.getFrame().setResizable(false);
    }
}
