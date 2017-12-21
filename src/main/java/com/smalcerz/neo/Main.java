package com.smalcerz.neo;

public class Main 
{	
    public static void main(String[] args) 
    {
    	String productionGraphLocation = "/home/szymcio/Desktop/neo4jCommunity/neo4j-community-3.3.1/data/databases/graph.db";
    	@SuppressWarnings("unused")
		GraphDatabase graphDatabase = GraphDatabase.createDatabase(productionGraphLocation);
    	System.out.println("asd");

    }

//    
//    public static boolean test_creating_paths(GraphDatabase graphDatabase)
//	{
//		long p1 = graphDatabase.addPerson("Julie Nigga");
//		long p2 = graphDatabase.addPerson("Robert Smith");
//		long p3 = graphDatabase.addPerson("Nicholas Stage");
//    	long p4 = graphDatabase.addCity("Valencia", "Spain");
//    	long p5 = graphDatabase.addCity("Hamburg", "Germany");
//    	
//    	graphDatabase.add_BORN_IN(p1, p4, 1982); // Julie Nigga - BORN_IN -> Valencia (1982)
//    	graphDatabase.add_BORN_IN(p2, p4, 1987); // Robert Smith - BORN_IN -> Valencia (1987)
//    	graphDatabase.add_BORN_IN(p3, p5, 1986); // Nicholas Stage - BORN_IN -> Hamburg (1986)
//    	graphDatabase.add_LIVES_IN(p1, p4, 1982); // Julie Nigga - LIVES_IN -> Valencia (1982)
//    	graphDatabase.add_LIVES_IN(p2, p5, 1987); // Robert Smith - LIVES_IN -> Hamburg (1987)
//    	graphDatabase.add_LIVES_IN(p3, p5, 1982); // Nicholas Stage - LIVES_IN -> Hamburg (1982)
// 
//    	String result = graphDatabase.findPath(graphDatabase.getNode(p1), graphDatabase.getNode(p3));
//    	
//    	graphDatabase.delete_node_relationships(p1);
//    	graphDatabase.delete_node_relationships(p2);
//    	graphDatabase.delete_node_relationships(p3);
//    	graphDatabase.delete_node_relationships(p4);
//    	graphDatabase.delete_node_relationships(p5);
//    	
//    	if(!result.contains(String.format("Node[%d]", p1))) // p1 -> p4 <- p2 -> p5 <- p3 (sciezka)
//        	return false;
//    	if(!result.contains(String.format("Node[%d]", p4)))
//    		return false;
//    	if(!result.contains(String.format("Node[%d]", p2)))
//    		return false;
//    	if(!result.contains(String.format("Node[%d]", p5)))
//    		return false;
//    	if(!result.contains(String.format("Node[%d]", p3)))
//    		return false;
//    	
//    	if(!(StringUtils.countMatches(result, "Node") == 5))
//    		return false;
//    	
//    	return true;
//	}
}
