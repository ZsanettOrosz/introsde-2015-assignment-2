package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless // only used if the the application is deployed in a Java EE container
@LocalBean // only used if the the application is deployed in a Java EE container

public class HistoryResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;

    EntityManager entityManager; // only used if the application is deployed in a Java EE container

    public HistoryResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
        this.entityManager = em;
    }

    public HistoryResource(UriInfo uriInfo, Request request,int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }
    
    // Application integration
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public HealthMeasureHistory getHistory() {
        HealthMeasureHistory his = this.getHistoryById(id);
        if (his == null){
            System.err.println("Empty history");
        	throw new RuntimeException("Get: history with " + id + " not found");
        }
            return his;
    }

    

    @PUT
    //@Path("/person/{personId}/{measureType}/{mid}")
    @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response putPerson(HealthMeasureHistory newHis) {
        System.out.println("--> Updating History... " +this.id);
        System.out.println("--> "+newHis.toString());
        //HealthMeasureHistory.updateHealthMeasureHistory(newHis);
        Response res;
        HealthMeasureHistory existing = getHistoryById(this.id);

        if (existing == null) {
            res = Response.noContent().build();
        } else {
            res = Response.created(uriInfo.getAbsolutePath()).build();
            //newHis.setIdMeasureHistory(this.id);
            //newHis.setPerson(Person.getPersonById(personId));
            if(existing.getValue() != newHis.getValue()){
            	existing.setValue(newHis.getValue());
            	HealthMeasureHistory.updateHealthMeasureHistory(existing);
            }
           
        }
        return res;
    }
    
    
    public HealthMeasureHistory getHistoryById(int historyId) {
        HealthMeasureHistory his = HealthMeasureHistory.getHealthMeasureHistoryById(historyId);
        System.out.println("History: "+his.toString());
        return his;
    }
}
