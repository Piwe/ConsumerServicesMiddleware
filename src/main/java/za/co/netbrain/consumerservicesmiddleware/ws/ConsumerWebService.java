package za.co.netbrain.consumerservicesmiddleware.ws;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Simphiwe.Twala
 */
@WebService(serviceName = "ConsumerServices")
@Stateless()
public class ConsumerWebService {
    
    private final ConsumerWebServiceImpl sparWebServiceImpl = new ConsumerWebServiceImpl();

    private static final Logger logger = Logger.getLogger(ConsumerWebServiceImpl.class.getName());

    @WebMethod(operationName = "RegisterServiceProvider")
    public String serviceProviderRegistration(@WebParam(name = "name") String name, @WebParam(name = "surname") String surname, @WebParam(name = "contactnumber") String contactnumber, @WebParam(name = "email") String emailaddress, @WebParam(name = "address") String physicaladdress) {
        return sparWebServiceImpl.serviceProviderRegistration(name, surname, contactnumber, emailaddress, physicaladdress);
    }

    @WebMethod(operationName = "RegisterClient")
    public String clientRegistration(@WebParam(name = "name") String name, @WebParam(name = "surname") String surname, @WebParam(name = "contactnumber") String contactnumber, @WebParam(name = "email") String emailaddress) {
        return sparWebServiceImpl.clientRegistration(name, surname, contactnumber, emailaddress);
    }

    @WebMethod(operationName = "BookAppointment")
    public String appointmentBooking(@WebParam(name = "appointment_date") String date, @WebParam(name = "appointmentstatus") String status, @WebParam(name = "serviceprovider") Object serviceprovider) {
        return sparWebServiceImpl.appointmentBooking(date, status, serviceprovider);
    }
}