This is code for solving the 8 puzzle.

Idea behind it is forming a game tree, so each board will have neighbouring boards which are possible states can transition to next.

Use of a priority que with a metric based on number of moves and manhattan distance to decide which moves to explore first

If initial state is valid, solve recognizes as being unsolvable.
