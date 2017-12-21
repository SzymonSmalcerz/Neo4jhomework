package com.smalcerz.neo.helpers;

import java.io.File;

public class DeathStarForDirectiories {
		//helper
		public static void deleteFileOrDirectory( final File file ) {
		    if ( file.exists() ) {
		        if ( file.isDirectory() ) {
		            for ( File child : file.listFiles() ) {
		                deleteFileOrDirectory( child );
		            }
		        }
		        file.delete();
		    }
		}
}
