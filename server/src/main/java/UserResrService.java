import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.Random;

/**
 * Created by Alexander on 18/02/16.
 */

@Path("/user")
public class UserResrService {

    @GET
    @Path("/add/{username}")
    public Response addUser(@PathParam("username") String username) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeService");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Random r = new Random();
        User user = new User(username, username, username, username);
        em.persist(user);
        em.getTransaction().commit();

        return Response.status(200).entity("User added.").build();
    }
}
