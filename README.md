# 🎮 Connect Four Game 

A **Java** implementation of the classic Connect Four game with both **single-player** and **two-player** modes.

## 🧩 Game Description

**Connect Four** is a two-player connection game in which the players take turns dropping discs into a vertically suspended grid. 
The pieces fall straight down, occupying the lowest available space within the column. 
The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs.

## ✨ Features

✅ Standard Board – 6 rows × 7 columns

🎨 Colorful Terminal Interface – Enhanced with ANSI color codes

🧠 Smart AI Bot – Powered by Minimax + Alpha-Beta Pruning
👫 Two Game Modes:

🧍‍♂️ Single-player vs AI | 👬 Two-player mode

🔁 Replay Option – Play again after each game

⚠️ Fallback AI logic for systems with limited compatibility

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
   - Select game mode (1 for single-player 🤖, 2 for two-player 🧍‍♂️🧍‍♂️)
   - Take turns selecting columns (1-7) to drop your pieces
   - First player to connect four pieces in a row (horizontally, vertically, or diagonally) wins. 🏆

## 🎮 Game Controls

🔢 Enter a number 1-7 to select a column
🚫 The game will reject invalid moves (full columns or out-of-bounds selections)
🔄 After a game ends, you can choose to play again

## Color Guide

🔴 Red (X) → Player 1

🟡 Yellow (O) → Player 2 / Bot

🟦 Blue → Info messages

🌀 Cyan → Borders & UI elements

## 🤖 AI Bot Strategy
The bot plays smart using:

🧠 Minimax Algorithm with Alpha-Beta Pruning

⚡ Search depth of up to 5 moves ahead

🛡️ Blocks your winning moves

🚀 Seizes winning opportunities immediately

🧩 Plans multiple steps ahead to corner you


## 🚀 Ready to Connect Four?

Play smart. Plan ahead. And don’t let the bot outsmart you! 😉
Have fun and feel free to improve or contribute!