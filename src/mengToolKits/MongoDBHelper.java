package mengToolKits;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

//�����õĹ����ࣺ
public class MongoDBHelper {
	
	static MongoClient tClient=null;
	static MongoCollection<Document> aColl = null;
	static Document editDoc=null;
	
	public static void main(String [] args){
		
		transCollection();
		
	}
	public static void connectDB(){
		
		tClient = MongoDBHelper.getMongoDbClient("localhost", 27017, "root", "root", "admin");
		aColl = MongoDBHelper.getMongoDbCollection(tClient, "LAW","Vocabulary");	
	}
	
	// ��һ���⵼����һ����
	public static void transCollection(){
		//source
		MongoClient clientA =getMongoDbClient("localhost", 27017, "root", "root", "admin");
		MongoCollection<Document> aColl = getMongoDbCollection(clientA, "LAW","Voc");
		//target
		MongoClient clientB = getMongoDbClient("10.167.174.162", 27017, "frdc_rw", "frdc_rw_123", "admin");
		MongoCollection<Document> bColl = getMongoDbCollection(clientB, "changshi_data","Voc");
		
		Document sObj=null;
		FindIterable<Document> findIterable = aColl.find();	//ֱ��find() ��ȫ������
		FindIterable<Document> tIter=null;
		MongoCursor<Document> mongoCursor = findIterable.noCursorTimeout(true).iterator();
		
		int count=0;
		while (mongoCursor.hasNext()) {
			sObj=mongoCursor.next();
			
			tIter = bColl.find(Filters.eq("_id", sObj.getString("_id")));	
			if(tIter.iterator().hasNext()){
				System.out.println("DUP: "+sObj.toString());
				continue;
			}
			bColl.insertOne(sObj);

			count++;
		}
		System.out.println(count);
		clientA.close();
		clientB.close();
	}
	public static void updateOne(){
		
		Document tDoc=new Document();
		tDoc.put("fieldname", "�����ᡢ�����ʺͶ���");
		aColl.updateMany(Filters.eq("fieldname","10000403"), new Document("$set",tDoc));

	}
	public static void updateAll(){
		
		FindIterable<Document> findIt = aColl.find();	//ֱ��find() ��ȫ������
		MongoCursor<Document> cursor = findIt.noCursorTimeout(true).iterator();
		int count=0;
		while (cursor.hasNext()) {
			editDoc=cursor.next();
			if(editDoc.getString("supVoc")!=null){			
				editDoc.put("supContent", editDoc.getString("supVoc"));
				editDoc.remove("supVoc");
				aColl.replaceOne(Filters.eq("_id",editDoc.getString("_id")), editDoc);
			}
		}
	}
	public static MongoClient getMongoDbClient(String ipAddr, int portNum, String userName, String passWord, String dbName) {
		MongoCredential credential = MongoCredential.createScramSha1Credential(userName, dbName, passWord.toCharArray());
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(credential);
		ServerAddress serverAddress = new ServerAddress(ipAddr, portNum);
		ArrayList<ServerAddress> addrs = new ArrayList<ServerAddress>();
		addrs.add(serverAddress);
		MongoClient mongoClient = new MongoClient(addrs, credentials);
		return mongoClient; // ����collection����
	}
	
	public static MongoCollection<Document> getMongoDbCollection(MongoClient mongoClient, String dbName, String collectionName) {
	
		MongoDatabase db = mongoClient.getDatabase(dbName);
		MongoCollection<Document> collection = db.getCollection(collectionName);
		return collection;
	}
	public static void closeDB(){
		tClient.close();
	}
	//���ֲ�����
	public static void iterateDB(){
		
		FindIterable<Document> findIt = aColl.find(Filters.eq("type","big"));	//ֱ��find() ��ȫ������
		MongoCursor<Document> cursor = findIt.noCursorTimeout(true).iterator();
		
		int count=0;
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
			count++;
			if(count>1){
				break;
			}
		}
	}
	public static void staticDB(){
		
		FindIterable<Document> findIt = aColl.find();	//ֱ��find() ��ȫ������
		MongoCursor<Document> cursor = findIt.noCursorTimeout(true).iterator();
		Set<String> allKeys=null;
		Map<String, Integer> keyFeq=new HashMap<String,Integer>();
		int count=0;
		int tInt;
		while (cursor.hasNext()) {
			editDoc=cursor.next();
			allKeys=editDoc.keySet();
			for(String k:allKeys){
				if(keyFeq.get(k)==null){
					keyFeq.put(k,1);
				}else{
					tInt=keyFeq.get(k);
					keyFeq.put(k,keyFeq.get(k)+1);

				}			
			}
			/*count++;
			if(count>1){
				break;
			}*/
	   }
	   keyFeq.forEach((k,v)->
	    System.out.println(k+"\t"+v));
	}
	//��ȡ
	public static void accessDataFromMongo() {
		// insert
		
		Document oneDoc=new Document();
		ArrayList<String> temList=new ArrayList<String>();
		List<String> list = Arrays.asList("o1","o2");
		temList.addAll(list);
		
		oneDoc.append("_id", 43).append("author", "MY").append("add",temList);
		aColl.insertOne(oneDoc);
		
		// update 
		UpdateResult temResult=aColl.updateMany(Filters.eq("author", "MY"), new Document("$set",new Document("author","Mengyao")));  
		System.out.println(temResult.getMatchedCount());
		
		//delete
		Document condition=new Document();
		condition.append("_id", 41);
		System.out.printf("deleteCount=%d",aColl.deleteMany(condition).getDeletedCount());
		
		//replace
		oneDoc.clear();
		oneDoc.append("mail", "mengyao@cn.fuj");
		temResult=aColl.replaceOne(Filters.eq("_id",40), oneDoc);
		System.out.printf("replace Count=%d",temResult.getMatchedCount());

	}	

}
