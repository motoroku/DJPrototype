package com.example.djprototype;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

import com.example.djprototype.UserAction.Move;

public class LessonStage {
	List<Uri>			lessonList		= new ArrayList<Uri>();
	List<List<Move>>	correctMoveList	= new ArrayList<List<Move>>();
	String				name;
	int					maxLessonSize;

	public LessonStage(int index) {
		name = "PRACTISE";
		createLesson();
		createCorrectMoveList();
	}

	public void createLesson() {
		// TODO ステージに合わせたレッスンをDBから取得する
		lessonList.add(0, R.raw.bgm_maoudamashii_acoustic04);
		lessonList.add(R.raw.bgm_maoudamashii_cyber07);
		lessonList.add(R.raw.bgm_maoudamashii_orchestra12);
		maxLessonSize = lessonList.size();
	}

	public void createCorrectMoveList() {
		// TODO ステージとレッスンに合わせた正解の動きをDBから取得する
		List<Move> moveList = new ArrayList<Move>();
		moveList.add(Move.sideSwing);
		moveList.add(Move.sideSwing);
		moveList.add(Move.frontSlide);
		correctMoveList.add(moveList);
		moveList.clear();
		moveList.add(Move.frontSlide);
		moveList.add(Move.frontSlide);
		moveList.add(Move.frontSlide);
		correctMoveList.add(moveList);
		moveList.clear();
		moveList.add(Move.highTap);
		moveList.add(Move.highTap);
		moveList.add(Move.highTap);
		correctMoveList.add(moveList);
		moveList.clear();
	}

	public Uri getLesson(int index) {
		Uri id = Uri.parse(lessonList.get(index));
		return id;
	}

	public List<Move> getCorrectMove(int index) {
		return correctMoveList.get(index);
	}
}
