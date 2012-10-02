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
package org.seasar.fisshplate.core.element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.util.StringUtil;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * 関数、計算式のクラスです。
 *
 * @author happy_ryo
 */
public class Formula extends AbstractCell {

	/**
	 * コンストラクタです。
	 *
	 * @param cell テンプレート側のセル
	 */
	public Formula(CellWrapper cell) {
		super(cell);
	}

	private static final Pattern patternFormula = Pattern.compile("^\\s*\\#formula\\((.*)\\)");

	/*
	 * (non-Javadoc)
	 *
	 * @see org.seasar.fisshplate.core.TemplateElement#merge(org.seasar.fisshplate.context.FPContext)
	 */
	void mergeImpl(FPContext context, HSSFCell out) throws FPMergeException {
		String value = super.getCellValue().toString();
		Matcher matcher = patternFormula.matcher(value);
		matcher.find();
		String formula = matcher.group(1);
		if (isWritePicture(formula)) {
			out.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			out.setCellFormula(formula);
		}
	}

	private boolean isWritePicture(String formula) {
		if (StringUtil.isEmpty(formula)) {
			return false;
		}
		return true;
	}

}