/*
 * 
 * WordSearchLib
 * 
 * Copyright © 2009-2012 United States Government as represented by 
 * the Chief Information Officer of the National Center for Telehealth 
 * and Technology. All Rights Reserved.
 * 
 * Copyright © 2009-2012 Contributors. All Rights Reserved. 
 * 
 * THIS OPEN SOURCE AGREEMENT ("AGREEMENT") DEFINES THE RIGHTS OF USE, 
 * REPRODUCTION, DISTRIBUTION, MODIFICATION AND REDISTRIBUTION OF CERTAIN 
 * COMPUTER SOFTWARE ORIGINALLY RELEASED BY THE UNITED STATES GOVERNMENT 
 * AS REPRESENTED BY THE GOVERNMENT AGENCY LISTED BELOW ("GOVERNMENT AGENCY"). 
 * THE UNITED STATES GOVERNMENT, AS REPRESENTED BY GOVERNMENT AGENCY, IS AN 
 * INTENDED THIRD-PARTY BENEFICIARY OF ALL SUBSEQUENT DISTRIBUTIONS OR 
 * REDISTRIBUTIONS OF THE SUBJECT SOFTWARE. ANYONE WHO USES, REPRODUCES, 
 * DISTRIBUTES, MODIFIES OR REDISTRIBUTES THE SUBJECT SOFTWARE, AS DEFINED 
 * HEREIN, OR ANY PART THEREOF, IS, BY THAT ACTION, ACCEPTING IN FULL THE 
 * RESPONSIBILITIES AND OBLIGATIONS CONTAINED IN THIS AGREEMENT.
 * 
 * Government Agency: The National Center for Telehealth and Technology
 * Government Agency Original Software Designation: WordSearchLib001
 * Government Agency Original Software Title: WordSearchLib
 * User Registration Requested. Please send email 
 * with your contact information to: robert.kayl2@us.army.mil
 * Government Agency Point of Contact for Original Software: robert.kayl2@us.army.mil
 * 
 */
package com.t2.wordsearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.t2.wordsearch.WordsearchGridFragment.Direction;
import com.t2.wordsearch.WordsearchGridFragment.Word;

/**
 * @author wes
 * 
 */
public class WordsearchGridView extends LinearLayout implements OnKeyListener {

	private int mDefaultColor;
	private int mSelectionColor;

	/**
	 * Vars used in calculations
	 */
	private int mColumnWidth;
	private int mColumns, mRows;
	private float mCornerRadius;
	private final float mScale = getResources().getDisplayMetrics().density;
	private final float mMinDistance = (int) (50.0 * mScale + 0.5);

	/**
	 * Vars related to selection and selection drawing
	 */
	private Integer mSelStartPosition;
	private Rect mSelStartRect;
	private Rect mSelEndRect;
	private Integer mSelectionSteps;
	private Direction mSelectionDirection;
	private Paint mPaint, mFoundPaint, mHintPaint;
	private List<View> mPreviousSelection;
	private Bitmap mFoundCache;

	/**
	 * State of the board
	 */
	private WordsearchGameState mGameState;

	/**
	 * Listeners
	 */
	private OnWordSelectedListener mOnWordSelectedListener;

	private boolean mFocusSelected;

	private String mLastWordFound;

	public WordsearchGridView(Context context) {
		super(context);
		mColumns = 8;
		mRows = 8;
		init();
	}

	public WordsearchGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.wordsearch);
		mColumns = a.getInt(R.styleable.wordsearch_android_numColumns, 8);
		mRows = mColumns;
		init();
	}

	public void clearHint() {
		mGameState.mHint = null;
	}

	public void clearSelection() {
		if (mSelectionDirection != null && mSelectionSteps != null) {
			for (View view : getSelectionViews()) {
				((TextView) view.findViewById(R.id.lbl_char)).setTextColor(mDefaultColor);
			}
			mOnWordSelectedListener.onWordSelected(getPositionsOnPath(mSelectionDirection, mSelStartPosition,
					mSelectionSteps));
		}
		mSelStartPosition = null;
		mSelectionSteps = null;
		mSelectionDirection = null;
		postInvalidate();
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED && mLastWordFound != null) {
			event.getText().add("Word Found: " + mLastWordFound);
			mLastWordFound = null;
			return true;
		}
		return super.dispatchPopulateAccessibilityEvent(event);
	}

	/**
	 * @return the cellSize
	 */
	public int getCellSize() {
		return mColumnWidth;
	}

	public View getChildAtPosition(int index) {
		int row = (int) Math.floor((double) index / (double) mColumns);
		int col = index % mColumns;
		LinearLayout rowView = (LinearLayout) getChildAt(row);
		return rowView.getChildAt(col);
	}

	public int getEndPosition(Direction direction, int startPosition, int steps) {
		int startRow = startPosition / mColumns;
		int startCol = startPosition % mColumns;
		int curRow = startRow;
		int curCol = startCol;

		for (int i = steps; i >= 0; i--) {
			curCol = startCol;
			curRow = startRow;

			if (direction.isUp()) {
				curRow -= steps;
			} else if (direction.isDown()) {
				curRow += steps;
			}

			if (direction.isLeft()) {
				curCol -= steps;
			} else if (direction.isRight()) {
				curCol += steps;
			}

			if (curRow < 0 || curCol < 0 || curRow >= mRows || curCol >= mColumns) {
				break;
			}
		}

		return (curRow * mColumns) + curCol;
	}

	public int getNumColumns() {
		return mColumns;
	}

	public int getNumRows() {
		return mRows;
	}

	public List<Integer> getPositionsOnPath(Direction direction, int startPosition, int steps) {
		List<Integer> positions = new ArrayList<Integer>();
		int curRow = startPosition / mColumns;
		int curCol = startPosition % mColumns;

		for (int i = 0; i <= steps; i++) {
			positions.add((curRow * mColumns) + curCol);

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

			if (curRow < 0 || curCol < 0 || curRow >= mRows || curCol >= mColumns) {
				break;
			}

		}
		return positions;
	}

	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
			Rect rect = getViewRect(v);
			selectionChanged(rect.centerX(), rect.centerY());
			if (mFocusSelected) {
				clearSelection();
			}

			TextView tv = (TextView) ((ViewGroup) getFocusedChild()).getFocusedChild().findViewById(R.id.lbl_char);
			if (tv != null) {
				tv.setTextColor(mFocusSelected ? 0xFF00870a : mSelectionColor);
			}

			mFocusSelected = !mFocusSelected;
		}
		return false;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof WordsearchGameState) {
			super.onRestoreInstanceState(((WordsearchGameState) state).getSuperState());
			mGameState = ((WordsearchGameState) state);
		} else {
			super.onRestoreInstanceState(state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.AbsListView#onSaveInstanceState()
	 */
	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable state = super.onSaveInstanceState();
		WordsearchGameState saveState = new WordsearchGameState(state);
		saveState.mHint = mGameState.mHint;
		saveState.mFoundWords = mGameState.mFoundWords;
		return saveState;
	}

	public void reset() {
		if (mPreviousSelection != null) {
			mPreviousSelection.clear();
		}
		mGameState = new WordsearchGameState();
		mSelStartPosition = null;
		mSelStartRect = null;
		mSelEndRect = null;
		mSelectionDirection = null;
		mFoundCache = null;
		mSelectionSteps = null;
	}

	public boolean selectionChanged(float xPos, float yPos) {
		if (mSelStartPosition == null) {
			int position = pointToPosition((int) xPos, (int) yPos);
			if (position >= 0) {
				View item = getChildAtPosition(position);
				mSelStartRect = getViewRect(item);
				mSelStartPosition = position;
			}
			postInvalidate();
		} else {
			float xDelta = xPos - mSelStartRect.centerX();
			float yDelta = (yPos - mSelStartRect.centerY()) * -1;
			double distance = Math.hypot(xDelta, yDelta);
			Log.d("Angle", "DIST: " + (int) distance + ", MIN: " + mMinDistance);
			if (isInTouchMode() && distance < mMinDistance) {
				return false;
			}

			Direction previousDirection = mSelectionDirection;
			Integer previousSteps = mSelectionSteps;
			mSelectionDirection = Direction.getDirection((float) Math.atan2(yDelta, xDelta));

			float stepSize = mSelectionDirection.isAngle() ? (float) Math.hypot(mColumnWidth, mColumnWidth) : mColumnWidth;
			mSelectionSteps = Math.round((float) distance / stepSize);

			if (mSelectionSteps == 0) {
				mSelectionSteps = null;
			}

			if (mSelectionDirection != previousDirection || mSelectionSteps != previousSteps) {
				List<View> selectedViews = getSelectionViews();
				if (selectedViews == null) {
					return false;
				}

				// Selection no longer includes these characters so
				// restore their default color
				if (mPreviousSelection != null && !mPreviousSelection.isEmpty()) {
					List<View> oldViews = new ArrayList<View>(mPreviousSelection);
					oldViews.removeAll(selectedViews);
					for (View view : oldViews) {
						((TextView) view.findViewById(R.id.lbl_char)).setTextColor(mDefaultColor);
					}
				}

				mPreviousSelection = selectedViews;

				// Selection now includes these characters so
				// highlight them
				if (!selectedViews.isEmpty()) {
					for (View view : selectedViews) {
						((TextView) view.findViewById(R.id.lbl_char)).setTextColor(mSelectionColor);
					}

					View endView = selectedViews.get(selectedViews.size() - 1);
					mSelEndRect = getViewRect(endView);
				}
				postInvalidate();
			}
		}
		return true;
	}

	public void setBoard(char[][] board) {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				View v = getChildAtPosition(i * mColumns + j);
				((TextView) v.findViewById(R.id.lbl_char)).setText("" + board[i][j]);
			}
		}
	}

	/**
	 * @param onWordSelectedListener
	 *            the onWordSelectedListener to set
	 */
	public void setOnWordSelectedListener(OnWordSelectedListener onWordSelectedListener) {
		mOnWordSelectedListener = onWordSelectedListener;
	}

	public void showHint(Word word) {
		mGameState.mHint = word;
		postInvalidate();
	}

	public void wordFound(Word word) {
		if (!mGameState.mFoundWords.contains(word)) {
			mLastWordFound = word.getWord();
			sendAccessibilityEvent(AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
			mGameState.mFoundWords.add(word);
			if (mFoundCache == null) {
				mFoundCache = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			}
			Canvas foundCanvas = new Canvas(mFoundCache);
			drawSelection(word, foundCanvas, mFoundPaint);
			postInvalidate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if (mGameState.mHint != null) {
			drawSelection(mGameState.mHint, canvas, mHintPaint);
		}

		if (mFoundCache == null) {
			mFoundCache = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas foundCanvas = new Canvas(mFoundCache);
			for (Word word : mGameState.mFoundWords) {
				drawSelection(word, foundCanvas, mFoundPaint);
			}
		}

		canvas.drawBitmap(mFoundCache, 0f, 0f, mFoundPaint);

		if (mSelectionDirection != null && mSelectionSteps != null && mSelStartPosition != null) {
			drawCurrentSelection(canvas);
		} else if (!isInTouchMode() && mSelStartPosition != null && mFocusSelected) {
			float pad = mColumnWidth / 3.2f;
			RectF superRect = new RectF(-pad, -pad, pad, pad);
			canvas.save();
			canvas.translate(mSelStartRect.centerX(), mSelStartRect.centerY());
			// canvas.rotate(mSelectionDirection.getAngleDegree());
			canvas.drawRoundRect(superRect, mCornerRadius, mCornerRadius, mPaint);
			canvas.restore();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.GridView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (isInEditMode()) {
			super.onMeasure(heightMeasureSpec, heightMeasureSpec);
			setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
			mColumnWidth = (int) ((float) getMeasuredWidth() / (float) mColumns);
		} else {
			// Display display = ((WindowManager)
			// getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			if (getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().heightPixels) {
				super.onMeasure(heightMeasureSpec, heightMeasureSpec);
				setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
			} else {
				super.onMeasure(widthMeasureSpec, widthMeasureSpec);
				setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
			}
			mColumnWidth = (int) ((float) getMeasuredWidth() / (float) mColumns);
		}

		mCornerRadius = getMeasuredWidth() / 28.0f;

	}

	private void drawCurrentSelection(Canvas canvas) {
		float pad = mColumnWidth / 3.2f;
		RectF superRect = new RectF(-pad, -pad, pad, pad);
		float xDelta = mSelEndRect.centerX() - mSelStartRect.centerX();
		float yDelta = (mSelEndRect.centerY() - mSelStartRect.centerY()) * -1;
		double distance = Math.hypot(xDelta, yDelta);
		superRect.right += distance;

		canvas.save();
		canvas.translate(mSelStartRect.centerX(), mSelStartRect.centerY());
		canvas.rotate(mSelectionDirection.getAngleDegree());
		canvas.drawRoundRect(superRect, mCornerRadius, mCornerRadius, mPaint);
		canvas.restore();
	}

	private void drawSelection(Word word, Canvas canvas, Paint paint) {
		float angleStep = (float) Math.hypot(mColumnWidth, mColumnWidth);
		float pad = mColumnWidth / 3.2f;
		float distance = (word.getDirection().isAngle() ? angleStep :
				mColumnWidth) * (word.getWord().length() - 1);
		RectF superRect = new RectF(-pad, -pad, pad, pad);
		superRect.right += distance;

		View v = getChildAtPosition((word.getRow() * mColumns) + word.getCol());
		Rect viewRect = getViewRect(v);

		canvas.save();
		canvas.translate(viewRect.centerX(), viewRect.centerY());
		canvas.rotate(word.getDirection().getAngleDegree());
		canvas.drawRoundRect(superRect, mCornerRadius, mCornerRadius, paint);
		canvas.restore();
	}

	private int getInverseColor(int color) {
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		int alpha = Color.alpha(color);
		return Color.argb(alpha, 255 - red, 255 - green, 255 - blue);
	}

	private List<View> getSelectionViews() {
		if (mSelStartPosition == null || mSelectionSteps == null || mSelectionDirection == null) {
			return null;
		}

		List<View> views = new ArrayList<View>();
		for (Integer position : getPositionsOnPath(mSelectionDirection, mSelStartPosition, mSelectionSteps)) {
			views.add(getChildAtPosition(position));
		}
		return views;
	}

	private Rect getViewRect(View v) {
		Rect viewRect = new Rect();
		v.getDrawingRect(viewRect);
		viewRect.offset(v.getLeft(), ((ViewGroup) v.getParent()).getTop());
		return viewRect;
	}

	private void init() {
		setWillNotDraw(false);
		setOrientation(LinearLayout.VERTICAL);

		mDefaultColor = getResources().getColorStateList(
				android.R.color.primary_text_dark).getDefaultColor();
		mSelectionColor = getInverseColor(mDefaultColor);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		lp.weight = 1.0f;
		for (int i = 0; i < mColumns; i++) {
			LinearLayout row = new LinearLayout(getContext());
			row.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < mColumns; j++) {
				View view = LayoutInflater.from(getContext()).inflate(R.layout.wordsearch_grid_cell, null);
				view.setFocusable(true);
				view.setOnFocusChangeListener(new OnFocusChangeListener() {
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus && mFocusSelected) {
							Rect rect = getViewRect(v);
							selectionChanged(rect.centerX(), rect.centerY());
						}

						if (mFocusSelected) {
							List<View> selectedViews = getSelectionViews();
							if (selectedViews == null) {
								return;
							}

							for (View view : selectedViews) {
								if (view.equals(v)) {
									return;
								}
							}
						}

						((TextView) v.findViewById(R.id.lbl_char)).setTextColor(hasFocus ? 0xFF00870a : mDefaultColor);
					}
				});
				view.setOnKeyListener(this);
				row.addView(view, lp);
			}
			addView(row, lp);
		}

		setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent e) {
				// Log.d("Stuff", "TOUCH DETECTED");

				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:

					break;
				case MotionEvent.ACTION_MOVE:
					selectionChanged(e.getX(), e.getY());

					break;
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					clearSelection();
					break;
				}
				return true;
			}
		});
		mGameState = new WordsearchGameState();
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setARGB(0xFF, 0x33, 0x99, 0x33);
		mFoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mFoundPaint.setARGB(0xA0, 0x47, 0x94, 0x47);
		mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mHintPaint.setARGB(0xA0, 0x94, 0x47, 0x47);

	}

	private boolean isPointInsideView(float x, float y, View view) {
		Rect rect = new Rect();
		view.getDrawingRect(rect);
		rect.offset(view.getLeft(), ((ViewGroup) view.getParent()).getTop());
		return rect.contains((int) x, (int) y);
	}

	private int pointToPosition(float x, float y) {
		for (int i = 0; i < mColumns * mColumns; i++) {
			if (isPointInsideView(x, y, getChildAtPosition(i))) {
				return i;
			}
		}
		return -1;
	}

	public static interface OnWordSelectedListener {
		public void onWordSelected(List<Integer> positions);
	}

	private static final class WordsearchGameState extends BaseSavedState {

		private Set<Word> mFoundWords;
		private Word mHint;

		@SuppressWarnings("unused")
		public static final Parcelable.Creator<WordsearchGameState> CREATOR =
				new Parcelable.Creator<WordsearchGameState>() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * android.os.Parcelable.Creator#createFromParcel(android
					 * .os.Parcel)
					 */
					public WordsearchGameState createFromParcel(Parcel source) {
						return new WordsearchGameState(source);
					}/*
					 * (non-Javadoc)
					 * 
					 * @see android.os.Parcelable.Creator#newArray(int)
					 */

					public WordsearchGameState[] newArray(int size) {
						return new WordsearchGameState[size];
					}
				};

		private WordsearchGameState() {
			super(Parcel.obtain());
			mFoundWords = new HashSet<WordsearchGridFragment.Word>();
		}

		private WordsearchGameState(Parcel in) {
			super(in);
			mFoundWords = new HashSet<WordsearchGridFragment.Word>();
			Parcelable[] parcels = in.readParcelableArray(Word.class.getClassLoader());
			for (Parcelable parcel : parcels) {
				mFoundWords.add((Word) parcel);
			}
			mHint = in.readParcelable(Word.class.getClassLoader());
		}

		private WordsearchGameState(Parcelable parcelable) {
			super(parcelable);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
		 */
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeParcelableArray(mFoundWords.toArray(new WordsearchGridFragment.Word[] {}), flags);
			dest.writeParcelable(mHint, flags);
		}

	}

}
