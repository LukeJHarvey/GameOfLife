Conways Game of Life Rules:
- Any live cell with fewer than two live neighbors dies, as if by under population.
- Any live cell with two or three live neighbors lives on to the next generation.
- Any live cell with more than three live neighbors dies, as if by overpopulation.
- Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.

GameOfLifeBoard is the current implementation of the game  
GameOfLifeTile is an implementation from High School  
Make file only works for GameOfLifeBoard  

How To Play:
1. Run Terminal in Main Folder
2. In Terminal:
   - "make"
   - "java gameoflifeboard/GameOfLife 30 30" or "java gameoflifeboard/GameOfLife ex1"
3. Click a square to change it from alive to dead
4. Press space to pause and unpause

Terminal Commands
```
import filename
export filename
change size [column size]
exit
```

Features:
- [x] Functioning Game Of Life, w/ efficient board
- [x] Pause
- [x] Variable Board Sizes
- [x] Importing Template at Start
- [x] Terminal Thread
- [x] Importing from Terminal
- [x] Exporting from Terminal
- [x] Resizing board from Terminal
- [x] Resizing board in game
- [ ] Add Buttons to function as Exporting, and Importing
- [ ] Change makefile to organize files better
- [ ] Go back to last import
- [ ] Reverse?
- [ ] Online Database to upload Templates to
- [ ] Client to upload and download Templates

Current bugs:
Nothing right now!