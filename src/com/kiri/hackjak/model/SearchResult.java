package com.kiri.hackjak.model;

import java.util.List;

public class SearchResult {
	
	public List<Step> steps;
	
	public static class Step {
		/** The track that the person has to take. */
		public Trayek track;
		/** Index of road in {@link Trayek#route} where the person has to take. */
		public int boardIndex;
		/** Index of road in {@link Trayek#route} where the person has to alight / get down. */
		public int alightIndex;
	}
}
