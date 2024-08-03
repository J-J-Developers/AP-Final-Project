

# Online Court Piece Game

## Introduction
Welcome to the Online Court Piece Game project! This is a Java-based online multiplayer card game where players can enjoy the traditional Persian game of Court Piece.

## Features
### Online Gameplay
1. Online Court Piece Game: Play friendly matches or join random games with other players.

### Client-Side Features
1. Beautiful User Interface: Enjoy a visually appealing and user-friendly interface.
2. Card Sorting: Easily sort cards by type for a smoother gameplay experience.

### Server-Side Features
1. Multiple Concurrent Games: Host multiple games simultaneously.
2. Game Data Storage: Save all game information including:
    - Number of rounds
    - Rul for each round
    - Number of sets played
    - Scores
    - Players
    - Details of each set
    - Cards played by each player in each set
3. Real-Time Game Status: Display real-time information of all games on the server with the GAMES STATUS command.
4. Player Disconnection Handling: If a player disconnects, a JOption will prompt other players to start a new game to avoid waiting.

## Installation
To run this project locally, follow these steps:

1. Clone the repository:
  
   git clone https://github.com/J-J-Developers/AP-Final-Project.git
   cd AP-Final-Project
   
2. Build the project:
  
   ./gradlew build
   
3. Run the server:
  
   ./gradlew run
   
## Usage
- Open your browser and navigate to http://localhost:8080 to start playing.
- Register a new account or log in with your existing credentials.
- Join an existing game or create a new one and invite your friends.

## Contributing
We welcome contributions! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Make your changes and commit them (git commit -m 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Open a pull request.

## Team Members and Contact
This project was developed by students of Noshirvani University of Technology:
- Arshia Mohammadzadeh - [mohammadzade.arshia84@gmail.com](mailto:mohammadzade.arshia84@gmail.com)
- Amir Mohammad Arabi - [arabiamir2005@gmail.com](mailto:arabiamir2005@gmail.com)
- Zeynab Abbasi - [darya.abbasi1383@gmail.com](mailto:darya.abbasi1383@gmail.com)
- Vania Vafaei - [vafaivania@gmail.com](mailto:vafaivania@gmail.com)

If you have any questions or feedback, feel free to reach out to us at the emails above.


## Gameplay Showcase
### Lobby
![Main Menu](src/main/java/Images/Lobby5.gif)

### Card Distribution and Rul Selection
![Card Distribution and Rul Selection](src/main/java/Images/Card%20dividing.gif)

### In-Game Play
![Game Play](src/main/java/Images/Card%20Puting.gif)

---






