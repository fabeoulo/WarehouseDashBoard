
import com.advantech.api.WebApiClient;
import com.advantech.api.WebApiUser;
import com.advantech.helper.HibernateObjectPrinter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Justin.Yeh
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestApi {

    @Autowired
    private WebApiClient wc;

    @Test
    public void testUserInAtmc() {
        String jobNo = "A-10376";
        System.out.println("wc.baseUrl= " + wc.getBaseUrl());
        WebApiUser atmcUser = wc.getUserInAtmc(jobNo);
        if (atmcUser != null) {
            System.out.println(" atmcUser.getEmplr_Id()= " + atmcUser.getEmplr_Id());
            HibernateObjectPrinter.print(atmcUser.getLocal_Name());
            System.out.println(" atmcUser.getEmail_Addr= " + atmcUser.getEmail_Addr());
        }
        HibernateObjectPrinter.print(atmcUser);
    }
}
