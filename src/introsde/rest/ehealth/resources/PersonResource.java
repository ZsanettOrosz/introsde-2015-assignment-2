package introsde.rest.ehealth.resources;

import java.util.List;

import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.model.HealthMeasureHistory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container
public class PersonResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public PersonResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
    }

    public PersonResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }

    // Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Person getPerson() {
        Person person = this.getPersonById(id);
        if (person == null)
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        	//throw new RuntimeException("Get: Person with " + id + " not found");
        return person;
    }
    

    // for the browser
    @GET
    @Produces(MediaType.TEXT_XML)
    public Person getPersonHTML() {
        Person person = this.getPersonById(id);
        if (person == null)
            throw new RuntimeException("Get: Person with " + id + " not found");
        System.out.println("Returning person... " + person.getIdPerson());
        return person;
    }
    
    @PUT
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response putPerson( Person person) {
        System.err.println("--> Updating Person... " +this.id);
        System.err.println("--> " + person.toString());
        
        
        
       // Person.updatePerson(person);
        Response res;
        Person existing = getPersonById(this.id);

        if (existing == null) {
        	System.err.println("No content" );
            res = Response.noContent().build();
        } else {
            res = Response.created(uriInfo.getAbsolutePath()).build();
            person.setIdPerson(this.id);
            Person fromDB = Person.getPersonById(this.id);
            
            System.err.println("Exists and id :" +  fromDB.getIdPerson() );
          	if(person.getName()!= null && !fromDB.getName().equals(person.getName())){
        		fromDB.setName(person.getName());
        	}
        	if(person.getLastname()!= null && !fromDB.getLastname().equals(person.getLastname())){
        		fromDB.setLastname(person.getLastname());
        	}
        	if(person.getBirthdate()!= null && fromDB.getBirthdate() == person.getBirthdate()){
        		fromDB.setBirthdate(person.getBirthdate());
        	}
        	if(person.getEmail()!= null && !fromDB.getEmail().equals(person.getEmail())){
        		fromDB.setEmail(person.getEmail());
        	}
        	if(person.getUsername()!= null && !fromDB.getUsername().equals(person.getUsername())){
        		fromDB.setUsername(person.getUsername());
        	}
                        
            Person.updatePerson(fromDB);
        }
        return res;
    }

    @DELETE
    public void deletePerson() {
        Person c = getPersonById(id);
        if (c == null){
        	throw new WebApplicationException(Response.Status.NOT_FOUND);
        	//Response.status(Response.Status.NOT_FOUND);
        	//throw new RuntimeException("Delete: Person with " + id + " not found");
        	
        }else{
        	Person.removePerson(c);
        }
    }

    public Person getPersonById(int personId) {
        System.out.println("Reading person from DB with id: "+personId);

        // this will work within a Java EE container, where not DAO will be needed
        //Person person = entityManager.find(Person.class, personId); 

        Person person = Person.getPersonById(personId);
        if(person == null) {
			return null;
		}
        System.out.println("Person: "+person.toString());
        return person;
    }
}