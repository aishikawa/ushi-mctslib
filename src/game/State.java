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

package game;

import java.util.List;
import java.util.Map;

/**
 * 
 * Game state. Your game must extend this.
 *
 * @param <A> correspond action
 */
public abstract class State<A extends Action> {
	
	/**
	 * 
	 * @return deep copy of the instance
	 */
	public abstract State<A> getDeepCopy();
	
	/**
	 * change state by the action
	 * @param action
	 */
	public abstract void process(A action);
	
	/**
	 * 
	 * @return true if this is a terminal state
	 */
	public abstract boolean isTerminal();
	
	/**
	 * 
	 * @return list of legal actions of the state
	 */
	public abstract List<A> getLegalActions();
	
	/**
	 * 
	 * @return a map of player to score of the state
	 */
	public abstract Map<Integer, Double> getScore();
	
	/**
	 * 
	 * @return player index who actions in the state
	 */
	public abstract int getTurn();
}
