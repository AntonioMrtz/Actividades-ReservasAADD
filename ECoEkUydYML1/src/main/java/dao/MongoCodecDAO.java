package dao;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public abstract class MongoCodecDAO<T> {

    protected static MongoClient mongoClient;
    protected static MongoDatabase db;
    protected MongoCollection<T> collection;

    protected CodecRegistry defaultCodecRegistry;
    public MongoCodecDAO() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");     
        db = mongoClient.getDatabase("actividades-reservas-aadd");
        
        defaultCodecRegistry =CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        
        
            
        createCollection();
    }

    public abstract void createCollection();
}
