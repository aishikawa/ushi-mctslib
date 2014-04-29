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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

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
	
	private int numThreads;
		
	public Mcts() {
		EXPAND_THRESHOLD = 1;
		TIME_LIMIT = 1000;
		C = Math.sqrt(2);
		rand = new Random();
		verbose = false;
		numThreads = 1;
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
	
	public void setNumThreads(int n) {
		numThreads = n;
	}
	
	/**
	 * Selct an action with Monte Carlo Tree Search
	 * @param state
	 * @return action
	 */
	public A getAction(State<A> state) {		

		Set<Thread> threads = new HashSet<Thread>();
		Set<Searcher<A>> searchers = new HashSet<Searcher<A>>();
		for (int i=0; i<numThreads; i++) {
			State<A> root = state.getDeepCopy();
			Searcher<A> searcher = new Searcher<A>(EXPAND_THRESHOLD, TIME_LIMIT, C, rand.nextLong(), root);
			searchers.add(searcher);
			Thread thread = new Thread(searcher);
			thread.start();
			threads.add(thread);
		}
		
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		Map<A, Node<A>> children = new HashMap<A, Node<A>>();
		for (Searcher<A> s : searchers) {
			Map<A, Node<A>> threadChildren = s.getRootChildren();
			for (A a : threadChildren.keySet()) {
				Node<A> threadChild = threadChildren.get(a);
				if (children.containsKey(a)) {
					Node<A> node = children.get(a);
					node.numVisits += threadChild.numVisits;
					node.value += threadChild.value;
				} else {
					children.put(a, threadChild);
				}
			}
		}
		
		// Select the best average value action.
		A ret = null;
		double bestValue = -100.0;
		for (Entry<A, Node<A>> entry : children.entrySet()) {
			A action = entry.getKey();
			Node<A> node = entry.getValue();
			if (verbose) {
				System.out.println("action:"+action+"  average value:"+(node.value/node.numVisits)+"  num visits:"+node.numVisits);
			}
			double value = node.value / node.numVisits;
			if (ret == null || bestValue < value) {
				bestValue = value;
				ret = action;
			}
		}
		
		return ret;
	}
	
}
