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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mctslib.game.Action;
import mctslib.game.State;

public class Searcher<A extends Action> implements Runnable {

	private int EXPAND_THRESHOLD;
	private long TIME_LIMIT;
	private double C;
	private Random rand;
	
	private Node<A> rootNode;
	
	Searcher(int e, long t, double c, long seed, State<A> rootState) {
		EXPAND_THRESHOLD = e;
		TIME_LIMIT = t;
		C = c;
		rand = new Random(seed);
		
		rootNode = new Node<A>(rootState, null);
	}
	
	@Override
	public void run() {
		long sTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - sTime < TIME_LIMIT) {
			Node<A> selected = select(rootNode);
			if (selected.numVisits >= EXPAND_THRESHOLD - 1) {
				expand(selected);
			}
			Map<Integer, Double> score = playout(selected);
			backPropagate(selected, score);
		}
	}
	
	//public Map<Node<A>, A> getRootChildren() {
	public Map<A, Node<A>> getRootChildren() {
		return rootNode.children;
	}
	
	private Node<A> select(Node<A> node) {
		if (node.state.isTerminal() || node.numVisits < EXPAND_THRESHOLD) {
			return node;
		}
		
		ArrayList<Node<A>> candidates = new ArrayList<Node<A>>();
		for (Node<A> n : node.children.values()) {
			if (n.numVisits < EXPAND_THRESHOLD) {
				candidates.add(n);
			}
		}
		int size = candidates.size();
		if (size != 0) {
			return candidates.get(rand.nextInt(size));
		}
		
		Node<A> ret = null;
		double bestScore = -100.0;
		for (Node<A> n : node.children.values()) {
			double score = n.value/n.numVisits + C * Math.sqrt(Math.log(n.parent.numVisits)/n.numVisits);
			if (ret == null || score > bestScore) {
				bestScore = score;
				ret = n;
			}
		}

		return select(ret);
	}
	
	private void expand(Node<A> node) {
		node.children = new HashMap<A, Node<A>>();
		
		List<A> actions = node.state.getLegalActions();
		for (A a : actions) {
			State<A> nextState = node.state.getDeepCopy();
			nextState.process(a);
			Node<A> child = new Node<A>(nextState, node);
			node.children.put(a, child);
		}
	}
	
	private Map<Integer, Double> playout(Node<A> node) {

		State<A> state = node.state.getDeepCopy();
		while (state.isTerminal() == false) {
			List<A> actions = state.getLegalActions();

			A m = actions.get(rand.nextInt(actions.size()));				
			state.process(m);
		}


		Map<Integer, Double> scoreMap = state.getScore();

		return scoreMap;
	}
	
	private void backPropagate(Node<A> node, Map<Integer, Double> score) {
		node.numVisits += 1;
		while (node.parent != null) {
			node.value += score.get(node.parent.state.getTurn());
			node = node.parent;
			node.numVisits += 1;
		}
	}
}
