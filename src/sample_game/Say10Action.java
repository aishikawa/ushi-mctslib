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

import mctslib.game.Action;

/**
 * Action of a say10 
 * 
 */
public class Say10Action extends Action {
	private int number;
	
	public Say10Action(int n) {
		this.set(n);
	}
	
	public void set(int n) {
		if (n != 1 && n != 2) {
			throw new IllegalArgumentException();
		}
		number = n;
	}
	
	public int get() {
		return number;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Say10Action) {
			Say10Action s10a = (Say10Action)o;
			return number == s10a.number;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return number;
	}
	
	@Override
	public String toString() {
		return ""+number;
	}
}
