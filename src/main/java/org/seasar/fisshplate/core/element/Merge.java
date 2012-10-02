package org.seasar.fisshplate.core.element;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.seasar.fisshplate.context.FPContext;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * #merge に対応するクラスです。
 * <p>
 * 水平方向に同じ値が続いていた場合にセルをマージします。
 * 
 * @author takezoe
 */
public class Merge extends AbstractCell {

	public Merge(CellWrapper cell) {
		super(cell);
	}

	@Override
	void mergeImpl(FPContext context, HSSFCell out) throws FPMergeException {
		String value = super.getCellValue().toString();
		value = value.replaceFirst("#merge\\s*", "");
		out.setCellValue(new HSSFRichTextString(value));
		
		HSSFSheet sheet = context.getOutSheet();
		
		int columnIndex = out.getColumnIndex();
		int rowIndex = out.getRowIndex();
		
		HSSFRow row = context.getCurrentRow();
		HSSFCell cell = row.getCell(columnIndex - 1);
		if(cell != null){
			String cellValue = cell.getStringCellValue();
			if(value.equals(cellValue)){
				sheet.addMergedRegion(new CellRangeAddress(
						rowIndex, rowIndex, columnIndex - 1, columnIndex));
			}
		}
	}

}
