import java.util.Scanner;

public class ConnectFour {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = ' ';
    private static final char PLAYER1 = 'X';
    private static final char PLAYER2 = 'O';
    private static final char BOT = 'O';
    
    // ANSI colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";
    
    private char[][] board;
    private boolean gameOver;
    private char currentPlayer;
    private boolean vsBot;
    
    public ConnectFour() {
        board = new char[ROWS][COLS];
        initializeBoard();
        gameOver = false;
        currentPlayer = PLAYER1;
    }
    
    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println(CYAN + "+-------------------------------+" + RESET);
        System.out.println(CYAN + "|" + RED + "     C O N N E C T  F O U R    " + CYAN + "|" + RESET);
        System.out.println(CYAN + "+-------------------------------+" + RESET);
        System.out.println();
        
        System.out.println(CYAN + "Select Game Mode:" + RESET);
        System.out.println(RED + "1. " + RESET + "One Player (vs Bot)");
        System.out.println(YELLOW + "2. " + RESET + "Two Players");
        System.out.print(CYAN + "Enter your choice (1-2): " + RESET);
        
        int mode = 0;
        while (mode != 1 && mode != 2) {
            try {
                mode = Integer.parseInt(scanner.nextLine());
                if (mode != 1 && mode != 2) {
                    System.out.print(BLUE + "Invalid input. Try again (1-2): " + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.print(BLUE + "Invalid input. Try again (1-2): " + RESET);
            }
        }
        
        vsBot = (mode == 1);
        
        while (!gameOver) {
            printBoard();
            
            if (currentPlayer == PLAYER1 || !vsBot) {
                playerMove(scanner);
            } else {
                botMove();
            }
            
            if (checkWin(currentPlayer)) {
                printBoard();
                if (currentPlayer == BOT && vsBot) {
                    System.out.println(YELLOW + "Bot wins!" + RESET);
                } else if (currentPlayer == PLAYER1) {
                    System.out.println(RED + "Player " + currentPlayer + " wins!" + RESET);
                } else {
                    System.out.println(YELLOW + "Player " + currentPlayer + " wins!" + RESET);
                }
                gameOver = true;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println(BLUE + "It's a draw!" + RESET);
                gameOver = true;
            } else {
                switchPlayer();
            }
        }
        
        System.out.print(CYAN + "Play again? (y/n): " + RESET);
        String playAgain = scanner.nextLine().trim().toLowerCase();
        if (playAgain.equals("y")) {
            initializeBoard();
            gameOver = false;
            currentPlayer = PLAYER1;
            startGame();
        }
    }
    
    private void printBoard() {
        System.out.println();
        
        // Print column numbers with cyan color
        System.out.print(CYAN + "  ");
        for (int j = 0; j < COLS; j++) {
            System.out.print((j + 1) + " ");
        }
        System.out.println(RESET);
        
        // Print the board with colors
        for (int i = 0; i < ROWS; i++) {
            System.out.print(CYAN + "| " + RESET);
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == PLAYER1) {
                    System.out.print(RED + board[i][j] + " " + RESET);
                } else if (board[i][j] == PLAYER2 || board[i][j] == BOT) {
                    System.out.print(YELLOW + board[i][j] + " " + RESET);
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println(CYAN + "|" + RESET);
        }
        
        // Print the bottom line
        System.out.print(CYAN + "+ ");
        for (int j = 0; j < COLS; j++) {
            System.out.print("- ");
        }
        System.out.println("+" + RESET);
        System.out.println();
    }
    
    private void playerMove(Scanner scanner) {
        int col = -1;
        boolean validMove = false;
        
        while (!validMove) {
            if (currentPlayer == PLAYER1) {
                System.out.print(RED + "Player " + currentPlayer + RESET + ", choose a column (1-" + COLS + "): ");
            } else {
                System.out.print(YELLOW + "Player " + currentPlayer + RESET + ", choose a column (1-" + COLS + "): ");
            }
            
            try {
                col = Integer.parseInt(scanner.nextLine()) - 1; // Convert to 0-based indexing
                
                if (col >= 0 && col < COLS) {
                    if (isValidMove(col)) {
                        validMove = true;
                    } else {
                        System.out.println(BLUE + "Column is full. Try again." + RESET);
                    }
                } else {
                    System.out.println(BLUE + "Invalid column. Try again." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(BLUE + "Invalid input. Try again." + RESET);
            }
        }
        
        makeMove(col, currentPlayer);
    }
    
    private void botMove() {
        System.out.println(BLUE + "Bot is thinking..." + RESET);
        
        // Use fallback logic directly without trying BotLogic.findBestMove
        int col = findSimpleMove(board, BOT);
        
        makeMove(col, BOT);
        System.out.println(YELLOW + "Bot placed in column " + (col + 1) + RESET);
    }
    
    // Simple fallback bot logic
    private int findSimpleMove(char[][] board, char botSymbol) {
        char playerSymbol = (botSymbol == PLAYER1) ? PLAYER2 : PLAYER1;
        
        // Check for winning move
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                // Find row where piece would fall
                int row = -1;
                for (int r = ROWS - 1; r >= 0; r--) {
                    if (board[r][col] == EMPTY) {
                        row = r;
                        break;
                    }
                }
                
                if (row != -1) {
                    // Try this move
                    board[row][col] = botSymbol;
                    
                    // If this move wins, choose it
                    if (checkWin(botSymbol)) {
                        board[row][col] = EMPTY; // Undo the move
                        return col;
                    }
                    
                    // Undo the move
                    board[row][col] = EMPTY;
                }
            }
        }
        
        // Check for blocking move
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                // Find row where piece would fall
                int row = -1;
                for (int r = ROWS - 1; r >= 0; r--) {
                    if (board[r][col] == EMPTY) {
                        row = r;
                        break;
                    }
                }
                
                if (row != -1) {
                    // Try opponent's move
                    board[row][col] = playerSymbol;
                    
                    // If this move would let opponent win, block it
                    if (checkWin(playerSymbol)) {
                        board[row][col] = EMPTY; // Undo the move
                        return col;
                    }
                    
                    // Undo the move
                    board[row][col] = EMPTY;
                }
            }
        }
        
        // If no winning or blocking move, prefer center columns
        int[] centerPreference = {COLS/2, COLS/2+1, COLS/2-1, COLS/2+2, COLS/2-2, COLS/2+3, COLS/2-3, COLS/2+4, COLS/2-4};
        for (int col : centerPreference) {
            if (col >= 0 && col < COLS && isValidMove(col)) {
                return col;
            }
        }
        
        // Last resort: first available column
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(col)) {
                return col;
            }
        }
        
        // Board is full (shouldn't reach here)
        return -1;
    }
    
    public boolean isValidMove(int col) {
        return board[0][col] == EMPTY;
    }
    
    public void makeMove(int col, char player) {
        // Find the lowest empty row in the selected column
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                board[row][col] = player;
                break;
            }
        }
    }
    
    private boolean checkWin(char player) {
        // Check horizontal
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player && 
                    board[row][col+1] == player && 
                    board[row][col+2] == player && 
                    board[row][col+3] == player) {
                    return true;
                }
            }
        }
        
        // Check vertical
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == player && 
                    board[row+1][col] == player && 
                    board[row+2][col] == player && 
                    board[row+3][col] == player) {
                    return true;
                }
            }
        }
        
        // Check diagonal (down-right)
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player && 
                    board[row+1][col+1] == player && 
                    board[row+2][col+2] == player && 
                    board[row+3][col+3] == player) {
                    return true;
                }
            }
        }
        
        // Check diagonal (up-right)
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (board[row][col] == player && 
                    board[row-1][col+1] == player && 
                    board[row-2][col+2] == player && 
                    board[row-3][col+3] == player) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER1) ? PLAYER2 : PLAYER1;
    }
    
    public static void main(String[] args) {
        ConnectFour game = new ConnectFour();
        game.startGame();
    }
} 