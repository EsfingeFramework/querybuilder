package org.esfinge.querybuilder.methodparser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.querybuilder.annotation.DomainTerm;
import org.esfinge.querybuilder.annotation.DomainTerms;
import org.esfinge.querybuilder.utils.StringUtils;

public class TermLibrary {
	
	private Map<String,DomainTerm> terms = new HashMap<String,DomainTerm>();
	
	public TermLibrary(Class<?> interf){
		if(interf.isAnnotationPresent(DomainTerm.class)){
			DomainTerm annot = interf.getAnnotation(DomainTerm.class);
			terms.put(annot.term(), annot);
		}
		if(interf.isAnnotationPresent(DomainTerms.class)){
			DomainTerms anot = interf.getAnnotation(DomainTerms.class);
			for(DomainTerm term : anot.value()){
				terms.put(term.term(), term);
			}
		}
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
