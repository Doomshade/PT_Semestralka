package git.doomshade.semstralka.doomshade;

import java.util.*;

public class MatrixUtil {

    private final int[][] matrix;
    private final int xLen, yLen;
    List<int[]> fu;

    public MatrixUtil(int[][] matrix) {
        this.matrix = matrix;
        xLen = matrix[0].length;
        yLen = matrix.length;
    }

    private class Cycle {
        private static final short invalidNum = -1;
        private final HashMap<Integer, Integer> indexMap = new HashMap<>();
        private final short[][] previousNumbers = new short[yLen][xLen];

        private final Collection<Integer> testedIndexes = new ArrayList<>();

        int[] tempPole = new int[1000];

        {
            tempPole[0] = 0;

            for (int i = 0; i < previousNumbers.length; i++) {
                Arrays.fill(previousNumbers[i], invalidNum);
            }
        }

        private int x = -1, y = 0;

        private void findCycle() throws StackOverflowError {
            findCycle(0, (byte) 1, (byte) 0);
        }

        private void findCycle(int previousIndex, byte moveX, byte moveY) throws StackOverflowError {
            x += moveX;
            y += moveY;

            if (x == xLen) {
                x--;
                return;
            }

            if (y == yLen) {
                y--;
                return;
            }

            if (x < 0) {
                x = 0;
                return;
            }

            if (y < 0) {
                y = 0;
                return;
            }

            final int currentIndex = y * xLen + x;
            final int num = matrix[y][x];
            if (num == 0) {
                findCycle(previousIndex, moveX, moveY);
            } else {

                final int idxComingFrom = indexMap.getOrDefault(previousIndex, -1);
                if (currentIndex == idxComingFrom) return;
                if (testedIndexes.contains(currentIndex)) {
                    System.out.println("CYCLUS");
                    throw new StackOverflowError();
                }
                testedIndexes.add(currentIndex);
                indexMap.put(idxComingFrom, previousIndex);
                findCycle(currentIndex, (byte) 1, (byte) 0);
                findCycle(currentIndex, (byte) 0, (byte) 1);
                findCycle(currentIndex, (byte) -1, (byte) 0);
                findCycle(currentIndex, (byte) 0, (byte) -1);
            }
        }

        private Collection<Integer> getCycle(int y, int x, int prevY, int prevX) {

            if (prevX >= 0 && prevY >= 0) {
                final int idx = (prevY * xLen) + prevX;
                previousNumbers[y][x] = (short) idx;
            }

            /*Collection<Integer> currCollection = indexMap.getOrDefault(idx, new ArrayList<>());
            if (current == invalidNum && (current == matrix[0].length - 1)) {
                return currCollection;
            }

            currCollection.add(current);
            indexMap.put(current, currCollection);*/

            /*
            for (int currX = x + 1; currX < xLen && currX != prevX; currX++) {
                final byte check = check(y, currX);
                if (check == 1) {
                    return getCycle(y, currX, prevY, x);
                } else if (check == 2) {
                    System.out.println("A");
                    return new ArrayList<>();
                }
            }

            for (int currX = x - 1; currX > 0 && currX != prevX; currX--) {
                final byte check = check(y, currX);
                if (check == 1) {
                    return getCycle(y, currX, prevY, x);
                } else if (check == 2) {
                    System.out.println("B");
                    return new ArrayList<>();
                }
            }

            for (int currY = y + 1; currY < yLen && currY != prevY; currY++) {
                final byte check = check(currY, x);
                if (check == 1) {
                    return getCycle(currY, x, y, prevX);
                } else if (check == 2) {
                    System.out.println("C");
                    return new ArrayList<>();
                }
            }

            for (int currY = y - 1; currY > 0 && currY != prevY; currY--) {
                final byte check = check(currY, x);
                if (check == 1) {
                    return getCycle(currY, x, y, prevX);
                } else if (check == 2) {
                    System.out.println("D");
                    return new ArrayList<>();
                }
            }*/

            /*
            while (++curr != prevX && curr < len) {
                return getCycle(x, curr, prevY, x);
            }

            curr = x;

            while (--curr != prevX && curr < len) {
                return getCycle(x, curr, prevY, x);
            }

            curr = x;

            while ((curr += len) != prevY && curr < matrix.length) {
                return getCycle(curr, x, x, prevX);
            }

            curr = x;

            while ((curr -= len) != prevY && curr < matrix.length) {
                return getCycle(curr, x, x, prevX);
            }*/

            /*while ((curr += len) < matrix.length) {
                return getCycle(current, curr);
            }

            while ((curr -= len) < matrix.length) {
                return getCycle(current, curr);
            }*/

            return new ArrayList<>();

/*
            // get next index first, if there's no index, we return empty collection
            final int next = nextIndex();
            if (next == -1) {
                return new HashSet<>();
            }
            // get indicing indexes
            final Collection<Integer> set = indexMap.getOrDefault(next, new ArrayList<>());
            if (indexMap.containsKey(next)) {
                return set;
            }

            tempPole[idx++] = next;
            set.add(next);
            indexMap.put(next, set);

            return getCycle(next);*/
        }

        private byte check(int y, int x) {
            byte check = 0;
            if (matrix[y][x] != 0) {
                check = 1;
            }

            final short prevNum = previousNumbers[y][x];
            if (prevNum != invalidNum && prevNum != (y * xLen + x)) {
                check = 2;
            }

            System.out.println("Checking " + y + " and " + x);

            System.out.println(check);
            return check;
        }

        /*
        private int nextIndex() {
            final int current = tempPole[idx];
            int previous = tempPole[idx - 1];
            int x = current;
            final int len = matrix[0].length;
            int y = (x + 1) / len;

            System.out.println("a");
            // doleva
            while (--x > 0) {
                if (check(y, x)) {
                    return x;
                }
            }
            x = previous;

            System.out.println("aa");
            // doprava
            while (++x < len) {
                if (x == previous) break;
                if (check(y, x)) {
                    return x;
                }
            }
            x = previous;

            System.out.println("aaa");
            // nahoru
            while ((x -= previous) > 0) {
                if (check(y, x)) {
                    return x;
                }
            }

            System.out.println("aaaa");
            x = previous;
            // dolu
            while ((x += previous) < matrix.length) {
                if (x == previous) break;
                if (check(y, x)) {
                    return x;
                }
            }

            // nic se nenaslo, vratime -1
            return -1;
        }*/
    }

    public Collection<Integer> getCycle() {
        final Cycle cycle = new Cycle();
        try {
            cycle.findCycle();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return cycle.testedIndexes;
    }
}
