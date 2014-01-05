package com.example.djprototype;

import java.util.ArrayList;
import java.util.List;

public class UserAction {
	int actionCount;
	boolean isUserTurn;
	List<Move> userActionHistory = new ArrayList<Move>();
	List<Move> correctActionList = new ArrayList<Move>();

	enum Move {
		sideSwing, frontSlide, verticalSlide
	}

	public void reset() {
		actionCount = 0;
		userActionHistory = new ArrayList<Move>();
		correctActionList = new ArrayList<Move>();
	}

	public void changeUserTurn() {
		isUserTurn = !isUserTurn;
	}

	public void addCount() {
		actionCount++;
	}

	public void addUserAction(Move move) {
		if (isUserTurn) {
			userActionHistory.add(move);
			addCount();
		}
	}

	public void addCorrectAction(Move move) {
		correctActionList.add(move);
	}

	public boolean isFinishedUserAction() {
		if (isUserTurn) {
			int num = correctActionList.size();
			return actionCount >= num;
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
