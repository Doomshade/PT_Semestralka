package git.doomshade.semstralka.martin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * --MODIFIED DISTRIBUTION METHOD--
 * Třída reprezentuje MODI algoritmus tj. dokáže zjistit zda zadané realizovatelné řešení je optimální.
 * Pokud by zadané realizovatelné řešení nebylo optimální, nalezne optimální řešení a to vrátí.
 *
 * Je počítáno s tím že všechna vstupní data JSOU ve správném tvaru a data tedy nejsou nijak ověřována, až na možnost, kdy
 * by průběhem algoritmu nebo již při zadání došlo k degenerativnímu realizovatelnému řešení
 *
 * @author Martin Jakubašek
 */
public class MODI {

    /**
     * Matice hran, tedy reprezentuje kudy se přepravuje zboží a kolik se ho přepravuje
     *        20 20 0 0 -> 40
     * např.  0 10 50 0 -> 60
     *        0 0 0 50 -> 50
     *        - - - -
     *        20 30 50 50
     */
    final double[][] arcMatrix;
    /**
     * Matic cen převozu mezi S a D
     *          4 6 8 8
     * např.    6 8 6 7
     *          5 7 6 8
     */
    final int[][] costMatrix;

    private final double[] u;
    private final double[] v;

    /**
     * Vytvoří novou instanci MODI algoritmu, ted instanci sloužící pro právě jeden příklad
     * Je potřeba zadat správná data, pro správnou funkčnost algoritmu
     *
     * @param arcMatrix matice hran, reprezentující 1 realizovatelné řešení (tj. správné řešení, ale ne optimální)
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
    private void initUV() {
        Arrays.fill(u, Double.POSITIVE_INFINITY);
        Arrays.fill(v, Double.POSITIVE_INFINITY);
        u[0] = 0;
    }

    //rozděl matici na 2 části -> alokovanou (hrany jsou používány pro přepravu) a nealokovanou část
    private ArrayList<int[]> allocated;
    private ArrayList<int[]> unallocated;

    /**
     * Přířadí hrany grafu buďto do přiřazené nebo nepřiřazené části
     */
    private void getAllocations() {
        allocated = new ArrayList<>();
        unallocated = new ArrayList<>();

        for (int y = 0; y < arcMatrix.length; y++) {
            for (int x = 0; x < arcMatrix[0].length; x++) {
                if (arcMatrix[y][x] > 0)
                    allocated.add(new int[] {x,y});
                else
                    unallocated.add(new int[] {x,y});
            }
        }
    }

    /**
     * Vypočítá, pokud je to možné u a v atributy algoritmu
     *
     * @return true pokud přiřazeno, jinak false
     */
    private boolean calculateUVs() {
        boolean impossible = false;
        while(!allUVsSet()) {
            if (impossible)
                return false;
            for (int[] arc : allocated) {
                int xCoord = arc[0];
                int yCoord = arc[1];
                // zjisti zda známe alespoň jednu proměnnou
                if (u[yCoord] == Double.POSITIVE_INFINITY && v[xCoord] == Double.POSITIVE_INFINITY)
                    impossible = true;
                if (u[yCoord] != Double.POSITIVE_INFINITY && v[xCoord] == Double.POSITIVE_INFINITY) {
                    // známe u[y]
                    v[xCoord] = costMatrix[yCoord][xCoord] - u[yCoord];
                    impossible = false;
                    continue;
                }
                if (v[xCoord] != Double.POSITIVE_INFINITY && u[yCoord] == Double.POSITIVE_INFINITY) {
                    u[yCoord] = costMatrix[yCoord][xCoord] - v[xCoord];
                    impossible = false;
                    continue;
                }
            }
        }
        return true;
    }

    /**
     * Metoda zjistí zda byly přiřazeny všechny prvky v 'u' a 'v' poli
     *
     * @return true, pokud všechny prvky jsou přiřazeny
     */
    private boolean allUVsSet() {
        for (double num : u) {
            if (num == Double.POSITIVE_INFINITY)
                return false;
        }
        for (double num : v) {
            if (num == Double.POSITIVE_INFINITY)
                return false;
        }

         return true;
    }

    /**
     * Vypočítá vylepšující ceny použítí nepoužitých hran
     *
     * @return  vylepšující ceny nepoužitých hran
     */
    private int[] calculateNetIncrease() {
        int[] netIncrease = new int[unallocated.size()];

        for (int i = 0; i < unallocated.size(); i++) {
            int x = unallocated.get(i)[0];
            int y = unallocated.get(i)[1];

            netIncrease[i] = (int) (costMatrix[y][x]  - (u[y] + v[x]));
        }

        return netIncrease;
    }

    /**
     * Zknotroluj zda existuje negativní net increase
     *
     * @param netIncrease net increase
     *
     * @return true pokud existuje jinak false
     */
    private boolean existNegNetIncrease(int[] netIncrease) {
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
    private int[] findLargestNegativeIncrease(int[] netIncrease) {
        int minNegative = 0; // index
        for (int i = 0; i < netIncrease.length; i++) {
            if (netIncrease[i] <= 0 && netIncrease[i] < netIncrease[minNegative])
                minNegative = i;
        }

        return unallocated.get(minNegative);
    }

    /**
     * Metoda najde cyklus (pokud existuje) ze zadaných souřadnic.
     * Následně najde a vrátí hodnoty všech hran cyklu.
     *
     * @param x souřadnice x;
     * @param y souřadnice y;
     *
     * @return list [pozice x, pozice y, přeprava]
     */
    private List<double[]> findCycleValues(int x, int y) {
        double value = arcMatrix[y][x];
        arcMatrix[y][x] = 1; // nic tam nebude, takže si tam bez problému přidáme 0

        CycleDetection cd = new CycleDetection(arcMatrix);
        ArrayList<int[]> cycle = (ArrayList<int[]>) cd.detectCycle(x,y);
        if (cycle == null)
            return null;
        // než něco začneme dělat vrátíme "arcMatrix" doi původního stavu
        arcMatrix[y][x] = value;
        // teď se můžeme zabývat cyklem
        ArrayList<double[]> cycleValues = new ArrayList<>();
        for (int i = 0; i < cycle.size(); i++) {
            int xPos = cycle.get(i)[0];
            int yPos = cycle.get(i)[1];

            int neg = (i % 2 == 0) ? 1 : -1;
            cycleValues.add(new double[] {xPos, yPos, arcMatrix[yPos][xPos] * neg});
        }

        return cycleValues;
    }

    /**
     * Vygeneruj nové řešení úlohy
     *
     * @param cycleValues hodnoty cyklu a pozice cyklu
     */
    private void generateNewSolution(List<double[]> cycleValues) {
        // flags
        boolean negativeEpsilon = false;

        // najdi mi theta číslo (maximální číslo co mohu odečít ze všech záporných prvků cyklu, tak abych nepřepravoval záporné množství prvků)
        double theta = cycleValues.get(1)[2]; // vezmeme lichá čísla (jsou VŽDY záporná)
        for (double[] val : cycleValues) {
            if (val[2] == Double.NEGATIVE_INFINITY) {
                negativeEpsilon = true;
                break;
            }

            if (val[2] < 0 && val[2] > theta)
                theta = val[2];
        }
        if (!negativeEpsilon) {
            // karanténa --
            theta *= -1; // theta musí být pozitivní :(
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
                arcMatrix[y][x] += theta * mul;
            }
        }
        else {
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

    private boolean calculateEpsilons() {
        ArrayList<Integer> unalocatedUs = new ArrayList<>();
        // najdi nepřiřazená u -> tedy u == Inf
        for (int i = 0; i < u.length; i++) {
            if (u[i] == Double.POSITIVE_INFINITY)
                unalocatedUs.add(i);
        }

        // projdi všechny řádky s nepřiřazenými u a přiřaď epsilon tak aby nevznikal cyklus
        ArrayList<int[]> bannedPos = new ArrayList<>();
        CycleDetection cd = new CycleDetection(arcMatrix);
        int epsilons = 0;
        while (true) {
            // zkus každému u přiřadit epsilon, tak aby nevznikal cyklus
            for (int index : unalocatedUs) {
                int banCount = 0;
                for (int x = 0; x < arcMatrix[0].length; x++) {
                    boolean banned = false;
                    // jedná se o ilegální pozici ?
                    for (int[] ban : bannedPos) {
                        if (x == ban[0] && index == ban[1]) {
                            banned = true;
                            ++banCount;
                        }
                    }
                    if (banCount == arcMatrix[0].length)
                        return false;
                    if (banned)
                        continue;

                    //pokud není již na pozici jiné číslo, zkus tam dát epsilon
                    if (arcMatrix[index][x] != 0)
                        continue;
                    //zkus přiřadit epsilon
                    double value = arcMatrix[index][x];
                    arcMatrix[index][x] = Double.POSITIVE_INFINITY;
                    if (cd.detectCycle(x, index) != null) {
                        // detekoval jsem cyklus
                        bannedPos.add(new int[] {x, index});
                        arcMatrix[index][x] = value;
                        continue;
                    }
                    //uspěšně si přiřadil epsilon
                    ++epsilons;
                    break;
                }
            }

            if (epsilons == unalocatedUs.size())
                return true;

            // nenašel jsi všechna epsilon
            // resetuj epsilon
            epsilons = 0;
            for (int index : unalocatedUs) {
                for (int x = 0; x < arcMatrix[0].length; x++) {
                    if (arcMatrix[index][x] == Double.POSITIVE_INFINITY)
                        arcMatrix[index][x] = 0;
                }
            }
        }
    }

    public boolean calculateMODI() {
        while(true) {
            initUV(); // prvně resetujeme u a v
            getAllocations(); // rozděl graf na allocated a unallocated části
            if (!calculateUVs()) {
                // degenerativní řešení
                if (!calculateEpsilons())
                    return false; //NEMOHL JSEM NENAJÍT ŘEŠENÍ BEZ CYKLU -> nemohu provést algoritmus
                getAllocations(); // restartuj alokace
                if (!calculateUVs())
                    return false; // FATAL ERROR -> tohle snad ani nemůže nastat

                /*
                for (int y = 0; y < 3; y++) {
                    for (int x = 0; x < 4; x++) {
                        if (arcMatrix[y][x] == Double.POSITIVE_INFINITY) {
                            System.out.print("E ");
                            continue;
                        }
                        System.out.print(arcMatrix[y][x] + " ");
                    }
                    System.out.println();
                }
                 */
            }
            int[] netIncrease = calculateNetIncrease(); // najdi Net Increasu
            if (!existNegNetIncrease(netIncrease))
                return true;
            int[] coordOfMin = findLargestNegativeIncrease(netIncrease); // najdi souřadnice nejmenšího Net Increasu
            ArrayList<double[]> cycle = (ArrayList<double[]>) findCycleValues(coordOfMin[0], coordOfMin[1]); // najdi cyklus a jeho hodnoty
            if (cycle == null) {
                // nelze najit řešení ? Může tato situace vůbec nastat ?
                // každopádně algoritmus selhal
                return false;
            }
            // vše hotovo, vyheneruj nové řešení
            generateNewSolution(cycle);

            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 4; x++) {
                    System.out.print(arcMatrix[y][x] + " ");
                }
                System.out.println();
            }
        }
    }
}
