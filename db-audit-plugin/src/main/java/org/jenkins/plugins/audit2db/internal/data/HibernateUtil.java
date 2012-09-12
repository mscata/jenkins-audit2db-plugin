/**
 * 
 */
package org.jenkins.plugins.audit2db.internal.data;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.jenkins.plugins.audit2db.internal.model.BuildDetailsImpl;
import org.jenkins.plugins.audit2db.internal.model.BuildParameterImpl;

/**
 * Utility class for Hibernate access.
 * 
 * @author Marco Scata
 *
 */
public class HibernateUtil {
	private final static Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());

    public static SessionFactory getSessionFactory(final Properties extraProperties) {
    	SessionFactory retval = null;
    	
    	try {
	    	// Load base configuration from hibernate.cfg.xml
    		LOGGER.log(Level.INFO, "Loading configuration file");
	    	final Configuration config = new AnnotationConfiguration().configure();
	    	if ((extraProperties != null) && !extraProperties.isEmpty()) {
	    		LOGGER.log(Level.FINE, "Setting extra properties.");
	    		LOGGER.log(Level.FINE, extraProperties.toString());
	    		config.addProperties(extraProperties);
	    	}
	        retval = config.buildSessionFactory();
        } catch (final Exception e) {
            // Make sure you log the exception, as it might be swallowed
            LOGGER.log(Level.SEVERE, "Initial SessionFactory creation failed.", e);
            throw new RuntimeException(e);
        }

        return retval;
    }
    
    public static SessionFactory getSessionFactory() {
    	return getSessionFactory(null);
    }
    
    public static Properties getExtraProperties(
    		final String driverClass,
    		final String driverUrl,
    		final String username,
    		final String password) {
		final Properties props = new Properties();
		props.put("hibernate.connection.driver_class", driverClass);
		props.put("hibernate.connection.url", driverUrl);
		props.put("hibernate.connection.username", username);
		props.put("hibernate.connection.password", password);
		
		return props;
    }
}
