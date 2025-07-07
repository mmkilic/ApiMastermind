package com.mmkilic.game.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mmkilic.game.entity.ResponseGuess;
import com.mmkilic.game.service.SingleGameService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SingleGameController {
	private final SingleGameService srv;
	
	@PostMapping("/start")
	public ResponseEntity<ResponseGuess> start(HttpSession session, @RequestParam int digitNumber) {
		return ResponseEntity.ok(srv.start(session, digitNumber));
	}
	
	@PostMapping("/guess")
    public ResponseEntity<ResponseGuess> guess(HttpSession session, @RequestParam String guess) {
		return ResponseEntity.ok(srv.guess(session, guess));
	}
}
