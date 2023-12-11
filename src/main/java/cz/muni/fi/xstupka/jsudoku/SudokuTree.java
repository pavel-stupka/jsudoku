package cz.muni.fi.xstupka.jsudoku;

import java.util.HashSet;
import java.util.Set;

/**
 * Trida SudokuTree
 * @author paja
 */
public class SudokuTree {
    
    private Node root;
    
    /** 
     * Vytvori novou instanci SudokuTree 
     * @param root koren stromu
     */
    public SudokuTree(Node root) {
        this.root = root;
    }
    
    /** 
     * Vytvori novou instanci SudokuTree 
     * @param sudoku ktere se ma resit
     */
    public SudokuTree(Sudoku sudoku) {
        root = new Node(sudoku);
    }
        
    /**
     * Resi sudoku
     */
    public Set<Sudoku> solveSudoku() {
        // vyresi sudoku
        root.solveSudoku();
        
        // prohledavanim do hloubky najdeme hotova reseni
        Set<Sudoku> set = new HashSet<Sudoku>();
        root.addSolvedSudoku(set);
        
        return set;
    }
}
