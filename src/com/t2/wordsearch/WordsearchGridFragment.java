/**
 * 
 */
package com.t2.wordsearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.t2.wordsearch.WordsearchGridView.OnWordSelectedListener;

/**
 * @author wes
 * 
 */
public class WordsearchGridFragment extends Fragment implements OnWordSelectedListener {

	private int mRows = 8;
	private int mColumns = 8;

	private static String[] mWordList = { "YES", "WONDROUS", "WONDERFUL", "WONDER", "WITH", "WILLING", "WHOLE",
			"WELL", "WEALTHY", "VOYAGE", "VIVACIOUS", "VITAL", "VISUALIZE", "VISION", "VIGOROUS", "VICTORY", "VIBRANT",
			"VENERATED", "VARY", "VALUED", "UPBEAT", "UNWAVERING", "UNUSUAL", "UNITY", "UNIQUE", "UNDERSTANDING", "TRUTH", "TRUSTFUL",
			"TRIUMPH", "TRANSFORM", "TRANQUIL", "TOGETHER", "TODAY", "TIED", "THRIVE", "THOROUGH", "THIS", "THERAPEUTIC",
			"THANKFUL", "TEAM", "TAKE", "SYNCHRONIZED", "SUSTAIN", "SURE", "SUPPORT", "SUNNY", "SUCCESS", "STYLE", "STUPENDOUS",
			"STUNNING", "STRONG", "STIRRING", "STIR", "STEADY", "STABLE", "SPONTANEOUS", "SPLENDID", "SPIRIT", "SPARKLING",
			"SOUL", "SOLUTION", "SMOOTH", "SMILE", "SMART", "SINCERE", "SIMPLE", "SILENCE", "SHOWN", "SHINE", "SHIFT", "SERENITY",
			"SENSATIONAL", "SENSATION", "SELFLESS", "SECURE", "SAFE", "ROUSING", "ROBUST", "RICH", "REWARDING", "REVOLUTIONIZE",
			"REVERED", "RESTORE", "RESPECT", "RESOURCES", "RESOUND", "RESOLUTION", "REPLENISHED", "RENOWNED", "RENEW",
			"REMARKABLE", "RELY", "RELAX", "REJUVENATE", "REJOICE", "REFRESH", "REFINEMENT", "RECOGNIZED", "REALIZE", "READY", "QUIET",
			"QUICK-MINDED", "QUICK", "QUEST", "PURPOSE", "PROUD", "PROTECT", "PROSPEROUS", "PROMINENT", "PROJECT", "PRODUCTIVE",
			"PRINCIPLE", "PRETTY", "PREPARED", "POWERFUL", "POSITIVE", "POPULAR", "POLISH", "POISE", "POETIC", "PLETHORA", "PLENTY",
			"PLENTEOUS", "PLEASURE", "PHENOMENON", "PERSISTENT", "PERFECT", "PERCEPTIVE", "PEACEFUL", "PEACE", "PASSIONATE", "PARTY",
			"PARADISE", "ORIGINAL", "OPTIMISTIC", "OPENHANDED", "OPEN", "NURTURE", "NOVEL", "NOURISH", "NATURE", "MOVING",
			"MOTIVATE", "MOMENT", "MODIFY", "MISSION", "MIRACLE", "METAMORPHOSIS", "MEND", "MEDITATE", "MEANINGFUL",
			"MASTER", "MARVELOUS", "MAKE", "MAJESTIC", "MAINTAIN", "MAGNANIMOUS", "LUMINOUS", "LUCRATIVE", "LUCIDITY", "LOYAL",
			"LOVELINESS", "LOVE", "LIVELY", "LEGENDARY", "LEARN", "LEADER", "LAUGH", "KNOW", "KISS", "KINDHEARTED",
			"KIND", "KEEN", "JUBILATION", "JOVIAL", "JOINED", "JAZZED", "INVENTIVE", "INTUITIVE", "INTELLIGENT", "INTELLECTUAL",
			"INSTINCT", "INSTANTANEOUS", "INSPIRE", "INNOVATE", "INNATE", "INGENIOUS", "INDEPENDENT", "INCREASE", "INCOMPARABLE",
			"IMPECCABLE", "IMMENSE", "IMMACULATE", "IMAGINATIVE", "IDEAL", "HONORED", "HONEST", "HOLY", "HIGHEST", "HERE",
			"HELPFUL", "HEAVENLY", "HEART", "HEALTHY", "HEALED", "HARMONY", "HAPPY", "HANDSOME", "GUTSY", "GROW", "GRIN",
			"GREGARIOUS", "GREEN", "GRATITUDE", "GRACIOUS", "GRACEFUL", "GRACE", "GORGEOUS", "GOOD", "GLOW", "GLAD", "GIVE", "GIFTED",
			"GENUINE", "GENIUS", "GENEROUS", "GATHER", "FUNNY", "FULL", "FRIENDLY", "FREEDOM", "FORTUNATE", "FOLLOW", "FLOURISH",
			"FLEXIBLE", "FEAT", "FASCINATING", "FAMOUS", "FAMILY", "FAITH", "EXULTANT", "EXTRAORDINARY", "EXQUISITE", "EXPRESSIVE",
			"EXPRESS", "EXPLORE", "EXPAND", "EXHILARATING", "EXCITED", "EVERYONE", "ESTEEM", "ESTABLISHED", "ESSENCE",
			"EQUITABLE", "ENTIRELY", "ENTHUSE", "ENTERTAINING", "ENORMOUSLY", "ENJOY", "ENGAGING", "ENERGY", "ENERGETIC", "ENDORSE",
			"ENCOURAGE", "ENCOMPASSING", "EMPATHETIC", "EMBRACE", "ELOQUENT", "ELEGANCE", "ELECTRIFYING", "EFFORTLESS", "EFFICIENT",
			"EFFERVESCENT", "EFFECTIVE", "ECSTASY", "EASY", "EARNEST", "EAGER", "EACH", "DOUBT", "DONATE", "DIVINE", "DISTINGUISHED",
			"DISCOVER", "DISCIPLINED", "DIRECT", "DETERMINED", "DESERVING", "DELIGHT", "DEDICATED", "DAZZLING", "CUTE",
			"CURE", "CULTIVATE", "CUDDLE", "CREATE", "COURTEOUS", "COURAGEOUS", "COUPLED", "CORE", "COPIOUS", "CONVICTION", "CONTENT",
			"CONSTANT", "CONSCIOUS", "CONNECT", "CONGENIAL", "CONFIDENT", "COMRADESHIP", "COMPLETE", "COMPASSIONATE",
			"COMPANIONSHIP", "COMMEND", "COMFORTABLE", "CLOSENESS", "CLEVER", "CLEAN", "CLASSY", "CLARITY", "CHOOSE", "CHERISH",
			"CHEERFUL", "CHARMING", "CHARITABLE", "CHARISMATIC", "CHARACTER", "CHANGE", "CERTAIN", "CELEBRATE", "CARING", "CAPTIVATING",
			"CALM", "BURGEON", "BUNCH", "BUBBLY", "BRILLIANT", "BRIGHT", "BRAVE", "BOUNTY", "BOLD", "BLOOM", "BLISS", "BLESSED",
			"BIGHEARTED", "BENEVOLENT", "BENEFIT", "BELIEVE", "BEAUTIFUL", "BEAMING", "BASIC", "AUTHENTIC", "ATTRACTIVE", "ATTENTIVE",
			"ASTUTE", "ASTOUNDING", "ASTONISH", "ASSERTIVE", "ARTISTIC", "ARTICULATE", "APTITUDE", "APPROVE", "APPRECIATION", "ANSWER",
			"ANIMATED", "AMUSING", "AMITY", "AMAZE", "ALTER", "ALLOW", "ALLIANCE", "ALIVE", "AIRY", "AGREE", "AFFLUENT", "AFFIRMATIVE",
			"AFFIRM", "ADVENTURE", "ADORED", "ADOPT", "ADMIRE", "ADJUST", "ACUMEN", "ACTIVE", "ACHIEVEMENT", "ACCOMPLISHED", "ACCLAIMED",
			"ACCEPT", "ABUNDANT", "ABSOLUTELY"
	};

	private final Direction[] mDirections = Direction.values();
	private boolean[][] mLock;
	private int[] mRandomIndexes;

	private char[][] mBoard;
	private Set<Word> mSolution = new HashSet<Word>();
	private Set<Word> mFoundWords = new HashSet<Word>();

	private WordsearchListener mWordsearchListener;

	public void setWordsearchListener(WordsearchListener wordsearchListener) {
		mWordsearchListener = wordsearchListener;
	}

	private WordListAdapter mWordAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return LayoutInflater.from(getActivity()).inflate(R.layout.wordsearch_view, null);
	}

	public void onWordSelected(List<Integer> positions) {
		int firstPos = positions.get(0);
		int lastPos = positions.get(positions.size() - 1);
		StringBuilder forwardWord = new StringBuilder();
		StringBuilder reverseWord = new StringBuilder();
		for (Integer position : positions) {
			int row = position / mColumns;
			int col = position % mColumns;
			char c = mBoard[row][col];
			forwardWord.append(c);
			reverseWord.insert(0, c);
		}

		for (Word word : mSolution) {
			int wordStart = (word.getRow() * mColumns) + word.getCol();
			if (wordStart != firstPos && wordStart != lastPos) {
				continue;
			}

			Word found = (word.getWord().equals(forwardWord.toString())) ? word : null;
			if (found == null) {
				found = (word.getWord().equals(reverseWord.toString())) ? word : null;
			}

			if (found != null) {
				mFoundWords.add(found);
				getGridView().clearHint();
				getGridView().wordFound(found);
				mWordAdapter.setWordFound(found);
				break;
			}
		}

		if (mFoundWords.size() == mSolution.size()) {
			onPuzzleComplete();
		}
	}

	/**
	 * 
	 */
	private void onPuzzleComplete() {
		AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
		fadeOut.setFillAfter(true);
		fadeOut.setDuration(500);
		fadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				resetBoard();
				generateBoard();
				initBoard();
				AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
				anim.setDuration(500);
				getGridView().startAnimation(anim);
			}
		});
		getGridView().startAnimation(fadeOut);

		fadeOut = new AlphaAnimation(1.0f, 0.0f);
		fadeOut.setFillAfter(true);
		fadeOut.setDuration(500);
		fadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				getWordListGridView().startLayoutAnimation();
			}
		});

		if (mWordsearchListener != null) {
			mWordsearchListener.onPuzzleComplete();
		}

		Toast.makeText(getActivity(), "Puzzle complete, well done!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mColumns = getGridView().getNumColumns();
		mRows = getGridView().getNumRows();
		mBoard = new char[mRows][mColumns];
		mLock = new boolean[mRows][mColumns];

		resetBoard();

		if (savedInstanceState == null) {
			generateBoard();
		} else {
			List<Word> words = savedInstanceState.getParcelableArrayList("solution");
			for (Word word : words) {
				placeWord(word);
			}
			words = savedInstanceState.getParcelableArrayList("found");
			mFoundWords = new HashSet<Word>(words);
			getWordListGridView().setLayoutAnimation(null);

		}
		getGridView().setOnWordSelectedListener(this);
		initBoard();
		if (savedInstanceState == null) {
			AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
			anim.setDuration(500);
			getGridView().startAnimation(anim);
		}

	}

	private void initBoard() {
		getGridView().setBoard(mBoard);
		List<Word> sortedWords = new ArrayList<Word>(mSolution);
		Collections.sort(sortedWords);
		mWordAdapter = new WordListAdapter(getActivity(), sortedWords);
		mWordAdapter.setWordsFound(mFoundWords);
		getWordListGridView().setAdapter(mWordAdapter);
		getWordListGridView().setEnabled(false);
		getWordListGridView().setFocusable(false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("solution", new ArrayList<Word>(mSolution));
		outState.putParcelableArrayList("found", new ArrayList<Word>(mFoundWords));
	}

	private void generateBoard() {
		mRandomIndexes = new int[mRows * mColumns];

		Random rand = new Random(System.currentTimeMillis());
		for (int i = 0; i < mRandomIndexes.length; i++) {
			mRandomIndexes[i] = i;
		}

		for (int i = mRandomIndexes.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			int realIndex = mRandomIndexes[i];
			mRandomIndexes[i] = mRandomIndexes[randIndex];
			mRandomIndexes[randIndex] = realIndex;
		}

		for (int i = mWordList.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			String word = mWordList[i];
			mWordList[i] = mWordList[randIndex];
			mWordList[randIndex] = word;
		}

		for (String word : mWordList) {
			addWord(word);
		}

	}

	private GridView getWordListGridView() {
		return (GridView) getView().findViewById(R.id.grd_word_list);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.mnu_wordsearch, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mnu_hint) {
			List<Word> words = new ArrayList<Word>(mSolution);
			words.removeAll(mFoundWords);
			getGridView().showHint(words.get(0));
		}
		return super.onOptionsItemSelected(item);
	}

	private WordsearchGridView getGridView() {
		return (WordsearchGridView) getView().findViewById(R.id.grd_wordsearch);
	}

	/**
	 * Attempts to add a word to the puzzle anywhere possible.
	 * 
	 * @param word
	 *            The word added
	 * @return The Word object representing this words position in the puzzle.
	 *         Null if the word could not be fit into the puzzle
	 */
	private Word addWord(String word) {
		if (word.length() > mColumns && word.length() > mRows) {
			return null;
		}

		Random rand = new Random();
		for (int i = mDirections.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			Direction direction = mDirections[i];
			mDirections[i] = mDirections[randIndex];
			mDirections[randIndex] = direction;
		}

		Direction bestDirection = null;
		int bestRow = -1;
		int bestCol = -1;
		int bestScore = -1;
		for (int index : mRandomIndexes) {
			int row = index / mColumns;
			int col = index % mColumns;
			for (Direction direction : mDirections) {
				int score = canFit(word, direction, row, col);
				if (score > bestScore) {
					bestRow = row;
					bestCol = col;
					bestDirection = direction;
					bestScore = score;
				}
			}
		}
		if (bestScore >= 0) {
			Word result = new Word(word, bestRow, bestCol, bestDirection);
			placeWord(result);
			return result;
		}

		return null;
	}

	/**
	 * Places the provided word in the puzzle at the given location. This
	 * assumes that tests have already been performed to check if the word will
	 * fit. Returns a representation of the word in the puzzle at the
	 * appropriate location.
	 */
	private void placeWord(Word word) {
		int curRow = word.getRow();
		int curCol = word.getCol();
		final String wordStr = word.getWord();
		final Direction direction = word.getDirection();
		for (int i = 0; i < wordStr.length(); i++) {
			char c = wordStr.charAt(i);

			mBoard[curRow][curCol] = c;
			mLock[curRow][curCol] = true;

			if (direction.isUp()) {
				curRow -= 1;
			} else if (direction.isDown()) {
				curRow += 1;
			}

			if (direction.isLeft()) {
				curCol -= 1;
			} else if (direction.isRight()) {
				curCol += 1;
			}
		}

		mSolution.add(word);
	}

	/**
	 * Determines if a given word can be placed at a given location in a
	 * particular direction. This returns a count of the number of characters
	 * shared with other words on the board. This is used for difficulty
	 * weighting
	 */
	private int canFit(String word, Direction direction, int row, int col) {
		if (getAvailableSpace(direction, row, col) < word.length()) {
			return -1;
		}

		int score = 0;
		int curRow = row;
		int curCol = col;
		for (int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);

			if (mLock[curRow][curCol] && mBoard[curRow][curCol] != c) {
				return -1;
			} else if (mLock[curRow][curCol]) {
				score++;
			}

			if (direction.isUp()) {
				curRow -= 1;
			} else if (direction.isDown()) {
				curRow += 1;
			}

			if (direction.isLeft()) {
				curCol -= 1;
			} else if (direction.isRight()) {
				curCol += 1;
			}

		}

		return score;
	}

	/**
	 * @return The maximum possible length of a word placed at the given
	 *         location in a given direction.
	 */
	private int getAvailableSpace(Direction direction, int row, int col) {
		switch (direction) {
		case DOWN:
			return mRows - row;
		case DOWN_LEFT:
			return Math.min(mRows - row, col);
		case DOWN_RIGHT:
			return Math.min(mRows - row, mColumns - col);
		case LEFT:
			return col;
		case RIGHT:
			return mColumns - col;
		case UP:
			return row;
		case UP_LEFT:
			return Math.min(row, col);
		case UP_RIGHT:
			return Math.min(row, mColumns - col);

		}

		return 0;
	}

	private void resetBoard() {
		mFoundWords.clear();
		mSolution.clear();
		getGridView().reset();

		for (int i = 0; i < mLock.length; i++) {
			for (int j = 0; j < mLock[i].length; j++) {
				mLock[i][j] = false;
			}
		}

		for (int i = 0; i < mBoard.length; i++) {
			for (int j = 0; j < mBoard[i].length; j++) {
				mBoard[i][j] = 'Z';
			}
		}

		Random rand = new Random(System.currentTimeMillis());
		for (int i = mWordList.length - 1; i >= 1; i--) {
			int randIndex = rand.nextInt(i);
			String word = mWordList[i];
			mWordList[i] = mWordList[randIndex];
			mWordList[randIndex] = word;
		}

	}

	public static enum Direction {
		RIGHT(0),
		UP_RIGHT(315),
		UP(270),
		UP_LEFT(225),
		LEFT(180),
		DOWN_LEFT(135),
		DOWN(90),
		DOWN_RIGHT(45);

		private static final float RADIAN_SNAP = 0.785398f;

		private float mAngleDegree;
		private int mPositionShift;

		/**
		 * @param angleDegree
		 * @param positionShift
		 */
		private Direction(float angleDegree) {
			mAngleDegree = angleDegree;
		}

		public static Direction getDirection(float radians) {
			if (radians <= (.5 * RADIAN_SNAP) && radians >= -(.5 * RADIAN_SNAP)) {
				return RIGHT;
			} else if (radians > (.5 * RADIAN_SNAP) && radians < (1.5 * RADIAN_SNAP)) {
				return UP_RIGHT;
			} else if (radians >= (1.5 * RADIAN_SNAP) && radians <= (2.5 * RADIAN_SNAP)) {
				return UP;
			} else if (radians > (2.5 * RADIAN_SNAP) && radians < (3.5 * RADIAN_SNAP)) {
				return UP_LEFT;
			} else if (radians >= (3.5 * RADIAN_SNAP) || radians <= -(3.5 * RADIAN_SNAP)) {
				return LEFT;
			} else if (radians < -(2.5 * RADIAN_SNAP) && radians > -(3.5 * RADIAN_SNAP)) {
				return DOWN_LEFT;
			} else if (radians <= -(1.5 * RADIAN_SNAP) && radians >= -(2.5 * RADIAN_SNAP)) {
				return DOWN;
			} else {
				return DOWN_RIGHT;
			}
		}

		public boolean isAngle() {
			return (this == UP_RIGHT || this == DOWN_RIGHT || this == UP_LEFT || this == DOWN_LEFT);
		}

		public boolean isLeft() {
			return (this == LEFT || this == UP_LEFT || this == DOWN_LEFT);
		}

		public boolean isUp() {
			return (this == UP || this == UP_LEFT || this == UP_RIGHT);
		}

		public boolean isDown() {
			return (this == DOWN || this == DOWN_LEFT || this == DOWN_RIGHT);
		}

		public boolean isRight() {
			return (this == RIGHT || this == UP_RIGHT || this == DOWN_RIGHT);
		}

		public float getAngleDegree() {
			return mAngleDegree;
		}

		public int getPositionShift() {
			return mPositionShift;
		}

	}

	public static interface WordsearchListener {
		public void onPuzzleComplete();
	}

	/**
	 * Represents a word placed at an arbitrary location in a grid. Row and
	 * Column point to the position of the first character in the word,
	 * regardless of direction or reversal.
	 * 
	 * @author wes
	 * 
	 */
	public static final class Word implements Parcelable, Comparable<Word> {

		public static final Parcelable.Creator<Word> CREATOR = new Parcelable.Creator<Word>() {
			public Word createFromParcel(Parcel in) {
				return new Word(in);
			}

			public Word[] newArray(int size) {
				return new Word[size];
			}
		};

		private String mWord;
		private int mRow, mCol;
		private Direction mDirection;

		public Word(Parcel in) {
			mWord = in.readString();
			mRow = in.readInt();
			mCol = in.readInt();
			mDirection = Direction.valueOf(in.readString());
		}

		public int compareTo(Word another) {
			return mWord.compareTo(another.getWord());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Parcelable#describeContents()
		 */
		public int describeContents() {
			return 0;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
		 */
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mWord);
			dest.writeInt(mRow);
			dest.writeInt(mCol);
			dest.writeString(mDirection.name());
		}

		public Word(String word, int row, int col, Direction direction) {
			super();
			mWord = word;
			mRow = row;
			mCol = col;
			mDirection = direction;
		}

		/**
		 * @return the word
		 */
		public String getWord() {
			return mWord;
		}

		/**
		 * @return the row
		 */
		public int getRow() {
			return mRow;
		}

		/**
		 * @return the col
		 */
		public int getCol() {
			return mCol;
		}

		/**
		 * @return the direction
		 */
		public Direction getDirection() {
			return mDirection;
		}

		// public int getFirstPosition() {
		// return (mRow * mColumns) + mCol;
		// }

		@Override
		public String toString() {
			return "Word [mWord=" + mWord + ", mRow=" + mRow + ", mCol=" + mCol + ", mDirection=" + mDirection
					+ "]";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + mCol;
			result = prime * result + ((mDirection == null) ? 0 : mDirection.hashCode());
			result = prime * result + mRow;
			result = prime * result + ((mWord == null) ? 0 : mWord.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Word other = (Word) obj;
			if (mCol != other.mCol)
				return false;
			if (mDirection != other.mDirection)
				return false;
			if (mRow != other.mRow)
				return false;
			if (mWord == null) {
				if (other.mWord != null)
					return false;
			} else if (!mWord.equals(other.mWord))
				return false;
			return true;
		}

	}

	public static final class WordListAdapter extends BaseAdapter implements ListAdapter {

		private Context mContext;
		private List<Word> mWords;
		private Set<Word> mFoundWords;

		/**
		 * 
		 */
		public WordListAdapter(Context context, List<Word> words) {
			mContext = context;
			mWords = words;
			mFoundWords = new HashSet<Word>();
		}

		public int getCount() {
			return mWords.size();

		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		public void setWordsFound(Collection<Word> words) {
			mFoundWords.addAll(words);
		}

		public void setWordFound(Word word) {
			mFoundWords.add(word);
			notifyDataSetChanged();
		}

		public Object getItem(int position) {
			return mWords.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = LayoutInflater.from(mContext).inflate(R.layout.wordsearch_word, null);
			}

			Word word = (Word) getItem(position);
			TextView tv = (TextView) v.findViewById(R.id.lbl_word);
			v.setFocusable(false);
			tv.setFocusable(false);
			String wordStr = word.getWord().toLowerCase();
			wordStr = wordStr.substring(0, 1).toUpperCase() + wordStr.substring(1);
			tv.setText(wordStr);
			if (!mFoundWords.contains(word)) {
				tv.setTextColor(0xFFFFFFFF);
				tv.setTypeface(Typeface.DEFAULT_BOLD);
				tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
			} else {
				tv.setTextColor(0xFF777777);
				tv.setTypeface(Typeface.DEFAULT);
				tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

			return v;
		}
	}

}
