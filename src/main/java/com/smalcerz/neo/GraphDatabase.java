package com.smalcerz.neo;



import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import org.ajbrown.namemachine.Name;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.smalcerz.neo.helpers.Generator;
import com.smalcerz.neo.helpers.GymDescription;



public final class GraphDatabase 
{
    private final GraphDatabaseService graphDatabaseService;
    private enum NodeEnum implements Label{Gym, Person;}


    public static GraphDatabase createDatabase(String graphDirLocation) 
    {
        return new GraphDatabase(graphDirLocation);
    }

    private GraphDatabase(String graphDirLocation) 
    { 
        graphDatabaseService = new GraphDatabaseFactory()
            .newEmbeddedDatabaseBuilder(new File(graphDirLocation))
            .newGraphDatabase();
        registerShutdownHook();
    }

    private void registerShutdownHook() 
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() 
        {
            @Override
            public void run() 
            {
                graphDatabaseService.shutdown();
            }
        });
    }

    public long addPerson(String name, int yearOfBirth, int circuitOfBiceps) 
    {
    	try (Transaction t = graphDatabaseService.beginTx()){
	        Node person = graphDatabaseService.createNode(NodeEnum.Person);
	        person.setProperty("name", name);
	        person.setProperty("born", yearOfBirth);
	        person.setProperty("biceps", circuitOfBiceps);
	        
	        t.success();
	        return person.getId();
        }
        
        
    }
    
    public long addGym(String name, String tagline) 
    { 
        try (Transaction t = graphDatabaseService.beginTx()){
	        Node gym = graphDatabaseService.createNode(NodeEnum.Gym);
	        gym.setProperty("name", name);
	        gym.setProperty("tagline", tagline);
	        t.success();
	        return gym.getId();
        }
    }
    
    public long addFIRST_TRAINING_IN(long personID, long gymID, int year) 
    {
    	try (Transaction t = graphDatabaseService.beginTx()){
	        Node person = graphDatabaseService.getNodeById(personID);
	        Node gym = graphDatabaseService.getNodeById(gymID);
	        Relationship r = person.createRelationshipTo(gym, RelationshipType.withName("FIRST_TRAINING_IN"));
	        r.setProperty("year", year);
	        t.success();
	        return r.getId();
    	}
        
    }
    
    public long addTRAINING_IN(long personID, long gymID, int since) 
    {
    	try (Transaction t = graphDatabaseService.beginTx()){
	        Node person = graphDatabaseService.getNodeById(personID);
	    	Node gym = graphDatabaseService.getNodeById(gymID);
	        Relationship r = person.createRelationshipTo(gym, RelationshipType.withName("TRAINING_IN"));
	        r.setProperty("since", since);
	        t.success();
	        return r.getId();
    	}
    }
    
    public long add_VISITED(long personID, long gymID) 
    {
    	try (Transaction t = graphDatabaseService.beginTx()){
	        Node person = graphDatabaseService.getNodeById(personID);
	    	Node gym = graphDatabaseService.getNodeById(gymID);
	        Relationship r = person.createRelationshipTo(gym, RelationshipType.withName("VISITED"));
	        t.success();
	        return r.getId();
    	}
    	
    	
        
    }
    
    public void generateData()
    {	
    	//nasze "uchwyty"
    	ArrayList<Long> peopleIDs = new ArrayList<>();
    	ArrayList<Long> gymsIDs = new ArrayList<>();
    	
    	//generujemy randomowe namy i wsadzamy je do bazy
    	Iterator<Name> nameIterator = Generator.getRandomNames(50);
    	while(nameIterator.hasNext()) {
    		Name name = nameIterator.next();
    		String fullName = name.getFirstName() + " " + name.getLastName();
    		peopleIDs.add(addPerson(fullName, Generator.getRandomInt(1960, 1990), Generator.getRandomInt(30, 65)));
    	}
    	
    	//generujemy randomowe gymy i wsadzamy je do bazy
    	Iterator<GymDescription> gymIterator = Generator.getRandomGymName(20);
    	while(gymIterator.hasNext()) {
    		GymDescription description = gymIterator.next();
    		gymsIDs.add(addGym(description.getName(), description.getTagline()));
    	}
    	
    	//adding relation FIRST_TRAINING_IN between person and gym
    	for(int i=0;i<peopleIDs.size();i++) {
    		long personID = peopleIDs.get(i);
    		long gymID = gymsIDs.get(Generator.getRandomInt(0, gymsIDs.size()));
    		int year = Generator.getRandomInt(1997, 2005);
    		this.addFIRST_TRAINING_IN(personID, gymID, year);
    	}
    	
    	 
    	
    	//adding relation TRAINING_IN between person and gym
    	for(int i=0;i<peopleIDs.size();i++) {
    		long personID = peopleIDs.get(i);
    		long gymID = gymsIDs.get(Generator.getRandomInt(0, gymsIDs.size()));
    		int since = Generator.getRandomInt(2005, 2017);
    		this.addTRAINING_IN(personID, gymID, since);
    	}
    	
    	
    	
    	//adding relation VISITED between gym and person
    	for(int i=0;i<peopleIDs.size();i++) {
    		
    		long personID = peopleIDs.get(i);
    		for(int j=0;j<gymsIDs.size();j++) {
    			
    			
    			//czyli mamy 30% szans, ze ktos odwiedzil dana silownie
    			if(Generator.getRandomInt(0, 100) > 70) {
    				try (Transaction t = graphDatabaseService.beginTx()){
	    				long gymID = gymsIDs.get(j);
	    				Node person = graphDatabaseService.getNodeById(personID);
	    				Node gym = graphDatabaseService.getNodeById(gymID);
	    				
	    				Iterator<Relationship> rels = person.getRelationships().iterator();
	    				
	    				//Musimy sprawdzic, czy nasz person tam nie trenuje
	    				//lub czy nie zaczynal tam trenowac
	    				boolean isTrainingInThereOrStartedTrainingThere = false;
	    				while(rels.hasNext()) {
	    					Relationship rel = rels.next();
	    					
	    					if(rel.getOtherNode(person).equals(gym)) {
	    						isTrainingInThereOrStartedTrainingThere = true;
	    						break;
	    					}
	    				}
	    				
	    				if(!isTrainingInThereOrStartedTrainingThere) {
	    					this.add_VISITED(personID, gymsIDs.get(j));
	    				}
	    				
	    				t.success();
    				}
    				
    			}
    			
    		}
    		
    	}
    	
    }
    //helper
    public Node getNodeByID(long nodeID)
    {
    	try(Transaction t = graphDatabaseService.beginTx()) 
    	{
    		Node node = graphDatabaseService.getNodeById(nodeID);
    		t.success();
    		return node;
    	}
    }
    
    //ZADANIE 4
    public String getNodeAllRelations(Node node) 
    {
        try(Transaction t = graphDatabaseService.beginTx()) 
        {
            Result r = graphDatabaseService.execute(String
            		.format("MATCH (p) - [r] -> () WHERE ID(p) = %d RETURN r AS Relationships", node.getId()));
            t.success();
            return r.resultAsString();
        }
    }
    //helper
    public void deleteAllRelationsOdNode(long nodeID) {
    	 try (Transaction tx = graphDatabaseService.beginTx()) {
    	     Node node = graphDatabaseService.getNodeById(nodeID);    
    	     for (Relationship r : node.getRelationships()) {
    	         r.delete();
    	     }
    	     tx.success();
    	     tx.close();
    	 }
    }
    
    //helper
    public void deleteNode(long nodeID) {
    	 try (Transaction tx = graphDatabaseService.beginTx()) {
    	     Node node = graphDatabaseService.getNodeById(nodeID);    
    	     node.delete();
    	     tx.success();
    	     tx.close();
    	 }
    } 
    //ZADANIE 5
    public String findPathBetweenNodes(Node node1, Node node2) 
    {
        try(Transaction t = graphDatabaseService.beginTx()) 
        {
            Result result = graphDatabaseService.execute(String
            		.format("MATCH path = shortestPath((p1) - [*] - (p2)) WHERE ID(p1) = %d AND ID(p2) = %d "
            				+ "RETURN path AS Path", node1.getId(), node2.getId()));
            
          
            t.success();
            return result.resultAsString();
        }
    }
}
