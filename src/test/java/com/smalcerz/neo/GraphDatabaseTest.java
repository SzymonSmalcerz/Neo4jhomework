package com.smalcerz.neo;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;


import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.smalcerz.neo.helpers.DeathStarForDirectiories;

import junit.framework.TestCase;

@RunWith(Parameterized.class)
public class GraphDatabaseTest extends TestCase {
	
	private static int guard = 0;
	private GraphDatabase graphDatabase = GraphDatabase.createDatabase("./testGraph" + guard++ + ".db");
	private long personID;
	private long gymID;
	
	
	@Before
	public void cleanAllRealtionsOfNode() {
		this.graphDatabase.deleteAllRelationsOdNode(this.personID);
		this.graphDatabase.deleteAllRelationsOdNode(this.gymID);
	}
	
	//clean dbgraph
	@AfterClass
	public static void clearDatabase() {
		DeathStarForDirectiories.deleteFileOrDirectory(new File("./testGraph.db"));
	}
	
	public GraphDatabaseTest(String name, int yearOfBirth, int circuitOfBiceps, String gymName, String gymTagline) {
		this.personID = this.graphDatabase.addPerson(name, yearOfBirth, circuitOfBiceps);
		this.gymID = this.graphDatabase.addGym(gymName,gymTagline);
	}
	
	@Parameters
	public static Collection<Object[]> testConditions() throws IOException {
		Object expectedOutputs[][] = { 
				{"Szymon",1950,50, "SsGym", "ssuper gym"}, 
				{"Bartek",1960,30, "SssGym", "ino biceps"},
				{"Damian",1970,60, "SsssGym", "sUpEr GyM"},};
		return Arrays.asList(expectedOutputs);
	}
	
	@Test
	public void test_getNodeAllRelations_VISITED(){
		long relationVISITEDID = this.graphDatabase.add_VISITED(this.personID, this.gymID);
		String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		assertEquals(true, realtionsOfPerson.contains(String.format(":VISITED[%d]{}", relationVISITEDID)));
	}
	
	
	@Test
	public void test_getNodeAllRelations_NOT_VISITED(){
		String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		// PODALEM W TESCIE ID rowne 80, ale tak na prawde obojetnie jakie id wieksze niz 5 
		// podajemy, powinno zadzialac, bo czyscimy z kazdym testem naszego persona z relacji
		// analogicznie w poznizszych testach
		assertEquals(false, realtionsOfPerson.contains(String.format(":VISITED[%d]{}", 80)));
	}
	
	
	@Test
	public void test_getNodeAllRelations_FIRST_TRAINING_IN(){
		int year = 2010;
		long relationFIRSTTRAINING = this.graphDatabase.addFIRST_TRAINING_IN(this.personID, this.gymID,year);
		String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		assertEquals(true, realtionsOfPerson.contains(String.format(":FIRST_TRAINING_IN[%d]{year:%d}", relationFIRSTTRAINING, year )));
	}
	
	
	@Test
	public void test_getNodeAllRelations_NOT_FIRST_TRAINING_IN(){
		int year = 2010;String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		assertEquals(false, realtionsOfPerson.contains(String.format(":FIRST_TRAINING_IN[%d]{year:%d}", 5, year )));
	}
	

	@Test
	public void test_getNodeAllRelations_TRAINING_IN(){
		int since = 2010;
		long relationTRAININGIN = this.graphDatabase.addTRAINING_IN(this.personID, this.gymID,since);
		String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		assertEquals(true, realtionsOfPerson.contains(String.format(":TRAINING_IN[%d]{since:%d}", relationTRAININGIN, since )));
	}
	
	
	@Test
	public void test_getNodeAllRelations_NOT_TRAINING_IN(){
		int since = 2010;
		String realtionsOfPerson = this.graphDatabase.getNodeAllRelations(this.graphDatabase.getNodeByID(this.personID));
		assertEquals(false, realtionsOfPerson.contains(String.format(":TRAINING_IN[%d]{since:%d}", 5, since )));
	}
	
	
	@Test
	public void test_findingPathBetweenNodes_path_exists() {
		long person1ID = this.graphDatabase.addPerson("person1", 1990, 50);
		long person2ID = this.graphDatabase.addPerson("person2", 1990, 50);
		long person3ID = this.graphDatabase.addPerson("person3", 1990, 50);
		long person4ID = this.graphDatabase.addPerson("person4", 1990, 50);
		
		long gymBetweenOurClassPersonANDPerson1 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		long gymBetweenPerson1ANDPerson2 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		long gymBetweenPerson2ANDPerson3 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		long gymBetweenPerson3ANDPerson4 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		
		
		//we want to create path : this.person -> person1 -> person2 -> person3 -> person4
		
		this.graphDatabase.add_VISITED(this.personID, gymBetweenOurClassPersonANDPerson1);
		this.graphDatabase.add_VISITED(person1ID, gymBetweenOurClassPersonANDPerson1);
		this.graphDatabase.add_VISITED(person1ID, gymBetweenPerson1ANDPerson2);
		this.graphDatabase.add_VISITED(person2ID, gymBetweenPerson1ANDPerson2);
		this.graphDatabase.add_VISITED(person2ID, gymBetweenPerson2ANDPerson3);
		this.graphDatabase.add_VISITED(person3ID, gymBetweenPerson2ANDPerson3);
		this.graphDatabase.add_VISITED(person3ID, gymBetweenPerson3ANDPerson4);
		this.graphDatabase.add_VISITED(person4ID, gymBetweenPerson3ANDPerson4);
		
		
		String r = graphDatabase.findPathBetweenNodes(graphDatabase.getNodeByID(this.personID), graphDatabase.getNodeByID(person4ID));
		
		assertEquals(true, r.contains(String.format("Node[%d]", person4ID)));
		
	}
	
	
	@Test
	public void test_findingPathBetweenNodes_path_doesnt_exist() {
		long person1ID = this.graphDatabase.addPerson("person1", 1990, 50);
		long person3ID = this.graphDatabase.addPerson("person3", 1990, 50);
		long person4ID = this.graphDatabase.addPerson("person4", 1990, 50);
		
		long gymBetweenOurClassPersonANDPerson1 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		long gymBetweenPerson1ANDPerson2 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		long gymBetweenPerson3ANDPerson4 = this.graphDatabase.addGym("gymBetweenPerson1ANDPerson2", "asd");
		
		
		//we want to create path : this.person -> person1 -> person2 -> person3 -> person4
		
		this.graphDatabase.add_VISITED(this.personID, gymBetweenOurClassPersonANDPerson1);
		this.graphDatabase.add_VISITED(person1ID, gymBetweenOurClassPersonANDPerson1);
		this.graphDatabase.add_VISITED(person1ID, gymBetweenPerson1ANDPerson2);
		this.graphDatabase.add_VISITED(person3ID, gymBetweenPerson3ANDPerson4);
		this.graphDatabase.add_VISITED(person4ID, gymBetweenPerson3ANDPerson4);
		
		
		String r = graphDatabase.findPathBetweenNodes(graphDatabase.getNodeByID(this.personID), graphDatabase.getNodeByID(person4ID));
		
		assertEquals(false, r.contains(String.format("Node[%d]", person4ID)));
		
	}
	
	
}
