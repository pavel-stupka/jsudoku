package cz.muni.fi.xstupka.jsudoku;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Trida SudokuGenerator pro generovani sudoku
 * @author Pavel Stupka
 */
public class SudokuGenerator {
    
    /**
     * Vygeneruje nove jednoznacne sudoku. V kazdem ctverci budou minimalne
     * 3 cisla.
     * @param add kolik cisel se ma jeste nahodne pridat 0 az 54 
     * (v kazdem ctverci 3 cisla => 3 * 9 = 27, tedy 81 - 27 = 54)
     */
    public static Sudoku generateSudoku(int add) {
        Sudoku sudoku = null;
        
        try {
            Set<Sudoku> set;
            do {
                // zkousime vytvaret sudoku a testujeme, zda-li ma prave
                // jedno reseni, takove sudoku vzdy existuje, ale
                // nebezpeci je, ze se k nemu nemusime dostat v konecnem case :)
                sudoku = null;
                sudoku = new Sudoku();
                addNumbersToQuads(sudoku);
                addNumbersToQuads(sudoku);
                addNumbersToQuads(sudoku);

                SudokuTree tree = new SudokuTree(new Sudoku(sudoku));
                set = tree.solveSudoku();
            } while (set.size() != 1);
            
            // pridame jeste add nahodnych cisel
            if (add >= 0 && add <= 54) {
                SudokuTree tree = new SudokuTree(new Sudoku(sudoku));
                set = tree.solveSudoku();
                Sudoku solved = (Sudoku) set.toArray()[0];
                for (int p = 0; p < add; p++) {
                    addNumber(sudoku, solved);
                }
            }
            
        } catch (SudokuException ex) {
            // i tohle se muze stat, malo kdy, doufam... :)
            // System.err.println("Sudoku nebylo nalezeno, prosim zkuste to znovu...");
            return generateSudoku(add);
            // System.exit(666);
        }

        return sudoku;
    }
    
    /**
     * Nahodne prida cislo do sudoku
     */
    private static void addNumber(Sudoku sudoku, Sudoku solved) throws SudokuException {
        Set<int[]> set = new HashSet<int[]>();

        // mnozina dvojic int[2] urcujici pozice prazdnych poli
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (sudoku.getData(x, y) == Sudoku.EMPTY) {
                    int[] foo = new int[] {x, y};
                    set.add(foo);
                }
            }
        }
        
        if (set.isEmpty()) {
            throw new SudokuException("V sudoku jiz nejsou volna pole!");
        }
        
        // nahodne vybereme dvojici z mnoziny
        Random rnd = new Random();
        int pos = rnd.nextInt(set.size());
        int bar[] = (int[]) set.toArray()[pos];
        
        sudoku.setData(bar[0], bar[1], solved.getData(bar[0], bar[1]));
    }
    
    /*
     * Prida cislo do kazdeho ctverce
     */
    private static void addNumbersToQuads(Sudoku sudoku) throws SudokuException {
        // projde vsechny ctverce a do kazdeho prida cislo
        for (int b = 0; b < 9; b += 3) {
            for(int a = 0; a < 9; a += 3) {
                // hledame takove pole, ktere bude volne
                //--------------------------------------------------------------
                // vytvorime mnozinu volnych poli pro dany ctverec
                Set<int[]> set = new HashSet<int[]>();

                // mnozina dvojic int[2] urcujici pozice volnych poli ve ctverci
                for (int v = b; v < b+3; v++) {
                    for (int u = a; u < a+3; u++) {
                        if (sudoku.getData(u, v) == Sudoku.EMPTY) {
                            int[] foo = new int[] {u, v};
                            set.add(foo);
                        }
                    }
                }
        
                if (set.isEmpty()) {
                    throw new SudokuException("Do ctverce jiz nelze pridat cislo");
                }                
                //--------------------------------------------------------------
                Random rnd = new Random();
                int pos = rnd.nextInt(set.size());
                int bar[] = (int[]) set.toArray()[pos];
                int x = bar[0];
                int y = bar[1];
                // nyni mame na [x, y] pozici pole, ktere je volne

                // najdeme cisla, ktera lze na dane pole doplnit                
                boolean[] nums = sudoku.getPossibleNumbers(x, y);
                
                // spocitame pocet moznych cisel
                int count = 0;
                for (int p = 0; p < 9; p++) {
                    if (nums[p]) count++;
                }
                
                // pokud nelze doplnit zadne cislo, koncime
                if (count == 0) {
                    throw new SudokuException("Sudoku nelze vytvorit!");
                }
                
                // vytvorime pole moznych cisel o velikosti count
                int[] foo = new int[count];
                int pom = 0;
                for (int p = 0; p < 9; p++) {
                    if (nums[p]) {
                        foo[pom] = p + 1;
                        pom++;
                    }
                }
                
                // na pozici [x, y] dosadime nahodne cislo z pole foo
                sudoku.setData(x, y, foo[rnd.nextInt(count)]);
            }
        }        
    }
}
