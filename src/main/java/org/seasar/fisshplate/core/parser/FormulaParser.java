/*
 *  Copyright 2009 happy_ryo.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.seasar.fisshplate.core.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Formula;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * 関数や計算式を埋め込む為のパーサです。
 *
 * @author happy_ryo
 */
public class FormulaParser implements CellParser {

	private static final Pattern patternFormula = Pattern.compile("^\\s*\\#formula\\((.*)\\)");

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.seasar.fisshplate.core.parser.CellParser#getElement(org.seasar.fisshplate
	 * .wrapper.CellWrapper, java.lang.String)
	 */
	public AbstractCell getElement(CellWrapper cell, String value) {
		AbstractCell cellElement = null;
		Matcher matcher = patternFormula.matcher(value);
		if (matcher.find()) {
			cellElement = new Formula(cell);
		}
		return cellElement;
	}

}
