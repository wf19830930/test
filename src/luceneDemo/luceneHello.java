package luceneDemo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.org.apache.bcel.internal.generic.NEW;

class luceneHello {
	/*
	 * 创建索引
	 */

	public void Index() {
	
		 Directory dir=null;
		// 0.创建indexwriter
		IndexWriter writer = null;
		
		try {
			// 1.创建Directory
			//Directory dir = new RAMDirectory();
			 // 指定索引库的存放路径，需要在系统中首先进行索引库的创建
	        // 指定索引库存放路径
	        //File indexrepository_file = new File("/Users/wangfeng/Desktop/test/aaa");
	        //Path path = indexrepository_file.toPath();
			//Directory dir =FSDirectory.open(Paths.get("/Users/wangfeng/Desktop/test/aaa"));
			
	        dir =FSDirectory.open(new File("/Users/wangfeng/Desktop/test/ccc").toPath());
			//2.创建分词工具
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(dir, iwc);
			// 3.创建Document对象
			Document doc=null;
			
			// 4.创建field
			File f=new File("/Users/wangfeng/Desktop/test/bbb");
			
			for(File file:f.listFiles()){
				System.out.println(file.getPath());
				
				FieldType fType=new FieldType();
				//fType.
				doc=new Document();
				//doc.add(new Field("content", new FileReader(file),new FieldType()));
				//doc.add(new Field("filename",file.getName(),Store.YES,Field.Index.ANALYZED_NO_NORMS));
				//doc.add(new Field("path",file.getAbsolutePath(),Store.YES,Field.Index.ANALYZED_NO_NORMS));
				
				doc.add(new TextField("contents", new FileReader(file)));  
				doc.add(new StringField("filename", file.toString(), Field.Store.YES)); 
				doc.add(new StringField("path", file.toString(), Field.Store.YES)); 
				// 5.indexwriter添加到索引
				writer.addDocument(doc);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				writer.close();
				dir.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	public void Searcher(String keyword){
		
		 
		
		  try {
		 
			//1.创建Directory
			Directory dir =FSDirectory.open(new File("/Users/wangfeng/Desktop/test/ccc").toPath());
			
			//2.创建创建IndexReader
			IndexReader reader=DirectoryReader.open(dir);
					
			//3.根据IndexReader创建IndexSearcher
			IndexSearcher searcher=new IndexSearcher(reader);
			//4.创建搜索的Query
			//创建QueryParser来确定文件搜索的内容
			QueryParser parser = new QueryParser("contents",  new StandardAnalyzer());
			Query query=null;
			  
			
			//创建query表示搜索content中的包含keyword的文档
			query= parser.parse(keyword);
			//5.根据searcher搜索并返回前10个TopDocs 
			TopDocs tdocs=searcher.search(query, 10);
			//6.根据TopDocs返回ScoreDoc数组
			ScoreDoc[] sDoc=tdocs.scoreDocs;
			  System.out.print(sDoc.length);
			 for (ScoreDoc scoreDoc : sDoc) {
		            System.out.println("匹配得分：" + scoreDoc.score);
		            System.out.println("文档索引ID：" + scoreDoc.doc);
		          //7.根据searcher和ScoreDoc对象获取具体的Document对象
		            Document document = searcher.doc(scoreDoc.doc);
		          //8.根据Document对象获取需要的值
		            System.out.println("名称："+document.get("filename")+"路径："+document.get("path"));
		        }
			 
			 reader.close();
			 dir.close();
			
		} catch (IOException e) {
			 
			e.printStackTrace();
		} catch (ParseException e) {
			 
			e.printStackTrace();
		}
		
	}
}
