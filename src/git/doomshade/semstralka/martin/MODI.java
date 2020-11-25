package git.doomshade.semstralka.martin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * --MODIFIED DISTRIBUTION METHOD--
 * Třída reprezentuje MODI algoritmus tj. dokáže zjistit zda zadané realizovatelné řešení je optimální.
 * Pokud by zadané realizovatelné řešení nebylo optimální, nalezne optimální řešení a to vrátí.
 * <p>
 * Je počítáno s tím že všechna vstupní data JSOU ve správném tvaru a data tedy nejsou nijak ověřována, až na možnost, kdy
 * by průběhem algoritmu nebo již při zadání došlo k degenerativnímu realizovatelnému řešení
 *
 * @author Martin Jakubašek
 */
public class MODI {

    /**
     * Matice hran, tedy reprezentuje kudy se přepravuje zboží a kolik se ho přepravuje
     * 20 20 0 0 -> 40
     * např.  0 10 50 0 -> 60
     * 0 0 0 50 -> 50
     * - - - -
     * 20 30 50 50
     */
    final double[][] arcMatrix;
    /**
     * Matic cen převozu mezi S a D
     * 4 6 8 8
     * např.    6 8 6 7
     * 5 7 6 8
     */
    final int[][] costMatrix;

    final double[] u;
    final double[] v;

    /**
     * Vytvoří novou instanci MODI algoritmu, ted instanci sloužící pro právě jeden příklad
     * Je potřeba zadat správná data, pro správnou funkčnost algoritmu
     *
     * @param arcMatrix  matice hran, reprezentující 1 realizovatelné řešení (tj. správné řešení, ale ne optimální)
     * @param costMatrix matice cen převozu
     */
    public MODI(double[][] arcMatrix, int[][] costMatrix) {
        this.arcMatrix = arcMatrix;
        this.costMatrix = costMatrix;

        // inicializuj velikosti u a v polí
        int xSize = arcMatrix[0].length;
        int ySize = arcMatrix.length;
        u = new double[ySize];
        v = new double[xSize];
    }

    /**
     * Zaplní u a v pole hodnotami reprezentující "nealokované" proměnné
     * a u0 nastaví na 0, dle postupu řešení algoritmu
     */
    void initUV() {
        Arrays.fill(u, Double.POSITIVE_INFINITY);
        Arrays.fill(v, Double.POSITIVE_INFINITY);
        u[0] = 0;
    }

    //rozděl matici na 2 části -> alokovanou (hrany jsou používány pro přepravu) a nealokovanou část
    List<int[]> allocated;
    List<int[]> unallocated;

    /**
     * Přířadí hrany grafu buďto do přiřazené nebo nepřiřazené části
     */
    void getAllocations() {
        allocated = new ArrayList<>();
        unallocated = new ArrayList<>();

        for (int y = 0; y < arcMatrix.length; y++) {
            for (int x = 0; x < arcMatrix[0].length; x++) {
                if (arcMatrix[y][x] > 0) {
                    allocated.add(new int[]{x, y});
                } else {
                    unallocated.add(new int[]{x, y});
                }
            }
        }
    }

    /**
     * Metoda zjistí zda byly přiřazeny všechny prvky v 'u' a 'v' poli
     *
     * @return true, pokud všechny prvky jsou přiřazeny
     */
    boolean allUVsSet() {
        for (double num : u) {
            if (num == Double.POSITIVE_INFINITY) {
                return false;
            }
        }
        for (double num : v) {
            if (num == Double.POSITIVE_INFINITY) {
                return false;
            }
        }

        return true;
    }

    /**
     * Vypočítá vylepšující ceny použítí nepoužitých hran
     *
     * @return vylepšující ceny nepoužitých hran
     */
    int[] calculateNetIncrease() {
        int[] netIncrease = new int[unallocated.size()];

        for (int i = 0; i < unallocated.size(); i++) {
            int x = unallocated.get(i)[0];
            int y = unallocated.get(i)[1];

            netIncrease[i] = (int) (costMatrix[y][x] - (u[y] + v[x]));
        }

        return netIncrease;
    }

    /**
     * Zknotroluj zda existuje negativní net increase
     *
     * @param netIncrease net increase
     * @return true pokud existuje jinak false
     */
    boolean existNegNetIncrease(int[] netIncrease) {
        boolean neg = false;
        for (int num : netIncrease) {
            if (num < 0) {
                neg = true;
                break;
            }
        }
        return neg;
    }

    /**
     * Najde (pokud existuje) největší zlepšení nepoužitých cest
     *
     * @return koordinace lepší cesty (záporné číslo), 0 pokud není lepší cesta, nebo cenově stejná cesta
     */
    int[] findLargestNegativeIncrease(int[] netIncrease) {
        int minNegative = 0; // index
        for (int i = 0; i < netIncrease.length; i++) {
            if (netIncrease[i] <= 0 && netIncrease[i] < netIncrease[minNegative]) {
                minNegative = i;
            }
        }

        return unallocated.get(minNegative);
    }

    /**
     * Metoda najde cyklus (pokud existuje) ze zadaných souřadnic.
     * Následně najde a vrátí hodnoty všech hran cyklu.
     *
     * @param x souřadnice x;
     * @param y souřadnice y;
     * @return list [pozice x, pozice y, přeprava]
     */
    List<double[]> findCycleValues(int x, int y) {
        double value = arcMatrix[y][x];
        arcMatrix[y][x] = 1; // nic tam nebude, takže si tam bez problému přidáme 0

        CycleDetection cd = new CycleDetection(arcMatrix);
        ArrayList<int[]> cycle = (ArrayList<int[]>) cd.detectCycle(x, y);
        if (cycle == null) {
            return null;
        }
        // než něco začneme dělat vrátíme "arcMatrix" doi původního stavu
        arcMatrix[y][x] = value;
        // teď se můžeme zabývat cyklem
        ArrayList<double[]> cycleValues = new ArrayList<>();
        for (int i = 0; i < cycle.size(); i++) {
            int xPos = cycle.get(i)[0];
            int yPos = cycle.get(i)[1];

            int neg = (i % 2 == 0) ? 1 : -1;
            cycleValues.add(new double[]{xPos, yPos, arcMatrix[yPos][xPos] * neg});
        }

        return cycleValues;
    }

    /**
     * Vygeneruj nové řešení úlohy
     *
     * @param cycleValues hodnoty cyklu a pozice cyklu
     */
    void generateNewSolution(List<double[]> cycleValues) {
        // flags
        boolean negativeEpsilon = false;

        // najdi mi theta číslo (maximální číslo co mohu odečít ze všech záporných prvků cyklu, tak abych nepřepravoval záporné množství prvků)
        double theta = cycleValues.get(1)[2]; // vezmeme lichá čísla (jsou VŽDY záporná)
        for (double[] val : cycleValues) {
            if (val[2] == Double.NEGATIVE_INFINITY) {
                negativeEpsilon = true;
                break;
            }

            if (val[2] < 0 && val[2] > theta) {
                theta = val[2];
            }
        }
        checkIfNegative(cycleValues, negativeEpsilon, theta);
    }

    /**
     * Nejprve zjistí zda je epsilon negativní -> pokud ano měníme jen pozici prvů a nic nepočítáme
     * Pokud není negativní najdeme nejmenší pozitivní hodnotu a tu odečtemě od lichých prvků cyklu
     * <p>
     * metody byli vygenerovány kvůli
     * PMD... upřímně nemám tušení co dělají a tedy pokud je v algo chyba nelze ji už napravit
     *
     * @param cycleValues     cyklus
     * @param negativeEpsilon je negativní epsilon?
     * @param theta           theta
     */
    private void checkIfNegative(List<double[]> cycleValues, boolean negativeEpsilon, double theta) {
        if (!negativeEpsilon) {
            // karanténa --
            double v = theta * -1;// theta musí být pozitivní :(
            // karanténa --
            // máme theta -> teď přičti nebo odečti theta od všech prvků cyklu
            for (double[] val : cycleValues) {
                if (val[2] == Double.POSITIVE_INFINITY) {
                    int xPos = (int) val[0];
                    int yPos = (int) val[1];
                    arcMatrix[yPos][xPos] = 0;
                }
            }

            for (int i = 0; i < cycleValues.size(); i++) {
                int x = (int) cycleValues.get(i)[0];
                int y = (int) cycleValues.get(i)[1];

                int mul = (i % 2 == 0) ? 1 : -1;
                arcMatrix[y][x] += v * mul;
            }
        } else {
            int xStart = (int) cycleValues.get(0)[0];
            int yStart = (int) cycleValues.get(0)[1];
            arcMatrix[yStart][xStart] = Double.POSITIVE_INFINITY;
            for (double[] val : cycleValues) {
                if (val[2] == Double.NEGATIVE_INFINITY) {
                    int xPos = (int) val[0];
                    int yPos = (int) val[1];
                    arcMatrix[yPos][xPos] = 0;
                }
            }
        }
    }

    //-- pro degenerative solution (tj. máme < m+n-1 přiřazených hran)

    /**
     * Vypočítá epsilony
     *
     * @return true-> ppokud se podařilo vypočítat epsilony
     */
    boolean calculateEpsilons() {
        // TODO předělat
        //ArrayList<Integer> unalocatedUs = new ArrayList<>();
        ArrayList<Integer> unalocatedUs = new ArrayList<>(); //procházíme všechna uS
        // najdi nepřiřazená u -> tedy u == Inf
        allocateUs(unalocatedUs);

        // projdi všechny řádky s nepřiřazenými u a přiřaď epsilon tak aby nevznikal cyklus
        ArrayList<int[]> bannedPos = new ArrayList<>();
        // potřeba přidat další podmínku
        int[] maxBans = new int[unalocatedUs.size()]; // pro každý řádek jeho max zabanovaných pozic
        allocateMatrix(unalocatedUs, maxBans);

        CycleDetection cd = new CycleDetection(arcMatrix);
        int epsilons = 0;
        int step = 0;
        while (true) {
            // zkus každému u přiřadit epsilon, tak aby nevznikal cyklus
            for (int index : unalocatedUs) {
                AtomicInteger banCount = new AtomicInteger(0);
                for (int x = 0; x < arcMatrix[0].length; x++) {
                    boolean banned = false;
                    // jedná se o ilegální pozici ?
                    banned = isBanned(bannedPos, index, x, banCount);

                    if (checkBanCount(maxBans, step, index, banCount.get())) {
                        return false;
                    }

                    if (checkBanAndCycle(bannedPos, cd, index, x, banned)) {
                        continue;
                    }
                    //uspěšně si přiřadil epsilon
                    ++epsilons;
                    break;
                }

                if (epsilons == (arcMatrix.length + arcMatrix[0].length - 1) - allocated.size()) {
                    return true;
                }
            }

            step++;
            // nenašel jsi všechna epsilon
            // resetuj epsilon
            epsilons = 0;
            nullifyMatrix(unalocatedUs);
        }
    }

    //-- PMD territory -- Error Logic not found... --  Enter At Your Own Risk --

    /**
     * Zkontroluje zda pozice je zabanovaná a kolik jich je
     *
     * @param bannedPos zabanované pozice
     * @param index     index
     * @param x         x
     * @param banCount  pořet banů
     * @return bany
     */
    private boolean isBanned(List<int[]> bannedPos, int index, int x, AtomicInteger banCount) {
        boolean banned = false;
        for (int[] ban : bannedPos) {
            if (x == ban[0] && index == ban[1]) {
                banned = true;
                //++banCount;
                banCount.set(banCount.get() + 1);
            }
        }
        return banned;
    }

    /**
     * @param maxBans  maxBans
     * @param step     step
     * @param index    index
     * @param banCount banCount
     * @return pmd, já vážne nevím
     */
    private boolean checkBanCount(int[] maxBans, int step, int index, int banCount) {
        return banCount == maxBans[index] && step != 0;
    }

    /**
     * Zkontroluj zda ban nebo cyklu
     *
     * @param bannedPos zabanované pozice
     * @param cd        cycleDetection
     * @param index     index
     * @param x         x
     * @param banned    ban
     * @return true pokud ban nebo cyklus
     */
    private boolean checkBanAndCycle(List<int[]> bannedPos, CycleDetection cd, int index, int x, boolean banned) {
        if (banned) {
            return true;
        }

        //pokud není již na pozici jiné číslo, zkus tam dát epsilon
        if (arcMatrix[index][x] != 0) {
            return true;
        }
        //zkus přiřadit epsilon
        double value = arcMatrix[index][x];
        arcMatrix[index][x] = Double.POSITIVE_INFINITY;
        return ifCycleDo(bannedPos, cd, index, x, value);
    }

    /**
     * vynuluje poziva vynuluje pozice s maticí kde je 0
     *
     * @param unalocatedUs nealokovaná u
     */
    private void nullifyMatrix(Collection<Integer> unalocatedUs) {
        for (int index : unalocatedUs) {
            for (int x = 0; x < arcMatrix[0].length; x++) {
                if (arcMatrix[index][x] == Double.POSITIVE_INFINITY) {
                    arcMatrix[index][x] = 0;
                }
            }
        }
    }

    /**
     * Pokud jsi provedl cyklus přidej do zabanovaných pozic
     *
     * @param bannedPos zabanované pozice
     * @param cd        cyklus
     * @param index     index
     * @param x         x
     * @param value     hodnota
     * @return true -> pokud byl cyklus jinak false
     */
    boolean ifCycleDo(List<int[]> bannedPos, CycleDetection cd, int index, int x, double value) {
        if (cd.detectCycle(x, index) != null) {
            // detekoval jsem cyklus
            bannedPos.add(new int[]{x, index});
            arcMatrix[index][x] = value;
            return true;
        }
        return false;
    }

    /**
     * Přiradá nealokované u
     *
     * @param unalocatedUs unalocated u
     */
    void allocateUs(List<Integer> unalocatedUs) {
        for (int i = 0; i < u.length; i++) {
            unalocatedUs.add(i);
        }
    }

    /**
     * okomentovat
     *
     * @param unalocatedUs nealokované u
     * @param maxBans      max bany
     */
    void allocateMatrix(Collection<Integer> unalocatedUs, int[] maxBans) {
        for (int u = 0; u < unalocatedUs.size(); u++) {
            for (int x = 0; x < arcMatrix[0].length; x++) {
                if (arcMatrix[u][x] == 0) {
                    maxBans[u]++;
                }
            }
        }
    }

    /**
     * Vypočítá MODI algoritmu
     *
     * @return true -> pokud úspěšný jinak false
     */
    public boolean calculateMODI() {
        UV uv = new UV(this);

        while (true) {
            initUV(); // prvně resetujeme u a v
            getAllocations(); // rozděl graf na allocated a unallocated části
            if (uv.performUVs()) {
                return false;
            }
            int[] netIncrease = calculateNetIncrease(); // najdi Net Increasu
            if (!existNegNetIncrease(netIncrease)) {
                // hotovo a odstan nekonecna
                for (int y = 0; y < arcMatrix.length; y++) {
                    for (int x = 0; x < arcMatrix[0].length; x++) {
                        if (arcMatrix[y][x] == Double.POSITIVE_INFINITY) {
                            arcMatrix[y][x] = 0;
                        }
                    }
                }
                return true;
            }
            int[] coordOfMin = findLargestNegativeIncrease(netIncrease); // najdi souřadnice nejmenšího Net Increasu
            ArrayList<double[]> cycle = (ArrayList<double[]>) findCycleValues(coordOfMin[0], coordOfMin[1]); // najdi cyklus a jeho hodnoty
            if (cycle == null) {
                // nelze najit řešení ? Může tato situace vůbec nastat ?
                // každopádně algoritmus selhal
                return false;
            }
            // vše hotovo, vyheneruj nové řešení
            generateNewSolution(cycle);

            /*
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 4; x++) {
                    System.out.print(arcMatrix[y][x] + " ");
                }
                System.out.println();
            }
             */
        }
    }
}