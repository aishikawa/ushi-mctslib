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

/**
 * 
 * Monte Carlo Tree Search
 *
 * @param <A> action class
 */
public class Mcts<A extends Action> {
	private int EXPAND_THRESHOLD;
	private long TIME_LIMIT;
	private double C;
	private Random rand;
	private boolean verbose;

	
	public Mcts() {
		EXPAND_THRESHOLD = 1;
		TIME_LIMIT = 1000;
		C = Math.sqrt(2);
		rand = new Random();
		verbose = false;
	}
	
	/**
	 * Set EXPAND_THRESHOLD (default 1)
	 * The MCTS expand a node when visits number is EXPAND_THRESHOLD 
	 * @param threshold
	 */
	public void setExpandThreshold(int threshold) {
		EXPAND_THRESHOLD = threshold;
	}
	
	/**
	 * Set TIME_LIMIT (default 1000)
	 * The MCTS finish the search 
	 * @param l time limit in millisecond
	 */
	public void setTimeLimit(long l) {
		TIME_LIMIT = l;
	}
	
	/**
	 * Set C (default sqrt(2))
	 * The MCTS select a node maxixmize (average value) + C * sqrt(log(parent visits)/(visits))
	 * @param c
	 */
	public void setC(double c) {
		C = c;
	}
	
	/**
	 * set random
	 * @param r
	 */
	public void setRand(Random r) {
		rand = r;
	}		
	
	/**
	 * set verbose (default false)
	 * @param v
	 */
	public void setVerbose(boolean v) {
		verbose = v;
	}
	
	/**
	 * Selct an action with Monte Carlo Tree Search
	 * @param state
	 * @return action
	 */
	public A getAction(State<A> state) {
		long sTime = System.currentTimeMillis();
		
		// Monte Carlo Tree Search
		Node root = new Node(state, null);		
		while (System.currentTimeMillis() - sTime < TIME_LIMIT) {
			Node selected = select(root);
			if (selected.numVisits >= EXPAND_THRESHOLD - 1) {
				expand(selected);
			}
			Map<Integer, Double> score = playout(selected);
			backPropagate(selected, score);
		}
		
		// Select the best average value action.
		A ret = null;
		double bestValue = -100.0;
		for (Node n : root.children.keySet()) {
			if (verbose) {
				A action = root.children.get(n);
				System.out.println("action:"+action+"  average value:"+(n.value/n.numVisits)+"  num visits:"+n.numVisits);
			}
			double value = n.value / n.numVisits;
			if (ret == null || bestValue < value) {
				bestValue = value;
				ret = root.children.get(n);
			}			
		}
		
		return ret;
	}
	
	
	private Node select(Node node) {
		if (node.state.isTerminal() || node.numVisits < EXPAND_THRESHOLD) {
			return node;
		}
		
		ArrayList<Node> candidates = new ArrayList<Node>();
		for (Node n : node.children.keySet()) {
			if (n.numVisits < EXPAND_THRESHOLD) {
				candidates.add(n);
			}
		}
		int size = candidates.size();
		if (size != 0) {
			return candidates.get(rand.nextInt(size));
		}
		
		Node ret = null;
		double bestScore = -100.0;
		for (Node n : node.children.keySet()) {
			double score = n.value/n.numVisits + C * Math.sqrt(Math.log(n.parent.numVisits)/n.numVisits);
			if (ret == null || score > bestScore) {
				bestScore = score;
				ret = n;
			}
		}

		return select(ret);
	}
	
	private void expand(Node node) {
		node.children = new HashMap<Node, A>();
		
		List<A> actions = node.state.getLegalActions();
		for (A a : actions) {
			State<A> nextState = node.state.getDeepCopy();
			nextState.process(a);
			Node child = new Node(nextState, node);
			node.children.put(child, a);
		}
	}
	
	private Map<Integer, Double> playout(Node node) {

		State<A> state = node.state.getDeepCopy();
		while (state.isTerminal() == false) {
			List<A> actions = state.getLegalActions();

			A m = actions.get(rand.nextInt(actions.size()));				
			state.process(m);
		}


		Map<Integer, Double> scoreMap = state.getScore();

		return scoreMap;
	}
	
	private void backPropagate(Node node, Map<Integer, Double> score) {
		node.numVisits += 1;
		while (node.parent != null) {
			node.value += score.get(node.parent.state.getTurn());
			node = node.parent;
			node.numVisits += 1;
		}
	}
	
	private class Node {
		State<A> state;
		Node parent;
		Map<Node, A> children;
		int numVisits;
		double value;
		
		Node(State<A> s, Node p) {
			state = s;
			parent = p;
		}
		
		@Override
		public String toString() {
			return state.toString();
		}
	}
}
