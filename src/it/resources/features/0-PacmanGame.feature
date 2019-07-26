Feature: Main Game
As a player
I can instantiate the game
So that I can play it


Scenario: basic starter game
Given a pacman game using the file "test.txt"
When we run the game for 1 ticks
Then the output should be:
"""
3    0
+----+
|    |
|    |
|GAME|
+OVER+
|M| <|
+----+

"""