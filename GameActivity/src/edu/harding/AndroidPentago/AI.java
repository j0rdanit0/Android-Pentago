package edu.harding.AndroidPentago;


import android.graphics.Point;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.awt.*;

/**
 * Created by trichar2 on 4/23/14.
 */
public class AI {
/*    ﻿using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using System.Windows.Media;
    using System.Windows;
    using Pentago.GameCore;
    using System.Diagnostics;
    using System.Collections;
    using System.Threading;
    using System.Threading.Tasks;*/
            //AI player
            private String _Name;
            private boolean _ActivePlayer;
            //private ImageBrush _Image;
            //private ImageBrush _ImageHover;
            public enum Difficulty { Beginner, Easy, Medium, Hard };
            public Difficulty _DifficultyLevel;
            private int _MaxTreeDepth;

            //board finals
            private final int BOARDSIZE = 36;
            private final int MAXMOVES = 36;
            private final int BOARWIDTH = 6;

            //Related to the move selected by AI
            private int _Choice;
            private boolean _IsClockWise;
            private short _Quad;
            private String _Active_Turn = "COMPUTER";
            private int[] _TempBoard = new int[BOARDSIZE];

            public AI(boolean isActive, Difficulty difficulty)
            {
                this._ActivePlayer = isActive;
                switch(difficulty)
                {
                    case Beginner:
                        this._MaxTreeDepth = 1;
                        this._DifficultyLevel = Difficulty.Beginner;
                        break;
                    case Easy:
                        this._MaxTreeDepth = 1;
                        this._DifficultyLevel = Difficulty.Easy;
                        break;
                    case Medium:
                        this._MaxTreeDepth = 2;
                        this._DifficultyLevel = Difficulty.Medium;
                        break;
                    case Hard:
                        this._MaxTreeDepth = 3;
                        this._DifficultyLevel = Difficulty.Hard;
                        break;
                    default:
                        this._MaxTreeDepth = 3;
                        this._DifficultyLevel = Difficulty.Hard;
                        break;
                }
            }


            public void setActivePlayer(boolean activePlayer)
            {
                this._ActivePlayer = activePlayer;
            }

            private int[] convertBoardTypes(int[] board)
            {
                int[] returnBoard = new int[BOARDSIZE];
                returnBoard[0] = board[0];
                returnBoard[1] = board[1];
                returnBoard[2] = board[2];
                returnBoard[6] = board[3];
                returnBoard[7] = board[4];
                returnBoard[8] = board[5];
                returnBoard[12] = board[6];
                returnBoard[13] = board[7];
                returnBoard[14] = board[8];

                returnBoard[3] = board[9];
                returnBoard[4] = board[10];
                returnBoard[5] = board[11];
                returnBoard[9] = board[12];
                returnBoard[10] = board[13];
                returnBoard[11] = board[14];
                returnBoard[15] = board[15];
                returnBoard[16] = board[16];
                returnBoard[17] = board[17];

                returnBoard[18] = board[18];
                returnBoard[19] = board[19];
                returnBoard[20] = board[20];
                returnBoard[24] = board[21];
                returnBoard[25] = board[22];
                returnBoard[26] = board[23];
                returnBoard[30] = board[24];
                returnBoard[31] = board[25];
                returnBoard[32] = board[26];

                returnBoard[21] = board[27];
                returnBoard[22] = board[28];
                returnBoard[23] = board[29];
                returnBoard[27] = board[30];
                returnBoard[28] = board[31];
                returnBoard[29] = board[32];
                returnBoard[33] = board[33];
                returnBoard[34] = board[34];
                returnBoard[35] = board[35];

                return returnBoard;
            }

            public boolean getActivePlayer()
            {
                return _ActivePlayer;
            }

            public void MakeAIMove(char[] board)
            {
                int enemyPieceCount = 0;
                int computerCount = 0;
                for (int i = 0; i < BOARDSIZE; i++)
                {
                    this._TempBoard[i] = board[i] - 48;
                    if (this._TempBoard[i] == 1)
                        enemyPieceCount++;
                    if (this._TempBoard[i] == 2)
                        computerCount++;
                }

                _TempBoard = convertBoardTypes(_TempBoard);

                try {
                    Thread.sleep(1000,0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (this._DifficultyLevel == Difficulty.Hard && enemyPieceCount == 1 && computerCount == 0)
                {
                    this._IsClockWise = true;
                    this._Quad = (short)1;
                    if (this._TempBoard[7] == 1)
                        this._Choice = 28;
                    else if (this._TempBoard[10] == 1)
                        this._Choice = 25;
                    else if (this._TempBoard[25] == 1)
                        this._Choice = 10;
                    else if (this._TempBoard[28] == 1)
                        this._Choice = 7;
                    else
                    {
                        Hashtable<String,Double> hashTable = new Hashtable();
                        alphaBeta(this._TempBoard, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, hashTable);
                    }

                }
                else
                {
                    Hashtable<String,Double> hashTable = new Hashtable();
                    alphaBeta(this._TempBoard, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, hashTable);
                }
            }

            private double alphaBeta(int[] board, int treeDepth, double alpha, double beta, Hashtable<String,Double> hashTable)
            {
                if (treeDepth == this._MaxTreeDepth || CheckForWin(board) != 0)
                    return GameScore(board, treeDepth);

                treeDepth++;
                double result;
                List<Integer> availableMoves = GetAvailableMoves(board);
                int move;
                int[] possible_game;
                String possible_game_string;
                int maxNumberOfAvailableMoves = availableMoves.size();
                if (_Active_Turn == "COMPUTER")
                {
                    for (int i = 0; i < maxNumberOfAvailableMoves; ++i)
                    {
                        for (int quadrant = 1; quadrant < 5; ++quadrant)
                        {
                            for (int rotationDirection = 0; rotationDirection < 2; ++rotationDirection)
                            {
                                move = availableMoves.get(i);
                                possible_game = PlacePiece(move, board);
                                possible_game = MakeRotation(quadrant, rotationDirection, possible_game);

                                possible_game_string = ConvertIntArrayToString(possible_game);
                                if (hashTable.get(possible_game_string) != null)
                                    result = (double)hashTable.get(possible_game_string);
                                else
                                {
                                    result = alphaBeta(possible_game, treeDepth, alpha, beta, hashTable);
                                    hashTable.put(possible_game_string, result);
                                }

                                board = UndoMove(board, move, quadrant, rotationDirection);
                                if (result > alpha)
                                {
                                    alpha = result;
                                    if (treeDepth == 1)
                                    {
                                        this._Choice = move;
                                        if (rotationDirection == 0)
                                            this._IsClockWise = false;
                                        else
                                            this._IsClockWise = true;
                                        this._Quad = (short)quadrant;
                                    }
                                }
                                else if (alpha >= beta)
                                    return alpha;
                            }
                        }
                    }
                    return alpha;
                }
                else
                {
                    for (int i = 0; i < maxNumberOfAvailableMoves; ++i)
                    {
                        for (int quadrant = 1; quadrant < 5; ++quadrant)
                        {
                            for (int rotationDirection = 0; rotationDirection < 2; ++rotationDirection)
                            {
                                move = availableMoves.get(i);
                                possible_game = PlacePiece(move, board);
                                possible_game = MakeRotation(quadrant, rotationDirection, possible_game);

                                possible_game_string = ConvertIntArrayToString(possible_game);
                                if (hashTable.get(possible_game_string) != null)
                                    result = (double)hashTable.get(possible_game_string);
                                else
                                {
                                    result = alphaBeta(possible_game, treeDepth, alpha, beta, hashTable);
                                    hashTable.put(possible_game_string, result);
                                }

                                board = UndoMove(board, move, quadrant, rotationDirection);
                                if (result < beta)
                                {
                                    beta = result;
                                    if (treeDepth == 1)
                                    {
                                        this._Choice = move;
                                        if (rotationDirection == 0)
                                            this._IsClockWise = false;
                                        else
                                            this._IsClockWise = true;
                                        this._Quad = (short)quadrant;
                                    }
                                }
                                else if (beta <= alpha)
                                    return beta;
                            }
                        }
                    }
                    return beta;
                }
            }

            private String ConvertIntArrayToString(int[] intArray)
            {
                StringBuilder builder = new StringBuilder();
                for (int value : intArray)
                    builder.append(value);
                return builder.toString();
            }

            private int[] MakeRotation(int quadrant, int rotationDirection, int[] board)
            {
                int[] possible_game;
                switch (quadrant)
                {
                    case 1:
                        if (rotationDirection == 1)
                            possible_game = RotateQuad1ClockWise(board);
                        else
                            possible_game = RotateQuad1CounterClockWise(board);
                        break;
                    case 2:
                        if (rotationDirection == 1)
                            possible_game = RotateQuad2ClockWise(board);
                        else
                            possible_game = RotateQuad2CounterClockWise(board);
                        break;
                    case 3:
                        if (rotationDirection == 1)
                            possible_game = RotateQuad3ClockWise(board);
                        else
                            possible_game = RotateQuad3CounterClockWise(board);
                        break;
                    case 4:
                        if (rotationDirection == 1)
                            possible_game = RotateQuad4ClockWise(board);
                        else
                            possible_game = RotateQuad4CounterClockWise(board);
                        break;
                    default:
                        //just to make compiler happy
                        possible_game = RotateQuad1ClockWise(board);
                        break;
                }
                ChangeTurn();
                return possible_game;
            }

            private int[] UndoMove(int[] board, int move, int c, int r)
            {
                switch (c)
                {
                    case 1:
                        if (r != 1)
                            board = RotateQuad1ClockWise(board);
                        else
                            board = RotateQuad1CounterClockWise(board);
                        break;
                    case 2:
                        if (r != 1)
                            board = RotateQuad2ClockWise(board);
                        else
                            board = RotateQuad2CounterClockWise(board);
                        break;
                    case 3:
                        if (r != 1)
                            board = RotateQuad3ClockWise(board);
                        else
                            board = RotateQuad3CounterClockWise(board);
                        break;
                    case 4:
                        if (r != 1)
                            board = RotateQuad4ClockWise(board);
                        else
                            board = RotateQuad4CounterClockWise(board);
                        break;
                    default:
                        //just to make compiler happy
                        board = RotateQuad1ClockWise(board);
                        break;
                }
                board[move] = 0;
                ChangeTurn();
                return board;
            }

            private int[] PlacePiece(int move, int[] board)
            {
                if (_Active_Turn == "COMPUTER")
                    board[move] = 2;
                else
                    board[move] = 1;
                return board;
            }

            private void ChangeTurn()
            {
                if (_Active_Turn == "COMPUTER")
                    _Active_Turn = "HUMAN";
                else
                    _Active_Turn = "COMPUTER";
            }

            private List<Integer> GetAvailableMoves(int[] board)
            {
                List<Integer> possibleMoves = new ArrayList<Integer>();
                for (int i = 0; i < BOARDSIZE; ++i)
                    if (board[i] == 0)
                        possibleMoves.add(i);
                return possibleMoves;
            }

            private int GameScore(int[] board, int treeDepth)
            {
                int newScore = 0;
                int checkWinner = CheckForWin(board);
                if (checkWinner == 1)
                    newScore = -999999;
                else if (checkWinner == 2)
                    newScore = 999999;
                else
                {
                    if (this._MaxTreeDepth == 3)
                        newScore = -CheckForPiecesLines(board, 1, 2);
                    else
                        newScore = CheckForPiecesLines(board, 2, 1);
                }
                return newScore;
            }

            private int CheckForPiecesLines(int[] board, int piece, int enemy)
            {
                int countPiece = 0;
                for (int i = 0; i < BOARDSIZE; ++i)
                {

                    //Horizontal pieces down
                    if (i % 6 == 0)
                    {
                        if (board[i] == piece && (board[i + 1] != enemy))
                            countPiece += 1;

                        if (board[i] == piece && board[i + 1] == piece && (board[i + 2] != enemy))
                            countPiece += 16;

                        if (board[i] == piece && board[i + 1] == piece && board[i + 2] == piece && (board[i + 3] != enemy))
                            countPiece += 40;

                        if (board[i] == piece && board[i + 1] == piece && board[i + 2] == piece && board[i + 3] == piece && (board[i + 4] != enemy))
                            countPiece += 100;
                    }


                    //Vertical pieces down
                    if (i >= 0 && i < 18)
                    {
                        if (board[i] == piece && (board[i + 6] != enemy))
                            countPiece += 1;
                        if (board[i] == piece && board[i + 6] == piece && (board[i + 12] != enemy))
                            countPiece += 16;
                        if (board[i] == piece && board[i + 6] == piece && board[i + 12] == piece && (board[i + 18] != enemy))
                            countPiece += 40;
                    }

                    //Vertical pieces up
                    if (this._DifficultyLevel != Difficulty.Beginner)
                    {
                        if (i <= 35 && i >= 18)
                        {
                            if (board[i] == piece && (board[i - 6] != enemy))
                                countPiece += 1;
                            if (board[i] == piece && board[i - 6] == piece && (board[i - 12] != enemy))
                                countPiece += 16;
                            if (board[i] == piece && board[i - 6] == piece && board[i - 12] == piece && (board[i - 18] != enemy))
                                countPiece += 40;
                        }
                    }

                    //Horizontal pieces
                    if (this._DifficultyLevel != Difficulty.Beginner && this._DifficultyLevel != Difficulty.Easy)
                    {
                        if (i < BOARDSIZE - 3)
                        {
                            if (board[i] == piece && board[i + 1] == piece && board[i + 2] == piece)
                                countPiece += 16;
                            if (i < BOARDSIZE - 4)
                                if (board[i] == piece && board[i + 1] == piece && board[i + 2] == piece && board[i + 3] == piece)
                                    countPiece += 32;
                        }
                    }

                    //Vertical pieces
                    if (this._DifficultyLevel != Difficulty.Beginner && this._DifficultyLevel != Difficulty.Easy)
                    {
                        if (i < BOARDSIZE - 12)
                        {
                            if (board[i] == piece && board[i + 6] == piece && board[i + 12] == piece)
                                countPiece += 16;
                            if (i < BOARDSIZE - 18)
                                if (board[i] == piece && board[i + 6] == piece && board[i + 12] == piece && board[i + 18] == piece)
                                    countPiece += 32;
                        }
                    }



                }

                if (this._DifficultyLevel == Difficulty.Hard)
                {
                    countPiece += CheckNEDiagonals(board, piece, enemy);
                    countPiece += CheckSEDiagonals(board, piece, enemy);
                    countPiece += CheckNWiagonals(board, piece, enemy);
                    countPiece += CheckSWDiagonals(board, piece, enemy);
                }

                return countPiece;
            }

            private int CheckNEDiagonals(int[] board, int piece, int enemy)
            {
                int countPiece = 0;

                if (board[24] == piece && board[19] == piece && (board[14] != enemy))
                    countPiece += 4;
                if (board[24] == piece && board[19] == piece && board[14] == piece && (board[9] != enemy))
                    countPiece += 8;

                if (board[19] == piece && board[14] == piece && (board[9] != enemy))
                    countPiece += 4;
                if (board[19] == piece && board[14] == piece && board[9] == piece && (board[4] != enemy))
                    countPiece += 8;

                if (board[14] == piece && board[9] == piece && (board[4] != enemy))
                    countPiece += 4;


                if (board[30] == piece && board[25] == piece && (board[10] != enemy))
                    countPiece += 4;
                if (board[30] == piece && board[25] == piece && board[10] == piece && (board[15] != enemy))
                    countPiece += 8;

                if (board[25] == piece && board[10] == piece && (board[15] != enemy))
                    countPiece += 4;
                if (board[25] == piece && board[10] == piece && board[15] == piece && (board[10] != enemy))
                    countPiece += 8;

                if (board[10] == piece && board[15] == piece && (board[10] != enemy))
                    countPiece += 4;
                if (board[10] == piece && board[15] == piece && board[10] == piece && (board[5] != enemy))
                    countPiece += 8;

                if (board[31] == piece && board[26] == piece && (board[21] != enemy))
                    countPiece += 4;
                if (board[31] == piece && board[26] == piece && board[21] == piece && (board[9] != enemy))
                    countPiece += 8;

                if (board[26] == piece && board[21] == piece && (board[16] != enemy))
                    countPiece += 4;
                if (board[26] == piece && board[21] == piece && board[16] == piece && (board[11] != enemy))
                    countPiece += 8;

                if (board[21] == piece && board[16] == piece && (board[11] != enemy))
                    countPiece += 4;

                return countPiece;
            }

            private int CheckSEDiagonals(int[] board, int piece, int enemy)
            {
                int countPiece = 0;

                if (board[1] == piece && board[8] == piece && (board[15] != enemy))
                    countPiece += 4;
                if (board[1] == piece && board[8] == piece && board[15] == piece && (board[22] != enemy))
                    countPiece += 8;

                if (board[8] == piece && board[15] == piece && (board[22] != enemy))
                    countPiece += 4;
                if (board[8] == piece && board[15] == piece && board[22] == piece && (board[29] != enemy))
                    countPiece += 8;

                if (board[15] == piece && board[22] == piece && (board[29] != enemy))
                    countPiece += 4;


                if (board[0] == piece && board[7] == piece && (board[14] != enemy))
                    countPiece += 4;
                if (board[0] == piece && board[7] == piece && board[14] == piece && (board[21] != enemy))
                    countPiece += 8;

                if (board[7] == piece && board[14] == piece && (board[21] != enemy))
                    countPiece += 4;
                if (board[7] == piece && board[14] == piece && board[21] == piece && (board[28] != enemy))
                    countPiece += 8;

                if (board[14] == piece && board[21] == piece && (board[28] != enemy))
                    countPiece += 4;
                if (board[14] == piece && board[21] == piece && board[28] == piece && (board[35] != enemy))
                    countPiece += 8;

                if (board[6] == piece && board[13] == piece && (board[20] != enemy))
                    countPiece += 4;
                if (board[6] == piece && board[13] == piece && board[20] == piece && (board[27] != enemy))
                    countPiece += 8;

                if (board[13] == piece && board[20] == piece && (board[27] != enemy))
                    countPiece += 4;
                if (board[13] == piece && board[20] == piece && board[27] == piece && (board[34] != enemy))
                    countPiece += 8;

                if (board[20] == piece && board[27] == piece && (board[34] != enemy))
                    countPiece += 4;

                return countPiece;
            }

            private int CheckNWiagonals(int[] board, int piece, int enemy)
            {
                int countPiece = 0;

                if (board[29] == piece && board[22] == piece && (board[15] != enemy))
                    countPiece += 4;
                if (board[29] == piece && board[22] == piece && board[15] == piece && (board[8] != enemy))
                    countPiece += 8;

                if (board[22] == piece && board[15] == piece && (board[8] != enemy))
                    countPiece += 4;
                if (board[22] == piece && board[15] == piece && board[8] == piece && (board[1] != enemy))
                    countPiece += 8;

                if (board[15] == piece && board[8] == piece && (board[1] != enemy))
                    countPiece += 4;


                if (board[35] == piece && board[28] == piece && (board[21] != enemy))
                    countPiece += 4;
                if (board[35] == piece && board[28] == piece && board[21] == piece && (board[14] != enemy))
                    countPiece += 8;

                if (board[28] == piece && board[21] == piece && (board[14] != enemy))
                    countPiece += 4;
                if (board[28] == piece && board[21] == piece && board[14] == piece && (board[7] != enemy))
                    countPiece += 8;

                if (board[21] == piece && board[14] == piece && (board[7] != enemy))
                    countPiece += 4;
                if (board[21] == piece && board[14] == piece && board[7] == piece && (board[0] != enemy))
                    countPiece += 8;

                if (board[34] == piece && board[27] == piece && (board[20] != enemy))
                    countPiece += 4;
                if (board[34] == piece && board[27] == piece && board[20] == piece && (board[13] != enemy))
                    countPiece += 8;

                if (board[27] == piece && board[20] == piece && (board[13] != enemy))
                    countPiece += 4;
                if (board[27] == piece && board[20] == piece && board[13] == piece && (board[6] != enemy))
                    countPiece += 8;

                if (board[20] == piece && board[13] == piece && (board[6] != enemy))
                    countPiece += 4;

                return countPiece;
            }

            private int CheckSWDiagonals(int[] board, int piece, int enemy)
            {
                int countPiece = 0;

                if (board[4] == piece && board[9] == piece && (board[14] != enemy))
                    countPiece += 4;
                if (board[4] == piece && board[9] == piece && board[14] == piece && (board[19] != enemy))
                    countPiece += 8;

                if (board[9] == piece && board[14] == piece && (board[19] != enemy))
                    countPiece += 4;
                if (board[9] == piece && board[14] == piece && board[19] == piece && (board[24] != enemy))
                    countPiece += 8;

                if (board[14] == piece && board[19] == piece && (board[24] != enemy))
                    countPiece += 4;


                if (board[5] == piece && board[10] == piece && (board[15] != enemy))
                    countPiece += 4;
                if (board[5] == piece && board[10] == piece && board[15] == piece && (board[20] != enemy))
                    countPiece += 8;

                if (board[10] == piece && board[15] == piece && (board[20] != enemy))
                    countPiece += 4;
                if (board[10] == piece && board[15] == piece && board[20] == piece && (board[25] != enemy))
                    countPiece += 8;

                if (board[15] == piece && board[20] == piece && (board[25] != enemy))
                    countPiece += 4;
                if (board[15] == piece && board[20] == piece && board[25] == piece && (board[30] != enemy))
                    countPiece += 8;

                if (board[11] == piece && board[16] == piece && (board[21] != enemy))
                    countPiece += 4;
                if (board[11] == piece && board[16] == piece && board[21] == piece && (board[26] != enemy))
                    countPiece += 8;

                if (board[16] == piece && board[21] == piece && (board[26] != enemy))
                    countPiece += 4;
                if (board[16] == piece && board[21] == piece && board[26] == piece && (board[31] != enemy))
                    countPiece += 8;

                if (board[21] == piece && board[26] == piece && (board[31] != enemy))
                    countPiece += 8;

                return countPiece;
            }
            public String getName()
            {
                return this._Name;
            }



            public int GetMoveChoice()
            {
                if (_Choice == 0 || _Choice == 1 || _Choice == 2
                        || _Choice == 15 || _Choice == 16 || _Choice == 17
                        || _Choice == 18 || _Choice == 19 || _Choice == 20
                        || _Choice == 33 || _Choice == 34 || _Choice == 35)
                {

                }
                else if (_Choice == 3 || _Choice == 4 || _Choice == 5)
                {
                    _Choice += 6;
                }
                else if (_Choice == 6 || _Choice == 7 || _Choice == 8)
                {
                    _Choice -= 3;
                }
                else if (_Choice == 9 || _Choice == 10 || _Choice == 11)
                {
                    _Choice += 3;
                }
                else if (_Choice == 12 || _Choice == 13 || _Choice == 14)
                {
                    _Choice -= 6;
                }
                else if (_Choice == 21 || _Choice == 22 || _Choice == 23)
                {
                    _Choice += 6;
                }
                else if (_Choice == 24 || _Choice == 25 || _Choice == 26)
                {
                    _Choice -= 3;
                }
                else if (_Choice == 27 || _Choice == 28 || _Choice == 29)
                {
                    _Choice += 3;
                }
                else if (_Choice == 30 || _Choice == 31 || _Choice == 32)
                {
                    _Choice -= 6;
                }
                return this._Choice;
            }

            public boolean GetRotationDirection()
            {
                return this._IsClockWise;
            }

            public short GetCuadrant()
            {
                return this._Quad;
            }

            //All about board rotations and check for winner
            private int[] RotateQuad1ClockWise(int[] board)
            {
                int placeHolder = board[0];
                board[0] = board[12];
                board[12] = board[14];
                board[14] = board[2];
                board[2] = placeHolder;
                placeHolder = board[6];
                board[6] = board[13];
                board[13] = board[8];
                board[8] = board[1];
                board[1] = placeHolder;

                return board;
            }

            private int[] RotateQuad1CounterClockWise(int[] board)
            {
                int placeHolder = board[0];
                board[0] = board[2];
                board[2] = board[14];
                board[14] = board[12];
                board[12] = placeHolder;
                placeHolder = board[6];
                board[6] = board[1];
                board[1] = board[8];
                board[8] = board[13];
                board[13] = placeHolder;

                return board;
            }

            private int[] RotateQuad2ClockWise(int[] board)
            {

                int placeHolder = board[3];
                board[3] = board[15];
                board[15] = board[17];
                board[17] = board[5];
                board[5] = placeHolder;
                placeHolder = board[4];
                board[4] = board[9];
                board[9] = board[16];
                board[16] = board[11];
                board[11] = placeHolder;

                return board;
            }

            private int[] RotateQuad2CounterClockWise(int[] board)
            {
                int placeholder = board[3];
                board[3] = board[5];
                board[5] = board[17];
                board[17] = board[15];
                board[15] = placeholder;
                placeholder = board[4];
                board[4] = board[11];
                board[11] = board[16];
                board[16] = board[9];
                board[9] = placeholder;

                return board;
            }

            private int[] RotateQuad3ClockWise(int[] board)
            {
                int placeHolder = board[18];
                board[18] = board[30];
                board[30] = board[32];
                board[32] = board[20];
                board[20] = placeHolder;
                placeHolder = board[24];
                board[24] = board[31];
                board[31] = board[26];
                board[26] = board[19];
                board[19] = placeHolder;

                return board;
            }

            private int[] RotateQuad3CounterClockWise(int[] board)
            {
                int placeHolder = board[18];
                board[18] = board[20];
                board[20] = board[32];
                board[32] = board[30];
                board[30] = placeHolder;
                placeHolder = board[24];
                board[24] = board[19];
                board[19] = board[26];
                board[26] = board[31];
                board[31] = placeHolder;

                return board;
            }

            private int[] RotateQuad4ClockWise(int[] board)
            {
                int placeholder = board[21];
                board[21] = board[33];
                board[33] = board[35];
                board[35] = board[23];
                board[23] = placeholder;
                placeholder = board[22];
                board[22] = board[27];
                board[27] = board[34];
                board[34] = board[29];
                board[29] = placeholder;

                return board;
            }

            private int[] RotateQuad4CounterClockWise(int[] board)
            {
                int placeholder = board[21];
                board[21] = board[23];
                board[23] = board[35];
                board[35] = board[33];
                board[33] = placeholder;
                placeholder = board[22];
                board[22] = board[29];
                board[29] = board[34];
                board[34] = board[27];
                board[27] = placeholder;

                return board;
            }

            public int CheckForWin(int[] board)
            {
                boolean res = true;
                boolean p1w = false;
                boolean p2w = false;
                boolean tie = false;
                int numMoves = 0;
                for (int i = 0; i < BOARDSIZE; i++)
                    if (board[i] != 0)
                        numMoves++;

                if (numMoves >= 9) // First check to see if it's even possible to win (Fifth move for player 1)
                {
                    // Check for horizontal win. If no win, continue to checking vert and diag.
                    int horiz = CheckHorizontals();
                    if (horiz == 0) // No one won on a horizontal. Check for verticals.
                    {

                    }
                    else if (horiz == 1) // Player 1 won on a horizontal
                    {
                        p1w = true;
                        res = false;
                    }
                    else if (horiz == 2) // Player 2 wins on a horizontal
                    {
                        p2w = true;
                        res = false;
                    }
                    else
                    {
                        tie = true;
                        res = false;
                    }

                    int vert = CheckVerticals();
                    if (vert == 0) // No one won on a vertical. Check for diagonals.
                    {

                    }
                    else if (vert == 1) // Player 1 won on a vertical
                    {
                        p1w = true;
                        res = false;
                    }
                    else if (vert == 2) // Player 2 won on a vertical
                    {
                        p2w = true;
                        res = false;
                    }
                    else // vert is 3 (A tie was caused by the move)
                    {
                        tie = true;
                        res = false;
                    }

                    int diag = CheckDiags();
                    if (diag == 0) // No one won on a diagonal. Check to see if it's possible to make more moves.
                    {
                    }
                    else if (diag == 1) // Player 1 won on a diagonal
                    {
                        p1w = true;
                        res = false;
                    }
                    else if (diag == 2) // Player 2 won on a diagonal
                    {
                        p2w = true;
                        res = false;
                    }
                    else // diag is 3 (A tie was caused by the move)
                    {
                        tie = true;
                        res = false;
                    }

                    if (res && numMoves < MAXMOVES)
                        return 0; // The game continues
                    if (tie || (p1w && p2w))
                        return 3;
                    if (p1w)
                        return 1;
                    if (p2w)
                        return 2;
                    if (numMoves == MAXMOVES)
                        return 3;
                }
                return 0;
            }

            private int CheckHorizontals()
            {
                boolean res = true;
                boolean p1w = false;
                boolean p2w = false;

                int returnValue = 0;
                short[] possibilities = new short[12];
                possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4));
                possibilities[1] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4), new Point(0, 5));
                possibilities[2] = (short)CheckPiecesOnBoard(new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4));
                possibilities[3] = (short)CheckPiecesOnBoard(new Point(1, 1), new Point(1, 2), new Point(1, 3), new Point(1, 4), new Point(1, 5));
                possibilities[4] = (short)CheckPiecesOnBoard(new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4));
                possibilities[5] = (short)CheckPiecesOnBoard(new Point(2, 1), new Point(2, 2), new Point(2, 3), new Point(2, 4), new Point(2, 5));
                possibilities[6] = (short)CheckPiecesOnBoard(new Point(3, 0), new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 4));
                possibilities[7] = (short)CheckPiecesOnBoard(new Point(3, 1), new Point(3, 2), new Point(3, 3), new Point(3, 4), new Point(3, 5));
                possibilities[8] = (short)CheckPiecesOnBoard(new Point(4, 0), new Point(4, 1), new Point(4, 2), new Point(4, 3), new Point(4, 4));
                possibilities[9] = (short)CheckPiecesOnBoard(new Point(4, 1), new Point(4, 2), new Point(4, 3), new Point(4, 4), new Point(4, 5));
                possibilities[10] = (short)CheckPiecesOnBoard(new Point(5, 0), new Point(5, 1), new Point(5, 2), new Point(5, 3), new Point(5, 4));
                possibilities[11] = (short)CheckPiecesOnBoard(new Point(5, 1), new Point(5, 2), new Point(5, 3), new Point(5, 4), new Point(5, 5));

                for (short s : possibilities)
                {
                    if (s == 1)
                    {
                        p1w = true;
                        res = false;
                    }
                    if (s == 2)
                    {
                        p2w = true;
                        res = false;
                    }
                }

                if (res)
                    return 0;
                if (p1w && p2w)
                    return 3;
                if (p1w)
                    return 1;
                if (p2w)
                    return 2;
                return returnValue;
            }

            private int CheckVerticals()
            {
                boolean res = true;
                boolean p1w = false;
                boolean p2w = false;

                int returnValue = 0;
                short[] possibilities = new short[12];
                possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0));
                possibilities[1] = (short)CheckPiecesOnBoard(new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0));
                possibilities[2] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(4, 1));
                possibilities[3] = (short)CheckPiecesOnBoard(new Point(1, 1), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 1));
                possibilities[4] = (short)CheckPiecesOnBoard(new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2));
                possibilities[5] = (short)CheckPiecesOnBoard(new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(5, 2));
                possibilities[6] = (short)CheckPiecesOnBoard(new Point(0, 3), new Point(1, 3), new Point(2, 3), new Point(3, 3), new Point(4, 3));
                possibilities[7] = (short)CheckPiecesOnBoard(new Point(1, 3), new Point(2, 3), new Point(3, 3), new Point(4, 3), new Point(5, 3));
                possibilities[8] = (short)CheckPiecesOnBoard(new Point(0, 4), new Point(1, 4), new Point(2, 4), new Point(3, 4), new Point(4, 4));
                possibilities[9] = (short)CheckPiecesOnBoard(new Point(1, 4), new Point(2, 4), new Point(3, 4), new Point(4, 4), new Point(5, 4));
                possibilities[10] = (short)CheckPiecesOnBoard(new Point(0, 5), new Point(1, 5), new Point(2, 5), new Point(3, 5), new Point(4, 5));
                possibilities[11] = (short)CheckPiecesOnBoard(new Point(1, 5), new Point(2, 5), new Point(3, 5), new Point(4, 5), new Point(5, 5));

                for (short s : possibilities)
                {
                    if (s == 1)
                    {
                        p1w = true;
                        res = false;
                    }
                    if (s == 2)
                    {
                        p2w = true;
                        res = false;
                    }
                }

                if (res)
                    return 0;
                if (p1w && p2w)
                    return 3;
                if (p1w)
                    return 1;
                if (p2w)
                    return 2;
                return returnValue;
            }

            private int CheckDiags()
            {
                boolean res = true;
                boolean p1w = false;
                boolean p2w = false;

                int returnValue = 0;
                short[] possibilities = new short[8];

                // Top Left to Bottom Rights
                possibilities[0] = (short)CheckPiecesOnBoard(new Point(0, 1), new Point(1, 2), new Point(2, 3), new Point(3, 4), new Point(4, 5));
                possibilities[1] = (short)CheckPiecesOnBoard(new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4));
                possibilities[2] = (short)CheckPiecesOnBoard(new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(4, 4), new Point(5, 5));
                possibilities[3] = (short)CheckPiecesOnBoard(new Point(1, 0), new Point(2, 1), new Point(3, 2), new Point(4, 3), new Point(5, 4));
                // Bottom Left to Top Rights
                possibilities[4] = (short)CheckPiecesOnBoard(new Point(0, 4), new Point(1, 3), new Point(2, 2), new Point(3, 1), new Point(4, 0));
                possibilities[5] = (short)CheckPiecesOnBoard(new Point(0, 5), new Point(1, 4), new Point(2, 3), new Point(3, 2), new Point(4, 1));
                possibilities[6] = (short)CheckPiecesOnBoard(new Point(1, 4), new Point(2, 3), new Point(3, 2), new Point(4, 1), new Point(5, 0));
                possibilities[7] = (short)CheckPiecesOnBoard(new Point(1, 5), new Point(2, 4), new Point(3, 3), new Point(4, 2), new Point(5, 1));

                for (short s : possibilities)
                {
                    if (s == 1)
                    {
                        p1w = true;
                        res = false;
                    }
                    if (s == 2)
                    {
                        p2w = true;
                        res = false;
                    }
                }

                if (res)
                    return 0;
                if (p1w && p2w)
                    return 3;
                if (p1w)
                    return 1;
                if (p2w)
                    return 2;
                return returnValue;
            }

            private int CheckPiecesOnBoard(Point piece1, Point piece2, Point piece3, Point piece4, Point piece5)
            {
                int playerAtPiece1 = _TempBoard[BOARWIDTH * (int)piece1.x + (int)piece1.y];
                int playerAtPiece2 = _TempBoard[BOARWIDTH * (int)piece2.x + (int)piece2.y];
                int playerAtPiece3 = _TempBoard[BOARWIDTH * (int)piece3.x + (int)piece3.y];
                int playerAtPiece4 = _TempBoard[BOARWIDTH * (int)piece4.x + (int)piece4.y];
                int playerAtPiece5 = _TempBoard[BOARWIDTH * (int)piece5.x + (int)piece5.y];

                if (playerAtPiece1 == playerAtPiece2 && playerAtPiece2 == playerAtPiece3 &&
                        playerAtPiece3 == playerAtPiece4 && playerAtPiece4 == playerAtPiece5)
                    return playerAtPiece1;
                return 0;
            }
}
