package org.seasar.fisshplate.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.fisshplate.core.parser.CellParser;
import org.seasar.fisshplate.core.parser.FormulaParser;
import org.seasar.fisshplate.core.parser.MergeParser;
import org.seasar.fisshplate.core.parser.MetaParser;
import org.seasar.fisshplate.core.parser.handler.CellParserHandler;
import org.seasar.fisshplate.exception.FPMergeException;
import org.seasar.fisshplate.exception.FPParseException;
import org.seasar.fisshplate.template.FPTemplate;

/**
 * Fisshplateを呼び出すためのユーティリティです。
 *
 * @author Naoki Takezoe
 */
public class FisshplateUtil {

	static {
		// CellParserをリフレクションで追加
		CellParser[] parsers = getStaticField(CellParserHandler.class, "builtInCellParser");
		List<CellParser> newParsers = new ArrayList<CellParser>();
		for(CellParser p: parsers){
			newParsers.add(p);
		}
		newParsers.add(new FormulaParser());
		newParsers.add(new MetaParser());
		newParsers.add(new MergeParser());

		setStaticField(CellParserHandler.class, "builtInCellParser",
				newParsers.toArray(new CellParser[newParsers.size()]));
	}

	/**
	 * ストリームをクローズするためのユーティリティメソッドです。
	 */
	private static void closeStream(Closeable closeable){
		if(closeable != null){
			try {
				closeable.close();
			} catch(Throwable t){
				;
			}
		}
	}

	/**
	 * リフレクションでstaticフィールドの値を取得するためのユーティリティメソッドです。
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getStaticField(Class<?> clazz, String name){
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return (T) field.get(null);
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * リフレクションでstaticフィールドの値を設定するためのユーティリティメソッドです。
	 */
	private static void setStaticField(Class<?> clazz, String name, Object value){
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			field.set(null, value);
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Fisshplateで #meta を含んだメタテンプレートを処理し、バイト配列で取得します。
	 *
	 * @param is Excelテンプレートの入力ストリーム
	 * @param data テンプレートにマージするデータ
	 * @return Excelテンプレートにデータをマージした結果
	 * @throws FPParseException
	 * @throws FPMergeException
	 * @throws IOException
	 */
	public static byte[] process(InputStream is, Map<String, Object> data)
		throws FPParseException, FPMergeException, IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			process(is, data, out);

			byte[] buf = out.toByteArray();
			return buf;

		} finally {
			closeStream(out);
		}
	}

	/**
	 * Fisshplateで #meta を含んだメタテンプレートを処理し、出力ストリームに書き出します。
	 *
	 * @param is Excelテンプレートの入力ストリーム
	 * @param data テンプレートにマージするデータ
	 * @param out Excelテンプレートにデータをマージした結果を書き込む出力ストリーム
	 * @throws FPParseException
	 * @throws FPMergeException
	 * @throws IOException
	 */
	public static void process(InputStream is, Map<String, Object> data, OutputStream out)
			throws FPParseException, FPMergeException, IOException {

		FPTemplate template = new FPTemplate();

		MetaParser.init();
		try {
			HSSFWorkbook wb = template.process(is, data);

			// #metaタグが使われていたら二回目の処理を実行
			if(MetaParser.existsMeta()){
				ByteArrayOutputStream byteOut = null;
				ByteArrayInputStream byteIn = null;

				try {
					byteOut = new ByteArrayOutputStream();
					wb.write(byteOut);
					byte[] buf = byteOut.toByteArray();

					byteIn = new ByteArrayInputStream(buf);
					wb = template.process(byteIn, data);

				} finally {
					closeStream(byteOut);
					closeStream(byteIn);
				}
			}

			wb.write(out);

		} finally {
			MetaParser.end();
		}
	}


}
