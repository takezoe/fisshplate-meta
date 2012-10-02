package org.seasar.fisshplate.core.parser;

import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Merge;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * Fisshplateで #merge タグを検出するためのパーサです。
 *
 * @author takezoe
 */
public class MergeParser implements CellParser {
	
	public AbstractCell getElement(CellWrapper cell, String value) {
		AbstractCell cellElement = null;
		if(value.startsWith("#merge")){
			return new Merge(cell);
		}
		return cellElement;
	}
	
}
