Feature: Collision handling
As a GameElement
I can detect a collision with another element
So that I can respond appropriately

Scenario: pacman collides with pill
Given a pacman facing "left" at 3,3 with starting position 0,0
And a pill at 2,3
When we advance the game by 1 tick
Then the score should increase by 10 points
And there should not be a pill at 2,3

Scenario: pacman collides with power pill
Given a pacman facing "left" at 3,3 with starting position 0,0
And a power pill at 2,3
When we advance the game by 1 tick
Then the score should increase by 50 points
And there should not be a pill at 2,3

Scenario: pacman collides with unpanicked ghost
Given a pacman facing "right" at 2,3 with starting position 0,0
And a ghost at 3,3
When we advance the game by 1 tick
Then there should not be a pacman at 3,3
And the lives should decrease by 1


Scenario: ghost collides with a tunnel and moves slower
Given a starting map:
"""
+-++-+-+
| ##M|>|
+-++-+-+
"""
When we parse the map
And we advance the game by 5 tick
Then there should be a calm ghost at 1,1