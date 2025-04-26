public class BotLogic {
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = ' ';
    private static final char BOT = 'O';
    private static final char PLAYER = 'X';
    private static final int MAX_DEPTH = 6;
    
    // Center columns are generally more valuable in Connect Four
    private final int[] columnScores = {3, 4, 5, 7, 5, 4, 3};
    
    public int[] findBestMove(char[][] board, char botSymbol) {
        // If board is empty, pick the middle column
        if (isBoardEmpty(board)) {
            return new int[]{COLS / 2};
        }
        
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;
        
        for (int col = 0; col < COLS; col++) {
            if (isValidMove(board, col)) {
                // Try this move
                int row = getNextEmptyRow(board, col);
                board[row][col] = botSymbol;
                
                // If this move wins, choose it immediately
                if (checkWin(board, botSymbol)) {
                    board[row][col] = EMPTY; // Undo the move
                    return new int[]{col};
                }
                
                // Check if player can win in one move and block it
                board[row][col] = EMPTY; // Undo bot move
                board[row][col] = PLAYER; // Try player move
                if (checkWin(board, PLAYER)) {
                    board[row][col] = EMPTY; // Undo the move
                    return new int[]{col};
                }
                
                // Restore and evaluate using minimax
                board[row][col] = EMPTY;
                board[row][col] = botSymbol;
                
                int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[row][col] = EMPTY; // Undo the move
                
                // Add bias for center columns
                score += columnScores[col];
                
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }
        
        // If no good moves found, pick the first valid one
        if (bestCol == -1) {
            for (int col = 0; col < COLS; col++) {
                if (isValidMove(board, col)) {
                    bestCol = col;
                    break;
                }
            }
        }
        
        return new int[]{bestCol};
    }
    
    private int minimax(char[][] board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        // Base cases
        if (checkWin(board, BOT)) return 100 - depth;
        if (checkWin(board, PLAYER)) return depth - 100;
        if (isBoardFull(board) || depth >= MAX_DEPTH) return evaluateBoard(board);
        
        if (isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            
            for (int col = 0; col < COLS; col++) {
                if (isValidMove(board, col)) {
                    int row = getNextEmptyRow(board, col);
                    board[row][col] = BOT;
                    
                    int eval = minimax(board, depth + 1, false, alpha, beta);
                    board[row][col] = EMPTY; // Undo move
                    
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    
                    if (beta <= alpha) {
                        break; // Beta cutoff
                    }
                }
            }
            
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            
            for (int col = 0; col < COLS; col++) {
                if (isValidMove(board, col)) {
                    int row = getNextEmptyRow(board, col);
                    board[row][col] = PLAYER;
                    
                    int eval = minimax(board, depth + 1, true, alpha, beta);
                    board[row][col] = EMPTY; // Undo move
                    
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    
                    if (beta <= alpha) {
                        break; // Alpha cutoff
                    }
                }
            }
            
            return minEval;
        }
    }
    
    private int evaluateBoard(char[][] board) {
        int score = 0;
        
        // Evaluate horizontal windows
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board, row, col, 0, 1);
            }
        }
        
        // Evaluate vertical windows
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS; col++) {
                score += evaluateWindow(board, row, col, 1, 0);
            }
        }
        
        // Evaluate diagonal (down-right) windows
        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board, row, col, 1, 1);
            }
        }
        
        // Evaluate diagonal (up-right) windows
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLS - 3; col++) {
                score += evaluateWindow(board, row, col, -1, 1);
            }
        }
        
        // Favor center column
        for (int row = 0; row < ROWS; row++) {
            if (board[row][COLS/2] == BOT) {
                score += 3;
            }
        }
        
        return score;
    }
    
    private int evaluateWindow(char[][] board, int startRow, int startCol, int deltaRow, int deltaCol) {
        char[] window = new char[4];
        
        // Get the window contents
        for (int i = 0; i < 4; i++) {
            window[i] = board[startRow + i * deltaRow][startCol + i * deltaCol];
        }
        
        int botCount = 0;
        int playerCount = 0;
        int emptyCount = 0;
        
        for (char piece : window) {
            if (piece == BOT) botCount++;
            else if (piece == PLAYER) playerCount++;
            else emptyCount++;
        }
        
        // Score the window
        if (botCount == 4) return 100;
        else if (botCount == 3 && emptyCount == 1) return 5;
        else if (botCount == 2 && emptyCount == 2) return 2;
        else if (playerCount == 3 && emptyCount == 1) return -5;
        
        return 0;
    }
    
    public boolean isValidMove(char[][] board, int col) {
        return col >= 0 && col < COLS && board[0][col] == EMPTY;
    }
    
    public int getNextEmptyRow(char[][] board, int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) {
                return row;
            }
        }
        return -1; // Column is full
    }
    
    private boolean isBoardEmpty(char[][] board) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean isBoardFull(char[][] board) {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == EMPTY) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkWin(char[][] board, char player) {
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
    
    // Add a simplified method for ConnectFour to use
    public int getColumnMove(char[][] board, char botSymbol) {
        int[] moveArray = findBestMove(board, botSymbol);
        return moveArray[0];
    }
} 