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

package mctslib.mcts;

import java.util.Map;

import mctslib.game.Action;
import mctslib.game.State;

class Node<A extends Action> {
	State<A> state;
	Node<A> parent;
	//Map<Node<A>, A> children;
	Map<A, Node<A>> children;
	int numVisits;
	double value;
	
	Node(State<A> s, Node<A> p) {
		state = s;
		parent = p;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}

}
