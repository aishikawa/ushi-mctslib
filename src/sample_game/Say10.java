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

import game.State;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Sample game.
 * rule: Players add 1 or 2 in order from 0. The winner is a who said 10.
 *
 */
public class Say10 extends State<Say10Action> {
	
	private int numPlayers;
	private int state;
	private int turn;
	private int winner;
	
	public Say10(int np) {
		numPlayers = np;
		state = 0;
		turn = 0;
		winner = -1;
	}
	
	public int getWinner() {
		return winner;
	}
	
	@Override
	public String toString() {
		return ""+state;
	}

	@Override
	public State<Say10Action> getDeepCopy() {
		Say10 ret = new Say10(numPlayers);
		ret.state = state;
		ret.turn = turn;
		ret.winner = winner;
		return ret;
	}

	@Override
	public void process(Say10Action action) {
		state += action.get();
		
		if (isTerminal()) {
			winner = turn;
		}
		
		turn += 1;
		if (turn >= numPlayers) {
			turn = 0;
		}
	}

	@Override
	public boolean isTerminal() {
		return state >= 10;
	}

	@Override
	public List<Say10Action> getLegalActions() {
		List<Say10Action> ret = new LinkedList<Say10Action>();
		ret.add(new Say10Action(1));
		ret.add(new Say10Action(2));
		return ret;
	}

	@Override
	public Map<Integer, Double> getScore() {
		Map<Integer, Double> ret = new HashMap<Integer, Double>();
		if (isTerminal()) {
			for (int i=0; i<numPlayers; i++) {
				double score = 0.0;
				if (i == winner) {
					score = 1.0;
				}
				ret.put(i, score);
			}
		} else {
			for (int i=0; i<numPlayers; i++) {
				ret.put(i, 0.5);
			}
		}
		
		return ret;
	}

	@Override
	public int getTurn() {
		return turn;
	}
	
}
