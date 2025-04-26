# Connect Four Game

A Java implementation of the classic Connect Four game with both single-player and two-player modes.

## Game Description

Connect Four is a two-player connection game in which the players take turns dropping discs into a vertically suspended grid. The pieces fall straight down, occupying the lowest available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs.

## Features

- 7x9 game board (larger than standard Connect Four)
- Colorful text-based interface with ANSI colors
- Two game modes:
  - Single-player vs AI bot
  - Two-player mode
- AI opponent uses minimax algorithm with alpha-beta pruning
- Fallback AI logic in case of compatibility issues

## How to Play

1. Compile the Java files:

   ```
   javac ConnectFour.java BotLogic.java Main.java
   ```

2. Run the game:

   ```
   java Main
   ```

3. Follow the on-screen instructions:
   - Select game mode (1 for single-player, 2 for two-player)
   - Take turns selecting columns (1-9) to drop your pieces
   - First player to connect four pieces in a row (horizontally, vertically, or diagonally) wins

## Game Controls

- Enter a number 1-9 to select a column
- The game will reject invalid moves (full columns or out-of-bounds selections)
- After a game ends, you can choose to play again

## Color Guide

- Red (X): Player 1
- Yellow (O): Player 2 / Bot
- Cyan: Game borders and UI elements
- Blue: Informational messages

## AI Bot

The AI opponent uses the minimax algorithm with alpha-beta pruning to determine the best move. The bot will:

- Win immediately if it can
- Block your winning moves
- Plan ahead to create winning opportunities

The bot has a maximum search depth of 5 moves to maintain reasonable performance with the larger board size.
