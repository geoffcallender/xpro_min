package com.goxpro.xpro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean isEmpty(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	/**
	 * This is lenient in that it treats a null string as equal to a string of length 0 after it's been trimmed.
	 */
	public static boolean isSameLenient(String s0, String s1) {
		return (isEmpty(s0) && isEmpty(s1) || isNotEmpty(s0) && isNotEmpty(s1) && s0.equals(s1));
	}

	/**
	 * This is lenient in that it treats a null string as equal to a string of length 0 after it's been trimmed.
	 */
	public static boolean isNotSameLenient(String s0, String s1) {
		return !isSameLenient(s0, s1);
	}

	public static String deNull(String s) {
		return s == null ? "" : s;
	}

	public static String abbreviate(String s, int maxLen) {
		if (s == null) {
			return null;
		}
		else {
			return s.length() > maxLen ? (s.substring(0, maxLen) + "...") : s.substring(0, s.length());
		}
	}

	public static int countOccurences(String searchFor, String in) {
		int count = 0;
		int len = searchFor.length();

		if (len > 0) { // search only if there is something
			int start = in.indexOf(searchFor);
			while (start != -1) {
				count++;
				start = in.indexOf(searchFor, start + len);
			}
		}
		return count;
	}

	/**
	 * Scan a string for the first occurrence of some regex Pattern. From
	 * http://mindprod.com/jgloss/regex.html#MATCHINGANDFINDING
	 * 
	 * @param lookFor the pattern to look for
	 * @param lookIn the String to scan.
	 * 
	 * @return offset relative to start of lookIn where it first found the pattern, -1 if not found.
	 */
	public static int regexIndexOf(String lookIn, Pattern lookFor) {
		Matcher m = lookFor.matcher(lookIn);
		if (m.find()) {
			return m.start();
		}
		else {
			return -1;
		}
	}

	public static String append(String s1, String s2, String divider) {
		if (s1 == null || s1.isEmpty()) {
			return s2;
		}
		else {
			return s1 + divider + s2;
		}
	}

	/****************************************************************************************
	 * The following methods are from http://www.java2s.com/Code/Java/Data-Type/Padstring.htm TODO - Is there a
	 * licensing issue? Surely not?
	 ****************************************************************************************/

	/**
	 * Pad the beginning of the given String with spaces until the String is of the given length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String prepad(String s, int length) {
		return prepad(s, length, ' ');
	}

	/**
	 * Pre-pend the given character to the String until the result is the desired length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @param c padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String prepad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		char padding[] = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuffer sb = new StringBuffer(length);
		sb.append(padding);
		sb.append(s);
		return sb.toString();
	}

	/**
	 * Pad the end of the given String with spaces until the String is of the given length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String postpad(String s, int length) {
		return postpad(s, length, ' ');
	}

	/**
	 * Append the given character to the String until the result is the desired length.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @param c padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String postpad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		char padding[] = new char[needed];
		java.util.Arrays.fill(padding, c);
		StringBuffer sb = new StringBuffer(length);
		sb.append(s);
		sb.append(padding);
		return sb.toString();
	}

	/**
	 * Pad the beginning and end of the given String with spaces until the String is of the given length. The result is
	 * that the original String is centered in the middle of the new string.
	 * <p>
	 * If the number of characters to pad is even, then the padding will be split evenly between the beginning and end,
	 * otherwise, the extra character will be added to the end.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String midpad(String s, int length) {
		return midpad(s, length, ' ');
	}

	/**
	 * Pad the beginning and end of the given String with the given character until the result is the desired length.
	 * The result is that the original String is centered in the middle of the new string.
	 * <p>
	 * If the number of characters to pad is even, then the padding will be split evenly between the beginning and end,
	 * otherwise, the extra character will be added to the end.
	 * <p>
	 * If a String is longer than the desired length, it will not be truncated, however no padding will be added.
	 * 
	 * @param s String to be padded.
	 * @param length desired length of result.
	 * @param c padding character.
	 * @return padded String.
	 * @throws NullPointerException if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String midpad(String s, int length, char c) {
		int needed = length - s.length();
		if (needed <= 0) {
			return s;
		}
		int beginning = needed / 2;
		int end = beginning + needed % 2;
		char prepadding[] = new char[beginning];
		java.util.Arrays.fill(prepadding, c);
		char postpadding[] = new char[end];
		java.util.Arrays.fill(postpadding, c);
		StringBuffer sb = new StringBuffer(length);
		sb.append(prepadding);
		sb.append(s);
		sb.append(postpadding);
		return sb.toString();
	}
}
