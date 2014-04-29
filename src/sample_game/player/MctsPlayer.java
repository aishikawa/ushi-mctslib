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

import mctslib.mcts.Mcts;
import sample_game.Say10;
import sample_game.Say10Action;

/**
 * Say10 player 
 * This player chooses an action using Monte Carlo tree search
 */
public class MctsPlayer extends Player {

	private Mcts<Say10Action> mcts;
	
	public MctsPlayer() {
		mcts = new Mcts<Say10Action>();
		mcts.setTimeLimit(1000);
		mcts.setExpandThreshold(2);
		mcts.setVerbose(true);
		mcts.setNumThreads(2);
	}
	
	@Override
	public Say10Action getAction(Say10 state) {
		return mcts.getAction(state);
	}

}
