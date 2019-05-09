# ushi-mctslib
This is an implementation of a Monte Carlo Tree Search.
This implementation can be used for turn based n-person games.

## Usage

### Build
Use ant.
```shell
$ ant
```
Above comand creates some files in `dest` directory.

### How to make mcts player for your game
1. Write a class of *YOUR_GAME_ACTION* extends `mctslib.game.Action`
2. Write a class of *YOUR_GAME* extends `mctslib.game.State<YOUR_GAME_ACTION>`
3. Instantiate `mctslib.mcts.Mcts<YOUR_GAME_ACTION>` and use `getAction`

See sample_game for details. 

## LICENSE
 Apache License Version 2.0

