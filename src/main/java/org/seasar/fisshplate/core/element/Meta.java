package org.seasar.fisshplate.core.element;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * #meta タグに対応するクラスです。
 * <p>
 * FisshplateのテンプレートからさらにFisshplateで処理可能なテンプレートを出力するためのもので、
 * #meta に続けて記述した文字列をそのままセルの値として出力します。
 * また、 #{...} という記述を ${...} に変換します。
 *
 * @author takezoe
 */
public class Meta extends AbstractCell {

	public Meta(CellWrapper cell) {
		super(cell);
	}

	@Override
	void mergeImpl(FPContext context, HSSFCell out) throws FPMergeException {
		String value = super.getCellValue().toString();

		value = value.replaceFirst("#meta\\s*", "");
		value = value.replace("#{", "${");

		out.setCellValue(new HSSFRichTextString(value));
	}

}
