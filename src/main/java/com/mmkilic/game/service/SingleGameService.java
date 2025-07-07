package com.mmkilic.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.mmkilic.game.entity.ResponseGuess;

import jakarta.servlet.http.HttpSession;

@Service
public class SingleGameService {
	private static final String SECRET_ATTR = "secretNumber";
    private static final String ATTEMPTS_ATTR = "attempts";
	
	public ResponseGuess start(HttpSession session, int digitCount) {
		if(digitCount > 10 || digitCount < 3) throw new RuntimeException("Wrong digitCount!");
		String secret = secretNumberGenerator(digitCount);
        session.setAttribute(SECRET_ATTR, secret);
        session.setAttribute(ATTEMPTS_ATTR, 0);
		return new ResponseGuess(0, "-", false, 0, 0);
	}
	
    public ResponseGuess guess(HttpSession session, String guess) {
    	String secret = (String) session.getAttribute(SECRET_ATTR);
        if (secret == null) 
        	throw new RuntimeException("Number cannot generated!");
        if(!guessVerification(secret, guess))
        	throw new RuntimeException("Guess Number is not valid!");
        
        var guessResult = guessChecker(secret, guess);
        
        Integer attempts = (Integer) session.getAttribute(ATTEMPTS_ATTR);
        session.setAttribute(ATTEMPTS_ATTR, attempts + 1);
        
        boolean win = secret.length() == guessResult.get("positive");
        
        if(win) session.invalidate();
        
		return new ResponseGuess(
				attempts + 1, 
				guess,
				win, 
				guessResult.get("positive"), 
				guessResult.get("negative")
				);
	}
    
    private String secretNumberGenerator(int digitCount) {
    	String result = "";
    	
    	main:
    	do {
    		String potential = String.valueOf(ThreadLocalRandom.current().nextInt(0, 10));
    		for (char number: result.toCharArray()) {
				if(potential.equals(String.valueOf(number))) 
					continue main;
			}
    		result += potential;
    	} while(result.length() != digitCount);
    	
    	return result;
    }
    
    private boolean guessVerification(String secret, String guess) {
    	if(secret.length() != guess.length()) return false;
    	
    	List<Character> verifiedNumberList = new ArrayList<>();
    	
    	for (char gNumber : guess.toCharArray()) {
			for (char verifiedNumber : verifiedNumberList) {
				if(gNumber == verifiedNumber) return false;
			}
			verifiedNumberList.add(gNumber);
		}
    	
    	return true;
    }
    
    private Map<String, Integer> guessChecker(String secret, String guess){
    	Map<String, Integer> result = new HashMap<>();
    	
    	int positive = 0;
    	int negative = 0;
    	
    	for (int i = 0; i < guess.length(); i++) {
			for (int j = 0; j < secret.length(); j++) {
				if(guess.charAt(i) == secret.charAt(j)) {
					if(i == j) positive++;
					else negative++;
				}
			}
		}
    	
    	result.put("positive", positive);
    	result.put("negative", negative);
    	return result;
    }

}
