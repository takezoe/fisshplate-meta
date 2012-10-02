package org.seasar.fisshplate.core.parser;

import org.seasar.fisshplate.core.element.AbstractCell;
import org.seasar.fisshplate.core.element.Meta;
import org.seasar.fisshplate.wrapper.CellWrapper;

/**
 * Fisshplateで #meta タグを検出するためのパーサです。
 *
 * @author takezoe
 */
public class MetaParser implements CellParser {

	private static ThreadLocal<Boolean> existsMeta = new ThreadLocal<Boolean>();

	/**
	 * Fisshplateの処理を開始する前に呼び出します。
	 */
	public static void init(){
		existsMeta.set(new Boolean(false));
	}

	/**
	 * テンプレート内に #meta の記述があった場合は true を返します。
	 *
	 * @return テンプレート内に #meta の記述があったかどうか
	 */
	public static boolean existsMeta(){
		Boolean result = existsMeta.get();
		return result.booleanValue();
	}

	/**
	 * Fisshplateの処理が完了したあとに呼び出します。
	 */
	public static void end(){
		existsMeta.remove();
	}

	public AbstractCell getElement(CellWrapper cell, String value) {
		AbstractCell cellElement = null;
		if(value.startsWith("#meta")){
			existsMeta.set(new Boolean(true));
			return new Meta(cell);
		}
		return cellElement;
	}

}
