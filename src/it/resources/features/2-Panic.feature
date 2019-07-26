Feature: Panic handling
As a Ghost
I can react to Power Pills being eaten
So that I can behave appropriately with Pacman

Scenario: pacman collides with power pill causes panic
Given a starting map:
"""
+--+-+
|o>|M|
+--+-+
"""
When we parse the map
And we advance the game by 49 tick
Then there should be a panicked ghost at 4,1

Scenario: ghosts recover from panic eventually
Given a starting map:
"""
+--+-+
|o>|M|
+--+-+
"""
When we parse the map
And we advance the game by 52 tick
Then there should be a calm ghost at 4,1

Scenario: ghosts recover from panic after eaten
Given a pacman facing "left" at 3,1 with starting position 0,0
And a ghost at 0,1 with starting position 10,10
And a power pill at 2,1
And walls at the following places:
  | icon|x:d|y:d|
  |  +  | 0 | 0 |
  |  +  | 1 | 0 |
  |  +  | 2 | 0 |
  |  +  | 3 | 0 |
  |  +  | 0 | 2 |
  |  +  | 1 | 2 |
  |  +  | 2 | 2 |
  |  +  | 3 | 2 |

When we advance the game by 2 tick
Then there should be a calm ghost at 10,10