package com.ontology.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontology.jenaWork.InitJena;
import com.ontology.models.RequestPOJO;

import net.minidev.json.JSONObject;

@RestController
public class MonControlleur {

	public static final String PREFIX_POLITICIAN = "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#> ";
	public static final String PREFIX_RDF = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ";

	/**
	 * ws pour les provinces
	 */
	@RequestMapping(value = "/getProvinces", method = GET)
	public List<JSONObject> getProvinces() {
		String queryString = "SELECT ?X WHERE{ ?X rdf:type pol:Province. }";
		final String queryWithPrefixs = PREFIX_POLITICIAN + PREFIX_RDF + queryString;

		List<JSONObject> resultSet = InitJena.getItems(queryWithPrefixs);
		System.out.println(queryWithPrefixs);

		return resultSet;
	}

	/**
	 * ws pour trouver tous les politcians
	 */
	@CrossOrigin
	@RequestMapping(value = "/findPolitician", method = GET)
	public List<JSONObject> findPolitician(@RequestBody RequestPOJO requestPOJO) {
		boolean allEmpty = true;
		System.out.println(requestPOJO);
		/**
		 * TODO changer le pol
		 */
		String queryString = "PREFIX pol: <http://www.semanticweb.org/viva/ontologies/2019/6/politicians#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT ?Y WHERE { ";

		if (requestPOJO.getDistrict() != null && !"".equals(requestPOJO.getDistrict())) {
			allEmpty = false;
			queryString += "?Y pol:isElectedFrom pol:" + requestPOJO.getDistrict() + ".";
		}
		if (requestPOJO.getProvince() != null && !requestPOJO.getProvince().equals("")) {
			allEmpty = false;
			queryString += "?Z pol:isDistrictOf pol:" + requestPOJO.getProvince() + "." + "?Y pol:isElectedFrom ?Z .";
		}

		if (requestPOJO.getParty() != null && !requestPOJO.getParty().equals("")) {
			allEmpty = false;
			queryString += "?Y pol:isMemberOfParty pol:" + requestPOJO.getParty() + ".";
		}

		if (requestPOJO.getPoliticalInstituteInstance() != null
				&& !requestPOJO.getPoliticalInstituteInstance().equals("")) {
			allEmpty = false;
			queryString += "?A rdfs:subPropertyOf pol:isMemberOfInstitution. ?Y ?A pol:"
					+ requestPOJO.getPoliticalInstituteInstance() + ".";
		}

		if (allEmpty) {
			List<JSONObject> emptyArray = new ArrayList<>();
			return emptyArray;
		}
		queryString += " }";
		System.out.println(queryString);

		List<JSONObject> resultSet = InitJena.findPoliticians(queryString);
		return resultSet;
	}

	/**
	 * ws pour decrire un politician
	 */
	/**
	 * ws pour les districts
	 */

}
