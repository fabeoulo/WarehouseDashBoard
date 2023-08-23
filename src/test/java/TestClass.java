
import com.advantech.helper.CronTrigMod;
import com.advantech.helper.HibernateObjectPrinter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static jdk.nashorn.internal.objects.NativeArray.some;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestClass {

    @Autowired
    private CronTrigMod ctm;

//    @Test
    public void testGetBean() {

        try {
            ctm.scheduleJob("PollingTags");
            HibernateObjectPrinter.print("Job PollingTags has been schedule");
        } catch (SchedulerException ex) {
            HibernateObjectPrinter.print(ex.getMessage(), ex);
        }
    }

//    @Test
    public void testSyncData() {
        DateTime lastDayOfPrevWeek = new DateTime().minusWeeks(1).withDayOfWeek(DateTimeConstants.SATURDAY);
        DateTime sDOW = new DateTime(lastDayOfPrevWeek).minusWeeks(4).withTime(0, 0, 0, 0);
        DateTime eDOW = new DateTime(lastDayOfPrevWeek).withTime(23, 0, 0, 0);
        System.out.println(sDOW);
        System.out.println(eDOW);
    }

    @Test
    public void testFindMAC() {
        String mac = "3D:F2:C9:A6:B3:4F";
        String filePath = "C:\\Users\\wei.cheng\\Desktop\\100246-TPAC088254-A-0427_PASS(T2).txt";

        Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})");

        File file = new File(filePath);

        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    System.out.println("Found it on line " + lineNum);
                    System.out.println(line);
//                    break;
                }
            }
            System.out.println("Scan complete");
        } catch (FileNotFoundException e) {
            //handle this
        }

    }
}
