/*
 * Copyright 2013 A.Ishikawa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample_game;

import java.util.Random;

import sample_game.player.MctsPlayer;
import sample_game.player.Player;
import sample_game.player.RandomPlayer;

public class Main {

	public static void main(String[] args) {
		int NUM_PLAYER = 2;
		Say10 game = new Say10(NUM_PLAYER);
		Player[] players = new Player[NUM_PLAYER];
		
		for (int i=0; i<NUM_PLAYER; i++) {
			players[i] = new RandomPlayer(new Random());
		}
		players[0] = new MctsPlayer();
		
		System.out.println("state: "+game);
		while (game.isTerminal() == false) {

			int turn = game.getTurn();
			Say10Action action = players[turn].getAction(game);

			System.out.println("turn: "+turn);
			System.out.println("action: "+action+"\n");
			game.process(action);
			System.out.println("state: "+game);
		}
		
		System.out.println("winner: "+game.getWinner());
	}

}
