package introsde.rest.ehealth.resources;
/*
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Stateless // will work only inside a Java EE application
@LocalBean // will work only inside a Java EE application
@Path("person/{personId}/lifestatus")
public class LifeStatusCollectionResource {

    // Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    // will work only inside a Java EE application
    @PersistenceUnit(unitName="introsde-jpa") // where to store data. persistence.xml file in web contetn folder
    EntityManager entityManager;

    // will work only inside a Java EE application
    @PersistenceContext(unitName = "introsde-jpa",type=PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;
    
    @GET
    @Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
    public List<LifeStatus> getLifeStatusBrowser(@PathParam("personId") int personId) {
        System.err.println("Getting list of ls...");
        Person p = Person.getPersonById(personId);
        List<LifeStatus> ls = p.getLifeStatus();
        System.err.println("List of ls created. " + ls.size());
        return ls;
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML})
    public LifeStatus newLifeStatus(@PathParam("personId") int personId, LifeStatus ls) throws IOException {
        System.err.println("in save ls");    
        Person p = Person.getPersonById(personId);
        p.getLifeStatus().add(ls);
        ls.setPerson(p);
        return LifeStatus.saveLifeStatus(ls);
    }
    
    
   
    @Path("{lsID}")
    public LifeStatusResource getLifeStatus(@PathParam("lsID") int id) {
        return new LifeStatusResource(uriInfo, request, id);
    }

}
*/
