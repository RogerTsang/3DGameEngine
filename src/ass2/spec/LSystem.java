package ass2.spec;

import java.util.HashMap;
import java.util.Map;

public class LSystem {
	private String first;
	private Map<Character, String> rules;
	private Map<Integer, String> iterations;
	
	public LSystem(String f, Map<Character, String> r) {
		first = f;
		rules = r;
		iterations = new HashMap<Integer, String>();
	}
	
	public LSystem() {
		iterations = new HashMap<Integer, String>();
	}
	
	public void addFirst(String f) {
		first = f;
	}
	
	public void addRules(Map<Character, String> r) {
		rules = r;
	}
	
	public String getIteration(int iterationNumber) {
		if (iterations.containsKey(iterationNumber)) {
			//Returns the stored iteration if it exists, to save calculation time at the cost of space.
			return iterations.get(iterationNumber); 
		} else {
			//Calculate iteration
			String currString = first;
			for (int i = 0; i < iterationNumber; i++) {
				StringBuilder currIteration = new StringBuilder();
				for (int j = 0; j < currString.length(); j++) {
					if (rules.containsKey(currString.charAt(j))) {
						currIteration.append(rules.get(currString.charAt(j)));
					} else {
						currIteration.append(currString.charAt(j));
					}
				}
				currString = currIteration.toString();
			}
			iterations.put(iterationNumber, currString);
			return currString;
		}
	}
}
