package com.kiri.hackjak.model;

import java.util.List;

public class SearchResult {
	
	public List<Step> steps;
	
	public SearchResult(List<Step> steps) {
		this.steps = steps;
	}
	
	public static class Step {
		/** The track that the person has to take. */
		public Trayek track;
		/** Index of road in {@link Trayek#route} where the person has to take. */
		public int boardIndex;
		/** Index of road in {@link Trayek#route} where the person has to alight / get down. */
		public int alightIndex;
		
		@Override
		public String toString() {
			return String.format("{track:%s, board:%d, alight:%d}", track.namaSingkat, boardIndex, alightIndex);
		}
	}
	
	@Override
	public String toString() {
		return steps.toString();
	}
}
