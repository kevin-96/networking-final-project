# networking-final-project

This is a game made for the final project of CSC340. The game we made is a variation of the commonly known Mastermind game.

## Team Roles:
### The team was divided into two subgroups
Each subgroup spent most of their time working together on their half of the program (client or server). However, we had multiple meetings where everyone in both groups worked together. Tasks were divided fairly within subgroups, where members tried to take portions that lined up with their strengths. 

To work together, we used Git and the Live Share extension for Visual Studio Code.

#### Server Side: 
* James Jacobson
* Ryan Clark
* Phillip Nam
#### Client Side:
* Joey Germain
* Kevin Sangurima
* Brian Carballo

## How to Build:
1. Download and install the IntelliJ IDE
2. Import the "networking-final-project" folder as a project
3. From the "Build" menu, select "Build Artifacts"
4. The `networking-final-project/out/artifacts` folder should contain JAR files for the client and server

## How To Play (Instructions):
### Setup
1. Start the server (`java -jar Server.jar`)
2. In the same window, type the port number for the server to listen on, then hit enter
3. Start the client (`java -jar Client.jar`) once for each client you would like to create
4. In the client window that opens up, select the "Connection Settings" button on the top right
5. In the dialog box that pops up, fill out the fields and hit "OK"
    * If you don't want to particate in the game and only want to watch, then select the checkbox next to "Join as spectator." You will not be able to submit guesses, and your name will not be displayed to other players.
### Gameplay
The goal of the game is to crack the 4-digit code that is randomly generated. The first player to guess the code correctly is the winner.
1. Using the arrow symbols located at the bottom of the screen, select a number for each of the four number slots. *All 4 numbers must be   distinct (i.e. no repeated digits).*
2. Once you've selected four numbers, press the "Submit Guess" button
3. The table located above the numbers will display the following: The guess, hit count, and blow count. Other players' latest hit and blow counts will be displayed on the right-hand side.
4. All players continue to submit guesses simultaneously until someone guesses the correct code. Once this happens, a window will display saying which player won and what the code was. At this point, the game resets and a new code is generated.
### Hits and Blows
A player's **Hit** count increases when a number in his/her guess matches a number in the code, *including* its position within the code

A player's **Blow** count increases when a number in his/her guess matches a number in the code but is in the wrong position

#### Example:
- Code: **0243**
- Guess: **0314**
- Hit Count: **1** (the '0' is in the code at the same position as in the guess)
- Blow Count: **2** (the '3' and '4' are in the code, but the positions of the digits are different)