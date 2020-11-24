package git.doomshade.semstralka.martin.gui;

import git.doomshade.semstralka.impl.graph.Storage;
import git.doomshade.semstralka.martin.Simulation;

public class App {

    private SimulationWindow sw;
    private Simulation simulation;

    public void startApp() {
        initGUI();
    }

    private void initGUI() {
        sw = new SimulationWindow(simulation);


        sw.getFrame().setVisible(true);
        sw.getFrame().pack();
        sw.getFrame().setResizable(false);
    }
}
