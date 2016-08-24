package org.esfinge.querybuilder.methodparser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.DomainTerms;
import org.esfinge.querybuilder.utils.StringUtils;

public class TermLibrary {
	
	private Map<String, DomainTerm> terms = new HashMap<String, DomainTerm>();
	
	public TermLibrary(Class<?> inter) {
		initialize(inter);
	}
	
	// adding this only for the recognition of domain terms by the plugin
	// this does not imply any change in the main flow
	public TermLibrary(Class<?> inter, ClassLoader loader) {
		try {
    		if (loader == null) {
    			initialize(inter);
    		} else {
    			Class<DomainTerm> domain = (Class<DomainTerm>) loader.loadClass(DomainTerm.class.getCanonicalName());
    			if(inter.isAnnotationPresent(domain)){
    				addTerm(inter.getAnnotation(domain));
    			}
    			Class<DomainTerms> domains = (Class<DomainTerms>) loader.loadClass(DomainTerms.class.getCanonicalName());
    			if(inter.isAnnotationPresent(domains)) {
    				DomainTerms domainTerms = inter.getAnnotation(domains);
    				for(DomainTerm term : domainTerms.value()){
    					addTerm(term);
    				}
    			}
    		}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void initialize(Class<?> inter) {
		if(inter.isAnnotationPresent(DomainTerm.class)){
			DomainTerm domain = inter.getAnnotation(DomainTerm.class);
			terms.put(domain.term(), domain);
		}
		if(inter.isAnnotationPresent(DomainTerms.class)){
			DomainTerms domains = inter.getAnnotation(DomainTerms.class);
			for(DomainTerm term : domains.value()){
				terms.put(term.term(), term);
			}
		}
	}

	private void addTerm(Annotation domain) throws IOException, ClassNotFoundException {
		DomainTerm term = (DomainTerm) TermLibrary.cloneAnnotationBySerialization(domain);
		terms.put(term.term(), term);
	}

	/**
	 * Makes a deep cloning by serialization of a given annotation.
	 * This is necessary when a given annotation came from another ClassLoader
	 * and it's not possible to extract the annotation from it's source or even
	 * compare it's type (due the incompatibility with the ClassLoaders) which is
	 * what basically happens on QueryBuilder Eclipse Plugin for example.
	 * When a deep cloning by serialization is done the annotation which 
	 * was initially loaded by a distinct ClassLoader is reloaded with
	 * the appropriate ClassLoader during the deserializing process.
	 * @param annotation - the annotation to be cloned.
	 * @return The annotation clone with the appropriate ClassLoader.
	 * When it's not possible to clone the annotation by serialization
	 * then return the own annotation without changing it's ClassLoader.
	 */
	public static Annotation cloneAnnotationBySerialization(Annotation annotation) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream serializer = new ObjectOutputStream(out);
		serializer.writeObject(annotation);
		
		out.close();
		serializer.close();
 	    
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		ObjectInputStream deserializer = new ObjectInputStream(in);
		Annotation clone = (Annotation) deserializer.readObject();
		
		in.close();
		deserializer.close();
		
		return clone;
	}
	
	public boolean hasDomainTerm(List<String> words, int i){
		for(String term : terms.keySet()){
			if(StringUtils.matchString(term, words, i)){
				return true;
			}
		}
		return false;
	}
	
	public DomainTerm getDomainTerm(List<String> words, IndexCounter index){
		for(String term : terms.keySet()){
			if(StringUtils.matchString(term, words, index.get())){
				return terms.get(term);
			}
		}
		return null;
	}

}
