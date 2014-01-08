package com.example.djprototype;

import java.util.ArrayList;
import java.util.List;

public class UserAction {
	boolean		isUserTurn;
	List<Move>	userActionHistory	= new ArrayList<Move>();
	LessonStage	mLessonStage;

	enum Move {
		sideSwing, frontSlide, verticalSlide, highTap, middleTap, lowTap
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

	public boolean isFinishedUserAction(List<Move> correctMove) {
		if (isUserTurn) {
			int correctSize = correctMove.size();
			int userActionSize = userActionHistory.size();
			return userActionSize >= correctSize;
		} else {
			return false;
		}

	}

	public boolean isCorrectUserAction(List<Move> correctMove) {
		int correctSize = correctMove.size();
		int userActionSize = userActionHistory.size();
		if (correctSize > userActionSize) {
			return false;
		}
		for (int i = 0; i < correctSize; i++) {
			if (userActionHistory.get(i) != correctMove.get(i)) {
				return false;
			}
		}
		return true;
	}
}
