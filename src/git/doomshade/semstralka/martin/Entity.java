package git.doomshade.semstralka.martin;

/**
 * Třída pro supermarkety/továrny jako entita (uložiště pro index v poli a hodnotu)
 *
 * @author Martin Jakubašek
 */
public class Entity {
    public final int index;
    public final int value;

    /**
     * Vytvoří novou entitu (supermarket nebo továrna), u které si drží její index a hodnotu
     *
     * @param index index
     * @param value hodnota
     */
    public Entity(int index, int value) {
        this.index = index;
        this.value = value;
    }
}
