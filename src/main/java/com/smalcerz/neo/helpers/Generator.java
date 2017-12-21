package com.smalcerz.neo.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.ajbrown.namemachine.NameGeneratorOptions;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;




public class Generator {
	public static Iterator<Name> getRandomNames(int howManyNames){
		
		NameGeneratorOptions options = new NameGeneratorOptions();

    	//Heavily prefer male names.
    	options.setGenderWeight( 12.23 );
    	NameGenerator generator = new NameGenerator( options );
    	List<Name> names = generator.generateNames( howManyNames );

		return names.iterator();
	}
	
	
	public static int getRandomInt(int min, int max) {
		return (int) (Math.floor(Math.random()*(max-min)) + min);
	}
	
	
	public static Iterator<GymDescription> getRandomGymName(int howManyGyms) {

		NameGeneratorOptions options = new NameGeneratorOptions();
		Lorem loremIpsum = LoremIpsum.getInstance();
    	//Heavily prefer male names.
    	options.setGenderWeight( 12.23 );
    	NameGenerator generator = new NameGenerator( options );
    	List<Name> names = generator.generateNames( howManyGyms, Gender.FEMALE );
		
		List<GymDescription> gyms = new ArrayList<>();
		for(int i=0;i<howManyGyms;i++) {
			String gymName = names.get(i).getFirstName() + names.get(i).getLastName() + "Gym";
			gyms.add(new GymDescription(gymName, loremIpsum.getWords(3,10)));
		}
		
		return gyms.iterator();
	}
}
