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

package sample_game.player;

import java.util.Random;

import sample_game.Say10;
import sample_game.Say10Action;

/**
 * Random Say10 player
 * This player chooses an action at random.
 */
public class RandomPlayer extends Player {

	private Random rand;
	
	public RandomPlayer(Random r) {
		rand = r;
	}
	
	@Override
	public Say10Action getAction(Say10 state) {
		return new Say10Action(rand.nextInt(2) + 1);
	}

}
