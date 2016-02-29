package org.octoteam.octoproject;

import org.octoteam.octoproject.Entities.Session;
import org.octoteam.octoproject.Entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexander on 23/02/16.
 */
public class SessionManager {
    final static int SESSION_LIFETIME = 60 * 5; // 5 minutes

    public static String createSession(long user_id, String ip) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        User user = em.find(User.class, user_id);

        Session session = new Session(user, ip);
        em.persist(session);
        String token = session.getToken();

        em.getTransaction().commit();
        em.close();
        emf.close();

        return token;
    }

    public static long checkSession(String token, String ip) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Query query = em.createQuery("SELECT s FROM Session s WHERE s.token = :token AND s.ip = :ip AND s.is_open = true");
        query.setParameter("token", token);
        query.setParameter("ip", ip);

        List<Session> sessions = query.getResultList();

        if (sessions.size() != 0) {     // Session exists for ip and not closed
            Session session = sessions.get(0);
            // Session is not expired
            if (((new Date()).getTime() - session.getLast_access().getTime()) / 1000 < SESSION_LIFETIME) {
                session.setLast_access(new Date());

                em.getTransaction().commit();
                em.close();
                emf.close();

                return session.getUser().getId();
            }
        }

        em.getTransaction().commit();
        em.close();
        emf.close();

        return -1l; // Wrong token
    }

    public static void closeSession(String token, String ip) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("OctoService");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Query query = em.createQuery("SELECT s FROM Session s WHERE s.token = :token AND s.ip = :ip");
        query.setParameter("token", token);
        query.setParameter("ip", ip);

        List<Session> sessions = query.getResultList();

        if (sessions.size() != 0) {
            sessions.get(0).setIs_open(false);
        }

        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}
