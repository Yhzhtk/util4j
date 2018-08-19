package yh.util.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class MergeIndex {

	public static void main(String[] args) {

		if (args.length != 2) {
			System.out.println("参数错误，共两个参数，第一个表示合并的目标路径，第二个是需要合并的数据的目录！");
			System.exit(0);
		}

		String indexPath = args[0];// "D:\\_gameIndex\\gameindex1";
		String dataDir = args[1];// "D:\\_gameIndex\\gameindex2";

		mergeIndex(indexPath, dataDir, new StandardAnalyzer());
	}

	/**
	 * 合并索引
	 * 
	 * @param indexPath
	 *            合并目标位置
	 * @param dataDir
	 *            需要合并的数据
	 * @param sa
	 */
	public static void mergeIndex(String indexPath, String dataDir,
			StandardAnalyzer sa) {
		System.out.println("合并索引，将" + dataDir + "的索引合并到" + indexPath + "中……");

		IndexWriter indexWriter = null;
		try {
			System.out.println("初始化数据，设置索引最大长度等…… ");
			indexWriter = new IndexWriter(indexPath, sa, false);
			indexWriter.setMergeFactor(100000);
			indexWriter.setMaxFieldLength(Integer.MAX_VALUE);
			indexWriter.setMaxBufferedDocs(Integer.MAX_VALUE);
			indexWriter.setMaxMergeDocs(Integer.MAX_VALUE);
			FSDirectory[] fs = { FSDirectory.getDirectory(dataDir) };
			System.out.println("最大长度设置为整型最大值：" + Integer.MAX_VALUE);
			System.out.println("合并索引开始，请等待…… ");
			indexWriter.addIndexes(fs);
			indexWriter.optimize();
			indexWriter.close();
			System.out.println("完成合并!合并后目录为" + indexPath + "\t ");
		} catch (Exception e) {
			System.out.println("合并索引出错！");
			e.printStackTrace();
		} finally {
			try {
				if (indexWriter != null)
					indexWriter.close();
			} catch (Exception e) {
			}
		}
	}

	public static void RamMergeIndex(String indexPath, String dataDir,
			StandardAnalyzer sa) {
		IndexWriter fswriter = null; // FS
		IndexWriter ramwriter = null; // RAM

		try {

			Directory fsDir = FSDirectory.getDirectory(indexPath);
			Directory ramDir = new RAMDirectory();

			// 打开已经存在的索引文件 最后一项必须为false 若为true则会删除已经存在的索引 这一步很重要
			System.out.println("*********now open exist index :" + indexPath
					+ "**********");
			fswriter = new IndexWriter(fsDir, sa, false);
			// fswriter.setUseCompoundFile(false); //设置复合索引false

			ramwriter = new IndexWriter(ramDir, sa, true); // 构建给予RAM的索引
			System.out.println("CompoundFile()= "
					+ fswriter.getUseCompoundFile());

			// 对dataDir目录下的所有文件建立RAM索引
			ramwriter.optimize();// 内存索引优化
			ramwriter.close(); // 关闭内存索引

			System.out.println("...begin combin index.....");
			fswriter.addIndexes(new Directory[] { ramDir }); // 索引合并
			fswriter.close(); // 关闭文件索引
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
