package com.example.djprototype;

import java.util.ArrayList;
import java.util.List;

public class UserAction {
	boolean isUserTurn;
	List<Move> userActionHistory = new ArrayList<Move>();
	List<Move> correctActionList = new ArrayList<Move>();

	enum Move {
		sideSwing, frontSlide, verticalSlide
	}

	public void reset() {
		userActionHistory = new ArrayList<Move>();
	}

	public void changeUserTurn() {
		isUserTurn = !isUserTurn;
	}

	public void addUserAction(Move move) {
		if (isUserTurn) {
			userActionHistory.add(move);
		}
	}

	public void addCorrectAction(Move move) {
		correctActionList.add(move);
	}

	public boolean isFinishedUserAction() {
		if (isUserTurn) {
			int correctSize = correctActionList.size();
			int userActionSize = userActionHistory.size();
			return userActionSize >= correctSize;
		} else {
			return false;
		}

	}

	public boolean isCorrectUserAction() {
		int correctSize = correctActionList.size();
		int userActionSize = userActionHistory.size();
		if (correctSize > userActionSize) {
			return false;
		}
		for (int i = 0; i < correctSize; i++) {
			if (userActionHistory.get(i) != correctActionList.get(i)) {
				return false;
			}
		}
		return true;
	}
}
