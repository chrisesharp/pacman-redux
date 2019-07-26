Feature: Game Levels

As a player of Pacman
I can get a new challenge after I have eaten all the pills
So that I can continue enjoying the game.


Scenario: Level over when all pills are eaten
Given a starting map:
"""
+----+-+
|...>|M|
+----+-+
"""
When we parse the map
And we advance the game by 3 tick
Then the level should be over
And the score should increase by 30 points

# @leave
Scenario: game map resets for new level
Given a starting map:
"""
+----+-+
|...>|M|
+----+-+
"""
When we parse the map
And we play the game for 4 ticks
Then the score should increase by 40 points
And there should be a pill at 1,1
And there should be a pill at 2,1