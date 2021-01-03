/*
 *	Copyright 2021 Cufy
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package lsafer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A sudoku solver. Give it the grid you want to solve and it will solve it.
 *
 * @author LSafer
 * @version 0.0.0
 * @since 0.0.0 ~2021.01.03
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClassWithoutPrivateConstructor", "NonFinalUtilityClass", "UtilityClass"})
public class SudokuSolver {
	/**
	 * An array of valid sudoku puzzle grids.
	 *
	 * @since 0.0.0 ~2021.01.3
	 */
	private static final int[][][] SAMPLES;

	static {
		SAMPLES = new int[][][]{
				{
						{5, 3, 0, 0, 7, 0, 0, 0, 0},
						{6, 0, 0, 1, 9, 5, 0, 0, 0},
						{0, 9, 8, 0, 0, 0, 0, 6, 0},
						{8, 0, 0, 0, 6, 0, 0, 0, 3},
						{4, 0, 0, 8, 0, 3, 0, 0, 1},
						{7, 0, 0, 0, 2, 0, 0, 0, 6},
						{0, 6, 0, 0, 0, 0, 2, 8, 0},
						{0, 0, 0, 4, 1, 9, 0, 0, 5},
						{0, 0, 0, 0, 8, 0, 0, 7, 9}
				}
		};
	}

	@SuppressWarnings("JavaDoc")
	public static void main(String[] args) {
		int[][] g = SudokuSolver.SAMPLES[0];
		System.out.println("The original grid:");
		System.out.println(Grid.format(g));

		int i = 1;
		for (int[][] s : Computerphile.solve(g)) {
			System.out.println();
			System.out.println("Solution number " + i++ + " :");
			System.out.println(Grid.format(s));
		}
	}

	/**
	 * The computerphile solution.
	 *
	 * @author Computerphile, LSafer
	 * @version 0.0.0
	 * @since 0.0.0 ~2021.01.3
	 */
	public static class Computerphile {
		/**
		 * Return true, if the given {@code n} number is possible at the position {@code [y][x]} at the given {@code grid}.
		 *
		 * @param grid the grid to check.
		 * @param y    the position of the first dimension {@code []} to be checked.
		 * @param x    the position of the second dimension {@code [][]} to be checked.
		 * @param n    the number to be checked if it is possible at the provided position.
		 * @return true, if the given number can be placed at the given position in the given grid.
		 * @throws NullPointerException      if the given {@code grid} is null. Or if {@code grid[0-8]} is null.
		 * @throws IndexOutOfBoundsException if the given {@code grid} has a lower bounds of {@code [9][9]}.
		 * @since 0.0.0 ~2021.01.3
		 */
		public static boolean possible(int[][] grid, int y, int x, int n) {
			for (int i = 0; i < 9; i++)
				if (grid[y][i] == n)
					return false;

			for (int i = 0; i < 9; i++)
				if (grid[i][x] == n)
					return false;

			int x0 = (x / 3) * 3;
			int y0 = (y / 3) * 3;
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (grid[y0 + i][x0 + j] == n)
						return false;

			return true;
		}

		/**
		 * Attempts the {@code Computerphile} approach of the solution. For more details, visit their {@code Youtube} Channel. Note: it is required
		 * for this function to work properly, to not store any number out of the range (0-9) inclusive.
		 *
		 * @param grid the grid to be solved.
		 * @return a copies of that grid on its solved state. (all the possible solutions)
		 * @throws NullPointerException      if the given {@code grid} is null. Or if {@code grid[0-8]} is null.
		 * @throws IndexOutOfBoundsException if the given {@code grid} has a lower bounds of {@code [9][9]}.
		 * @since 0.0.0 ~2021.01.3
		 */
		public static int[][][] solve(int[][] grid) {
			Objects.requireNonNull(grid, "grid");
			List<int[][]> solutions = new ArrayList<>();
			Computerphile.solve(grid, a -> solutions.add(Grid.copy(a)));
			return solutions.toArray(Grid.EMPTY_ARRAY);
		}

		/**
		 * The actual algorithm backing the method {@link #solve(int[][])}.
		 *
		 * @param grid     the grid to be solved.
		 * @param callback a callback to be invoked when the grid got into a solved state.
		 * @throws NullPointerException      if the given {@code grid} or {@code callback} is null. Or if {@code grid[0-8]} is null.
		 * @throws IndexOutOfBoundsException if the given {@code grid} has a lower bounds of {@code [9][9]}.
		 * @since 0.0.0 ~2021.01.3
		 */
		public static void solve(int[][] grid, Consumer<int[][]> callback) {
			for (int y = 0; y < grid.length; y++)
				for (int x = 0; x < grid[y].length; x++)
					if (grid[y][x] == 0) {
						for (int n = 1; n < 10; n++)
							if (Computerphile.possible(grid, y, x, n)) {
								grid[y][x] = n;
								Computerphile.solve(grid, callback);
								grid[y][x] = 0;
							}

						return;
					}

			callback.accept(grid);
		}
	}

	/**
	 * Grid utilities.
	 *
	 * @author LSafer
	 * @version 0.0.0
	 * @since 0.0.0 ~2021.01.3
	 */
	public static class Grid {
		/**
		 * An empty grid.
		 */
		public static final int[][] EMPTY = new int[0][0];
		/**
		 * An empty grid array.
		 */
		public static final int[][][] EMPTY_ARRAY = new int[0][0][0];

		/**
		 * Copies the given {@code s} array.
		 *
		 * @param s the array to be copied.
		 * @return a deep copy of the given {@code s} array.
		 * @throws NullPointerException if the given {@code s} or any of its elements is null.
		 * @since 0.0.0 ~2021.01.3
		 */
		public static int[][] copy(int[][] s) {
			int[][] a = new int[s.length][];
			for (int i = 0; i < s.length; i++)
				a[i] = s[i].clone();
			return a;
		}

		/**
		 * Convert the given sudoku {@code grid} into a readable string.
		 *
		 * @param grid the grid to be formatted.
		 * @return a readable string from formatting the given {@code grid}.
		 * @throws NullPointerException if the given {@code grid} is null. Or if any inner array of it is null.
		 * @since 0.0.0 ~2021.01.3
		 */
		public static String format(int[][] grid) {
			return Arrays.stream(grid)
					.map(row ->
							Arrays.stream(row)
									.mapToObj(Integer::toString)
									.collect(Collectors.joining("\t"))
					)
					.collect(Collectors.joining("\n"));
		}
	}
}
