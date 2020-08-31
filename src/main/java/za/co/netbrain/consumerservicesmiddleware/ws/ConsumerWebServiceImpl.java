package za.co.netbrain.consumerservicesmiddleware.ws;

import java.util.Date;
import java.util.logging.Logger;
import za.co.netbrain.controllers.AppointmentsJpaController;
import za.co.netbrain.controllers.ClientJpaController;
import za.co.netbrain.controllers.ServiceproviderJpaController;
import za.co.netbrain.controllers.util.SparEntityManagerUtil;
import za.co.netbrain.entities.Appointments;
import za.co.netbrain.entities.Client;
import za.co.netbrain.entities.Serviceprovider;

/**
 *
 * @author Simphiwe.Twala
 */
public class ConsumerWebServiceImpl extends SparEntityManagerUtil {

    private static final Logger logger = Logger.getLogger(ConsumerWebServiceImpl.class.getName());

    public String serviceProviderRegistration(String name,String surname, String contactnumber, String emailaddress, String physicaladdress) {

        ServiceproviderJpaController serviceproviderJpaController = new ServiceproviderJpaController(getEntityManagerFactory());

        Serviceprovider serviceProvider = new Serviceprovider();
        serviceProvider.setName(name);
        serviceProvider.setSurname(surname);
        serviceProvider.setEmailaddress(emailaddress);
        serviceProvider.setContactnumber(contactnumber);

        try {
            serviceproviderJpaController.create(serviceProvider);
            return serviceProvider.toString();
        } catch (RuntimeException runtimeException) {
            return runtimeException.getMessage();
        }
    }

    public String clientRegistration(String name, String surname, String contactnumber, String emailaddress) {

        ClientJpaController clientJpaController = new ClientJpaController(getEntityManagerFactory());

        Client client = new Client();
        client.setName(name);
        client.setSurname(surname);
        client.setEmail(name);
        client.setContact(name);

        try {
            clientJpaController.create(client);
            return client.toString();
        } catch (RuntimeException runtimeException) {
            return runtimeException.getMessage();
        }
    }

    public String appointmentBooking(Object date, String status, Object serviceprovider) {

        AppointmentsJpaController appointmentsJpaController = new AppointmentsJpaController(getEntityManagerFactory());

        Appointments appointments = new Appointments();
        appointments.setAppointmentDate((Date) date);
        appointments.setAppointmentstatus(status);
        appointments.setServiceproviderpk((Serviceprovider) serviceprovider);

        try {
            appointmentsJpaController.create(appointments);
            return appointments.toString();
        } catch (RuntimeException runtimeException) {
            return runtimeException.getMessage();
        }

    }
}
