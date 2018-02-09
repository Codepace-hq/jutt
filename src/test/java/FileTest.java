import io.codepace.jutt.file.Utils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class FileTest {

    @Test public void testFileUtils(){
        String size = Utils.byteCountToDisplaySize(new BigInteger("1024"));
        assertEquals(size, "1 KB");
    }

}
