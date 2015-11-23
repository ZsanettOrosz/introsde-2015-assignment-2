package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElementWrapper;

@Stateless
@LocalBean
@Path("person/{personID}/{measureType}")
public class HistoryCollectionResource {
	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// will work only inside a Java EE application
	@PersistenceUnit(unitName = "introsde-jpa")
	// where to store data. persistence.xml file in web contetn folder
	EntityManager entityManager;

	// will work only inside a Java EE application
	@PersistenceContext(unitName = "introsde-jpa", type = PersistenceContextType.TRANSACTION)
	private EntityManagerFactory entityManagerFactory;

	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML })
	public List<HealthMeasureHistory> getHistoryByMeasureType(
			@PathParam("measureType") String type,
			@PathParam("personID") int personId) {
		System.err.println("only measure type");
		Person p = Person.getPersonById(personId);
		List<HealthMeasureHistory> his = p.getMeasureHistories();
		List<HealthMeasureHistory> hisByType = new ArrayList<>();
		for (int i = 0; i < his.size(); i++) {
			if (type.equalsIgnoreCase(his.get(i).getMeasureDefinition()
					.getMeasureName())) {
				hisByType.add(his.get(i));
			}
		}
		if(hisByType.size() == 0)
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		// List<HealthMeasureHistory> his =
		// HealthMeasureHistory.getHistoriesByType(typeId);
		return hisByType;
	}


	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Person newStatus(@PathParam("personID") int personID,
			@PathParam("measureType") String measureType, HealthMeasureHistory postedValue)
			throws IOException {

		System.err.println("In post of history");
		Person person = Person.getPersonById(personID);
		postedValue.setPerson(person);
		System.err.println("Person: " + person.getIdPerson());

		MeasureDefinition type = MeasureDefinition.getByName(measureType);
		System.err.println("Type: " + type.getMeasureName());
		postedValue.setMeasureDefinition(type);
		LifeStatus newLifeStatus = new LifeStatus(person, type, postedValue.getValue()); // new
																		// LS to
																		// save
		System.err.println("Value: " + newLifeStatus.getValue());

		LifeStatus oldLifeStatus = person.getLifeStatusByMeasureType(type);
		if (oldLifeStatus == null) {
			person.addLifeStatus(newLifeStatus);
			LifeStatus.saveLifeStatus(newLifeStatus);
			Person.updatePerson(person);
		} else {
			System.err.println("Old value: " + oldLifeStatus.getValue());
			
			//person.addHistory(postedValue);
			//HealthMeasureHistory.saveHealthMeasureHistory(postedValue);
			
			//here should go the old lifestatus
			HealthMeasureHistory witOldLFS = new HealthMeasureHistory(oldLifeStatus);
			person.addHistory(witOldLFS);
			HealthMeasureHistory.saveHealthMeasureHistory(witOldLFS);
			
			
			System.err.println("Length of the history after: "
					+ person.getMeasureHistories().size());

			person.removeLifeStatus(oldLifeStatus);
			LifeStatus.removeLifeStatus(oldLifeStatus);

			person.addLifeStatus(newLifeStatus);
			LifeStatus.saveLifeStatus(newLifeStatus);

			Person.updatePerson(person);
		}

		return person;
	}
	
	@Path("{mid}")
	public HistoryResource getPerson(@PathParam("mid") int id) {
		return new HistoryResource(uriInfo, request, id);
	}

}
