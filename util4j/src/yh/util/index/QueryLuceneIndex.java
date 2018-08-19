package yh.util.index;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

public class QueryLuceneIndex {
	public static void main(String arg[]) throws Exception {
		Analyzer analyzer = new StandardAnalyzer();
		IndexSearcher search = new IndexSearcher("d:\\index");
		String queryString = "\"G705 JALOU\"~8";
		Query query = null;
		Hits hits = null;
		try {
			// 在lucene中有一些字符是被保留来做特殊应用的,它们是:
			// + - && || ! ( ) { } [ ] ^ " ~ * ? : \
			// 但你在查询字符串中需要包含其中的字符的时候,一定要记得在前面加上字符"\"表示转义.
			// 例如要查询:(1+1):2
			// 必须写成:\(1\+1\)\:2

			// TermQuery skeyWordQuery = new TermQuery(new Term(name, nameValue));//完全匹配查询
			// BooleanQuery bquery = new BooleanQuery(); //多条件查询

			QueryParser qp = new QueryParser("mt", analyzer); // 模糊匹配分词查询
			query = qp.parse(queryString);
		} catch (ParseException e) {
		}
		if (search != null) {
			// 按序号排序
			SortField SortField1 = new SortField("id", SortField.INT, true); // 中间参数表示排序字段类型，true 表示降序 false 升序
			SortField[] sortField_arr = new SortField[] { SortField1 };
			Sort sort = new Sort(sortField_arr);
			hits = search.search(query, sort);
			if (hits.length() > 0) {
				for (int i = 0; i < hits.length(); i++) {
					Document doc = hits.doc(i);
					System.out.println("搜索到的结果1: " + doc.get("mb"));
					System.out.println("搜索到的结果2:" + doc.get("mt"));
					System.out.println("搜索到的结果3:" + doc.get("id"));
				}

				System.out.println("找到:" + hits.length() + " 个结果!");
			} else
				System.out.println("结果为0条");
		}

		// 索引中删除文档
		// QueryLuceneIndex queryIndex = new QueryLuceneIndex();
		// queryIndex.deleteLuceneIndex("indexId", "1");

		// 修改索引中的文档
		// 针对lucene的索引中文档内容进行修改操作,可以通过先删除后添加实现

	}

	/**
	 * 删除索引文件中的数据
	 */
	public void deleteLuceneIndex(String deFieldName, String deFieldNameValue)
			throws IOException {
		File file = new File("f:\\index");
		/**
		 * 从索引中删除文档 类IndexReader负责从一个已经存在的索引中删除文档
		 * */
		IndexReader indexReader = IndexReader.open(file);
		indexReader.deleteDocuments(new Term(deFieldName, deFieldNameValue));
		indexReader.close();
	}

}
